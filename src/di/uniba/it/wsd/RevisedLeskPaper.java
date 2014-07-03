/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.wsd;

import di.uniba.it.wsd.data.ExecuteStatistics;
import di.uniba.it.wsd.data.POSenum;
import di.uniba.it.wsd.data.RelatedSynset;
import di.uniba.it.wsd.data.SynsetOut;
import di.uniba.it.wsd.data.Token;
import di.uniba.it.wsd.dsm.ObjectVector;
import di.uniba.it.wsd.dsm.VectorStore;
import di.uniba.it.wsd.dsm.VectorUtils;
import di.uniba.it.wsd.tool.SenseFreqAPI;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.POS;
import it.uniroma1.lcl.babelnet.BabelGloss;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSenseSource;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.stat.StatUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import org.tartarus.snowball.ext.porterStemmer;
import org.tartarus.snowball.ext.spanishStemmer;

/**
 *
 * @author pierpaolo
 */
public class RevisedLeskPaper {

    public static final int OUT_WORDNET = 1000;
    public static final int OUT_BABELNET = 2000;
    private int outType = OUT_BABELNET;
    private BabelNet babelNet;
    private int contextSize = 5;
    private Language language;
    private boolean stemming = false;
    private VectorStore dsm;
    private int maxDepth = 1;
    private SenseFreqAPI senseFreq;
    public boolean scoreGloss = true;
    private ExecuteStatistics execStats = new ExecuteStatistics();
    private static final Logger logger = Logger.getLogger(RevisedLeskPaper.class.getName());

    public RevisedLeskPaper(Language language) {
        this.language = language;
    }

    public RevisedLeskPaper(Language language, VectorStore dsm) {
        this.language = language;
        this.dsm = dsm;
    }

    public SenseFreqAPI getSenseFreq() {
        return senseFreq;
    }

    public void setSenseFreq(SenseFreqAPI senseFreq) {
        this.senseFreq = senseFreq;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getContextSize() {
        return contextSize;
    }

    public void setContextSize(int contextSize) {
        this.contextSize = contextSize;
    }

    public void init() {
        babelNet = BabelNet.getInstance();
    }

    public void close() {
    }

    private SnowballStemmer getStemmer(Language language) {
        if (language.equals(Language.EN)) {
            return new porterStemmer();
        } else if (language.equals(Language.ES)) {
            return new spanishStemmer();
        } else if (language.equals(Language.FR)) {
            return new frenchStemmer();
        } else if (language.equals(Language.DE)) {
            return new germanStemmer();
        } else if (language.equals(Language.IT)) {
            return new italianStemmer();
        } else {
            return null;
        }
    }

    public Map<String, Float> buildBag(String text) throws IOException {
        Map<String, Float> bag = new HashMap<>();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        SnowballStemmer stemmer = null;
        if (stemming) {
            stemmer = getStemmer(language);
            if (stemmer == null) {
                Logger.getLogger(RevisedLesk.class.getName()).log(Level.WARNING, "No stemmer for language {0}", language);
            }
        }
        TokenStream tokenStream = analyzer.tokenStream("gloss", new StringReader(text));
        while (tokenStream.incrementToken()) {
            TermAttribute token = (TermAttribute) tokenStream.getAttribute(TermAttribute.class);
            String term = token.term();
            if (stemmer != null) {
                stemmer.setCurrent(term);
                if (stemmer.stem()) {
                    term = stemmer.getCurrent();
                }
            }
            Float c = bag.get(term);
            if (c == null) {
                bag.put(term, 1f);
            } else {
                bag.put(term, c + 1f);
            }
        }
        return bag;
    }

    private Map<String, Float> buildContext(List<Token> sentence, int pivot) throws Exception {
        int i = pivot - 1;
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 && c < contextSize) {
            if (sentence.get(i).getPos() != POSenum.OTHER) {
                sb.append(sentence.get(i).getToken());
                sb.append(" ");
                c++;
            }
            i--;
        }
        i = pivot + 1;
        c = 0;
        while (i < sentence.size() && c < contextSize) {
            if (sentence.get(i).getPos() != POSenum.OTHER) {
                sb.append(sentence.get(i).getToken());
                sb.append(" ");
                c++;
            }
            i++;
        }
        return buildBag(sb.toString());
    }

