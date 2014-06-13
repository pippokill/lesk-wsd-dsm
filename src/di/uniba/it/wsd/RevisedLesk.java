/**
 * Copyright (c) 2014, the LESK-WSD-DSM AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
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
 * This class implements the Word Sense Disambiguation algorithm
 *
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class RevisedLesk {

    /**
     * Constant for WordNet output format
     */
    public static final int OUT_WORDNET = 1000;

    /**
     * Constant for BabelNet output format
     */
    public static final int OUT_BABELNET = 2000;
    private int outType = OUT_BABELNET;
    private BabelNet babelNet;
    private int contextSize = 5;
    private Language language;
    private boolean stemming = false;
    private VectorStore dsm;
    private int maxDepth = 1;
    private SenseFreqAPI senseFreq;
    private boolean scoreGloss = true;
    private ExecuteStatistics execStats = new ExecuteStatistics();
    private static final Logger logger = Logger.getLogger(RevisedLesk.class.getName());

    /**
     *
     * @param language
     */
    public RevisedLesk(Language language) {
        this.language = language;
    }

    /**
     *
     * @param language
     * @param dsm
     */
    public RevisedLesk(Language language, VectorStore dsm) {
        this.language = language;
        this.dsm = dsm;
    }

    /**
     *
     * @return
     */
    public SenseFreqAPI getSenseFreq() {
        return senseFreq;
    }

    /**
     *
     * @param senseFreq
     */
    public void setSenseFreq(SenseFreqAPI senseFreq) {
        this.senseFreq = senseFreq;
    }

    /**
     *
     * @return
     */
    public Language getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     *
     * @return
     */
    public int getContextSize() {
        return contextSize;
    }

    /**
     *
     * @param contextSize
     */
    public void setContextSize(int contextSize) {
        this.contextSize = contextSize;
    }

    /**
     *
     * @return
     */
    public VectorStore getDsm() {
        return dsm;
    }

    /**
     *
     */
    public void init() {
        babelNet = BabelNet.getInstance();
    }

    /**
     *
     */
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

    /**
     *
     * @param text
     * @return
     * @throws IOException
     */
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
            List<BabelGloss> glosses = relSynset.getGlosses(language);
            List<String> glossesToProcess = new ArrayList<>();
            execStats.incrementTotalGloss();
            if (glosses.isEmpty()) {
                logger.log(Level.FINEST, "No gloss for synset: {0}", relSynset);
                execStats.incrementNoGloss();
                /*List<BabelSense> senses = relSynset.getSenses(this.language);
                 StringBuilder sb = new StringBuilder();
                 for (BabelSense bs : senses) {
                 sb.append(bs.getLemma().replace("_", " ")).append(" ");
                 }
                 glossesToProcess.add(sb.toString());*/
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
            float w = bag.get(t1);
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

    /**
     *
     * @param sentence
     * @throws Exception
     */
    public void disambiguate(List<Token> sentence) throws Exception {
        execStats = new ExecuteStatistics();
        System.out.println();
        for (int i = 0; i < sentence.size(); i++) {
            System.out.print(".");
            Token token = sentence.get(i);
            if (token.isToDisambiguate() && token.getSynsetList().isEmpty()) {
                Map<String, Float> contextBag = buildContext(sentence, i);
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
                            sim = sim(contextBag, bag);
                            if (senseFreq != null) {
                                if (language.equals(Language.EN)) {
                                    String lemmakey = token.getLemma() + "#" + convertPosEnum(token.getPos());
                                    float freq = senseFreq.getFreq(lemmakey, senses.get(j).getWordNetOffset());
                                    //sim = freq * sim;
                                    sim = 0.5 * sim + 0.5 * freq;
                                } else {
                                    String mainSense = senses.get(j).getSynset().getMainSense();
                                    if (mainSense != null && !mainSense.startsWith("WIKI:") && mainSense.length() > 0) {
                                        int si = mainSense.lastIndexOf("#");
                                        if (si >= 0) {
                                            String lemmakey = mainSense.substring(0, si);
                                            float maxFreq = 1 / (float) senses.size();
                                            for (int l = 0; l < senses.get(j).getSynset().getWordNetOffsets().size(); l++) {
                                                float freq = senseFreq.getFreq(lemmakey, senses.get(j).getSynset().getWordNetOffsets().get(l));
                                                if (freq > maxFreq) {
                                                    maxFreq = freq;
                                                }
                                            }
                                            sim = 0.5 * sim + 0.5 * maxFreq;
                                        }
                                    } else {
                                        sim = 0.5 * sim + 0.5 / (double) senses.size();
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

    /**
     *
     * @param list
     * @return
     */
    public double getMean(List<SynsetOut> list) {
        double[] scores = new double[list.size()];
        int l = 0;
        for (SynsetOut out : list) {
            scores[l] = out.getScore();
            l++;
        }
        return StatUtils.mean(scores);
    }

    /**
     *
     * @param list
     * @return
     */
    public double getVariance(List<SynsetOut> list) {
        double[] scores = new double[list.size()];
        int l = 0;
        for (SynsetOut out : list) {
            scores[l] = out.getScore();
            l++;
        }
        return StatUtils.variance(scores);
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     *
     * @param maxDepth
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     *
     * @return
     */
    public boolean isScoreGloss() {
        return scoreGloss;
    }

    /**
     *
     * @param scoreGloss
     */
    public void setScoreGloss(boolean scoreGloss) {
        this.scoreGloss = scoreGloss;
    }

    /**
     *
     * @return
     */
    public ExecuteStatistics getExecStats() {
        return execStats;
    }

}