    private Map<String, Float> buildTextContext(List<Token> sentence) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Token token : sentence) {
            if (token.getPos() != POSenum.OTHER) {
                sb.append(token.getToken());
                sb.append(" ");
            }
        }
        return buildBag(sb.toString());
    }

    private void getRelatedSynsets(Map<BabelSynset, RelatedSynset> map, int distance) throws IOException {
        List<BabelSynset> listKey = new ArrayList<>(map.keySet());
        for (BabelSynset synset : listKey) {
            RelatedSynset get = map.get(synset);
            if (!get.isVisited()) {
                get.setVisited(true);
                Map<IPointer, List<BabelSynset>> relatedMap = synset.getRelatedMap();
                Iterator<IPointer> itRel = relatedMap.keySet().iterator();
                while (itRel.hasNext()) {
                    IPointer pointer = itRel.next();
                    if (!pointer.getName().equalsIgnoreCase("antonym")) {
                        List<BabelSynset> list = relatedMap.get(pointer);
                        for (BabelSynset relSynset : list) {
                            RelatedSynset rs = map.get(relSynset);
                            if (rs == null) {
                                map.put(relSynset, new RelatedSynset(relSynset, distance));
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, Float> buildGlossBag(BabelSynset synset) throws IOException {
        Map<BabelSynset, RelatedSynset> relatedMap = new HashMap<>();
        relatedMap.put(synset, new RelatedSynset(synset, 0));
        for (int i = 0; i < maxDepth; i++) {
            getRelatedSynsets(relatedMap, i + 1);
        }
        /*List<BabelNetGraphEdge> successorEdges = babelNet.getSuccessorEdges(synset.getId());
         for (BabelNetGraphEdge edge : successorEdges) {
         if (edge.getLanguage().equals(this.language)) {
         String target = edge.getTarget();
         if (!edge.getPointer().getName().equalsIgnoreCase("antonym")) {
         BabelSynset synsetSucc = babelNet.getSynsetFromId(target);
         if (!relatedMap.containsKey(synsetSucc)) {
         relatedMap.put(synsetSucc, new RelatedSynset(synsetSucc, 1));
         //System.out.println("Added "+synsetSucc);
         }
         }
         } else {
         //System.out.println("No english");
         }
         }*/
        Iterator<BabelSynset> itRel = relatedMap.keySet().iterator();
        Map<String, Float> bag = new HashMap<>();
        while (itRel.hasNext()) {
            BabelSynset relSynset = itRel.next();
            RelatedSynset rs = relatedMap.get(relSynset);
            List<BabelGloss> glosses = relSynset.getGlosses(this.language);
            List<String> glossesToProcess = new ArrayList<>();
            execStats.incrementTotalGloss();
            if (glosses.isEmpty()) {
                logger.log(Level.FINEST, "No gloss for synset: {0}", relSynset);
                execStats.incrementNoGloss();
                List<BabelSense> senses = relSynset.getSenses(this.language);
                StringBuilder sb = new StringBuilder();
                for (BabelSense bs : senses) {
                    sb.append(bs.getLemma().replace("_", " ")).append(" ");
                }
                glossesToProcess.add(sb.toString());
            } else {
                for (BabelGloss gloss : glosses) {
                    glossesToProcess.add(gloss.getGloss());
                }
            }
            float df = maxDepth + 1 - rs.getDistance();
            for (String gloss : glossesToProcess) {
                Map<String, Float> gbag = buildBag(gloss);
                Iterator<String> iterator = gbag.keySet().iterator();
                while (iterator.hasNext()) {
                    String term = iterator.next();
                    Float c = bag.get(term);
                    if (c == null) {
                        bag.put(term, df * gbag.get(term));
                    } else {
                        bag.put(term, c + df * gbag.get(term));
                    }
                }
            }
        }
        return bag;
    }

    private float gf(List<Map<String, Float>> mapList, String key) {
        float gf = 0;
        for (Map<String, Float> map : mapList) {
            if (map.containsKey(key)) {
                gf++;
            }
        }
        return gf;
    }

    private List<Map<String, Float>> buildGlossBag(List<BabelSense> senses) throws IOException {
        List<Map<String, Float>> mapList = new ArrayList<>();
        for (BabelSense sense : senses) {
            mapList.add(buildGlossBag(sense.getSynset()));
        }
        if (scoreGloss) {
            for (Map<String, Float> map : mapList) {
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    float igf = 1 + (float) (Math.log(senses.size() / gf(mapList, key)) / Math.log(2));
                    float gf = map.get(key);
                    map.put(key, gf * igf);
                }
            }
        }
        return mapList;
    }

    private double simBag(Map<String, Float> bag1, Map<String, Float> bag2) {
        double n1 = 0;
        double n2 = 0;
        double ip = 0;
        Iterator<String> it1 = bag1.keySet().iterator();
        while (it1.hasNext()) {
            String t1 = it1.next();
            Float v1 = bag1.get(t1);
            if (bag2.containsKey(t1)) {
                ip += v1.doubleValue() * bag2.get(t1).doubleValue();
            }
            n1 += Math.pow(v1, 2);
        }
        Iterator<Float> it2 = bag2.values().iterator();
        while (it2.hasNext()) {
            n2 += Math.pow(it2.next().doubleValue(), 2);
        }
        return ip / (n1 * n2);
    }

    private float[] buildVector(Map<String, Float> bag, boolean normalize) {
        Iterator<String> it = bag.keySet().iterator();
        float[] bagv = new float[ObjectVector.vecLength];
        while (it.hasNext()) {
            String t1 = it.next();
            float w = bag.get(t1).floatValue();
            float[] v = dsm.getVector(t1);
            execStats.incrementTotalSVhit();
            if (v != null) {
                for (int k = 0; k < v.length; k++) {
                    bagv[k] += w * v[k];
                }
            } else {
                execStats.incrementMissSV();
            }
        }
        if (normalize && !VectorUtils.isZeroVector(bagv)) {
            bagv = VectorUtils.getNormalizedVector(bagv);
        }
        return bagv;
    }

    private double simVector(Map<String, Float> bag1, Map<String, Float> bag2) {
        float[] gv1 = buildVector(bag1, true);
        float[] gv2 = buildVector(bag2, true);
        return VectorUtils.scalarProduct(gv1, gv2);
    }

    private double sim(Map<String, Float> bag1, Map<String, Float> bag2) {
        if (dsm != null) {
            return simVector(bag1, bag2);
        } else {
            return simBag(bag1, bag2);
        }
    }

    private List<BabelSense> lookupSense(Language language, String lemma, POS postag) throws IOException {
        List<BabelSense> senses;
        if (language.equals(Language.EN)) {
            senses = babelNet.getSenses(language, lemma, postag, BabelSenseSource.WN);
            if (senses == null || senses.isEmpty()) {
                senses = babelNet.getSenses(language, lemma.replace(" ", "_"), postag, BabelSenseSource.WN);
            }
        } else {
            senses = babelNet.getSenses(language, lemma, postag, BabelSenseSource.WNTR);
            if (senses == null || senses.isEmpty()) {
                senses = babelNet.getSenses(language, lemma.replace(" ", "_"), postag, BabelSenseSource.WNTR);
            }
        }
        if (senses == null || senses.isEmpty()) {
            senses = babelNet.getSenses(language, lemma, postag);
            if (senses == null || senses.isEmpty()) {
                senses = babelNet.getSenses(language, lemma.replace(" ", "_"), postag);
            }
        }
        if (senses == null || senses.isEmpty()) {
            Logger.getLogger(RevisedLesk.class.getName()).log(Level.WARNING, "No senses for {0}, pos-tag {1}", new Object[]{lemma, postag});
        }
        if (senses != null && !senses.isEmpty()) {
            Set<String> ids = new HashSet<>();
            for (int i = senses.size() - 1; i >= 0; i--) {
                if (!ids.add(senses.get(i).getSynset().getId())) {
                    senses.remove(i);
                }
            }
        }
        return senses;
    }

    private String convertPosEnum(POSenum pos) {
        if (pos == POSenum.NOUN) {
            return "n";
        } else if (pos == POSenum.VERB) {
            return "v";
        } else if (pos == POSenum.ADJ) {
            return "a";
        } else if (pos == POSenum.ADV) {
            return "r";
        } else {
            return "o";
        }
    }

    public void disambiguate(List<Token> sentence, boolean projection) throws Exception {
        execStats = new ExecuteStatistics();
        float[] textVector = null;
        if (projection) {
            Map<String, Float> bagContext = buildTextContext(sentence);
            textVector = buildVector(bagContext, true);
        }
        for (int i = 0; i < sentence.size(); i++) {
            Token token = sentence.get(i);
            if (token.isToDisambiguate() && token.getSynsetList().isEmpty()) {
                Map<String, Float> contextBag = buildContext(sentence, i);
                float[] contextVector = null;
                if (projection) {
                    contextVector = buildVector(contextBag, true);
                }
                if (token.getPos() != POSenum.OTHER) {
                    List<BabelSense> senses = null;
                    if (token.getPos() == POSenum.NOUN) {
                        senses = lookupSense(language, token.getLemma(), POS.NOUN);
                    } else if (token.getPos() == POSenum.VERB) {
                        senses = lookupSense(language, token.getLemma(), POS.VERB);
                    } else if (token.getPos() == POSenum.ADJ) {
                        senses = lookupSense(language, token.getLemma(), POS.ADJECTIVE);
                    } else if (token.getPos() == POSenum.ADV) {
                        senses = lookupSense(language, token.getLemma(), POS.ADVERB);
                    }
                    if (senses != null) {
                        List<Map<String, Float>> buildGlossBag = buildGlossBag(senses);
                        for (int j = 0; j < senses.size(); j++) {
                            double sim = 0;
                            Map<String, Float> bag = buildGlossBag.get(j);
                            if (projection) {
                                //build projection of context on text
                                float[] vectorProjection = VectorUtils.getVectorProjection(contextVector, textVector);
                                //compute similarity
                                sim = VectorUtils.scalarProduct(vectorProjection, buildVector(bag, true));
                            } else {
                                sim = sim(contextBag, bag);
                            }
                            if (senseFreq != null) {
                                if (language.equals(Language.EN)) {
                                    String lemmakey = token.getLemma() + "#" + convertPosEnum(token.getPos());
                                    float freq = senseFreq.getSynsetProbability(lemmakey, senses.get(j).getWordNetOffset(), senses.size());
                                    //sim = freq * sim;
                                    sim = 0.5 * sim + 0.5 * freq;
                                } else {
                                    String mainSense = senses.get(j).getSynset().getMainSense();
                                    if (mainSense != null && !mainSense.startsWith("WIKI:") && mainSense.length() > 0) {
                                        int si = mainSense.lastIndexOf("#");
                                        if (si >= 0) {
                                            String lemmakey = mainSense.substring(0, si);
                                            float maxFreq = 0;
                                            for (int l = 0; l < senses.get(j).getSynset().getWordNetOffsets().size(); l++) {
                                                float freq = senseFreq.getSynsetProbability(lemmakey, senses.get(j).getSynset().getWordNetOffsets().get(l), senses.size());
                                                if (freq > maxFreq) {
                                                    maxFreq = freq;
                                                }
                                            }
                                            sim = 0.5 * sim + 0.5 * maxFreq;
                                        }
                                    }
                                }
                            }
                            if (outType == OUT_BABELNET) {
                                token.getSynsetList().add(new SynsetOut(senses.get(j).getSynset().getId(), sim));
                            } else if (outType == OUT_WORDNET) {
                                token.getSynsetList().add(new SynsetOut(senses.get(j).getSensekey(), sim));
                            } else {
                                throw new Exception("Output type not valid: " + outType);
                            }
                        }
                        Collections.sort(token.getSynsetList());
                        //logger.log(Level.FINEST, "{0}\tmean: {1}\tvariance: {2}", new Object[]{token.toString(), getMean(token.getSynsetList()), getVariance(token.getSynsetList())});
                    }
                }
            }
        }
    }

    public double getMean(List<SynsetOut> list) {
        double[] scores = new double[list.size()];
        int l = 0;
        for (SynsetOut out : list) {
            scores[l] = out.getScore();
            l++;
        }
        return StatUtils.mean(scores);
    }

    public double getVariance(List<SynsetOut> list) {
        double[] scores = new double[list.size()];
        int l = 0;
        for (SynsetOut out : list) {
            scores[l] = out.getScore();
            l++;
        }
        return StatUtils.variance(scores);
    }

    public BabelNet getBabelNet() {
        return babelNet;
    }

    /**
     * @return the stemming
     */
    public boolean isStemming() {
        return stemming;
    }

    /**
     * @param stemming the stemming to set
     */
    public void setStemming(boolean stemming) {
        this.stemming = stemming;
    }

    /**
     * @return the outType
     */
    public int getOutType() {
        return outType;
    }

    /**
     * @param outType the outType to set
     */
    public void setOutType(int outType) {
        this.outType = outType;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public boolean isScoreGloss() {
        return scoreGloss;
    }

    public void setScoreGloss(boolean scoreGloss) {
        this.scoreGloss = scoreGloss;
    }

    public ExecuteStatistics getExecStats() {
        return execStats;
    }

}
