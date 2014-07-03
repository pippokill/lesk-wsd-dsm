/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.exec;

import di.uniba.it.wsd.OldXMLTextReader;
import di.uniba.it.wsd.PlainTextReader;
import di.uniba.it.wsd.RevisedLeskPaper;
import di.uniba.it.wsd.Utils;
import di.uniba.it.wsd.XMLTextReader;
import di.uniba.it.wsd.data.SynsetOut;
import di.uniba.it.wsd.data.TextReader;
import di.uniba.it.wsd.data.Token;
import di.uniba.it.wsd.dsm.LuceneVectorStore;
import di.uniba.it.wsd.dsm.VectorStore;
import di.uniba.it.wsd.tool.SenseFreqAPI;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class RunWSDPaper {

    private static final String OUTFORMAT_PLAIN = "plain";
    private static final String OUTFORMAT_TASK = "task";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Logger.getLogger(RunWSD.class.getName()).log(Level.INFO, "Init...");
            TextReader reader;
            Properties props = Utils.parseCmd(args);
            if (!props.containsKey("-i")) {
                System.exit(1);
            }
            int mode = TextReader.SENTENCE_MODE;
            String modeS = props.getProperty("-cm");
            if (modeS != null) {
                if (modeS.equals("sent")) {
                    mode = TextReader.SENTENCE_MODE;
                } else if (modeS.equals("doc")) {
                    mode = TextReader.DOC_MODE;
                } else if (modeS.equals("text")) {
                    mode = TextReader.TEXT_MODE;
                } else {
                    Logger.getLogger(RunWSD.class.getName()).log(Level.WARNING, "Mode {0} not valid...using default SENTENCE_MODE", modeS);
                }
            }
            String fileType = props.getProperty("-f");
            if (fileType != null && fileType.equals("xml")) {
                reader = new XMLTextReader(new File(props.getProperty("-i")), mode);
            } else if (fileType != null && fileType.equals("oldxml")) {
                reader = new OldXMLTextReader(new File(props.getProperty("-i")), mode);
            } else {
                reader = new PlainTextReader(new File(props.getProperty("-i")), mode);
            }
            reader.openTextReader();
            String langS = props.getProperty("-lang");
            Language language = Language.EN;
            if (langS != null) {
                if (langS.equals("en")) {
                    language = Language.EN;
                } else if (langS.equals("es")) {
                    language = Language.ES;
                } else if (langS.equals("de")) {
                    language = Language.DE;
                } else if (langS.equals("it")) {
                    language = Language.IT;
                } else if (langS.equals("fr")) {
                    language = Language.FR;
                } else {
                    Logger.getLogger(RunWSD.class.getName()).log(Level.WARNING, "Language {0} not valid...using default ENGLISH language", langS);
                }
            }
            boolean projection = false;
            String projS = props.getProperty("-proj");
            if (projS != null) {
                projection = Boolean.parseBoolean(projS);
            }
            String dsmFilename = props.getProperty("-dsm");
            RevisedLeskPaper wsd;
            if (dsmFilename == null) {
                wsd = new RevisedLeskPaper(language);
                wsd.setStemming(true);
            } else {
                VectorStore dsm = new LuceneVectorStore();
                dsm.init(new File(dsmFilename));
                wsd = new RevisedLeskPaper(language, dsm);
                wsd.setStemming(false);
            }
            String senseFreqFilename = props.getProperty("-sc");
            if (senseFreqFilename != null) {
                SenseFreqAPI sfapi = new SenseFreqAPI(new File(senseFreqFilename));
                sfapi.init();
                wsd.setSenseFreq(sfapi);
            }
            String projDepth = props.getProperty("-depth");
            if (projDepth != null) {
                wsd.setMaxDepth(Integer.parseInt(projDepth));
            }
            String scoreGloss = props.getProperty("-sg");
            if (scoreGloss != null) {
                wsd.setScoreGloss(Boolean.parseBoolean(scoreGloss));
            }
            String sf = props.getProperty("-sf");
            if (sf == null) {
                wsd.setOutType(RevisedLeskPaper.OUT_BABELNET);
            } else if (sf.equals("wn")) {
                wsd.setOutType(RevisedLeskPaper.OUT_WORDNET);
            } else if (sf.equals("bn")) {
                wsd.setOutType(RevisedLeskPaper.OUT_BABELNET);
            } else {
                throw new Exception("Synset format not valid: " + sf);
            }
            String cs = props.getProperty("-c");
            if (cs != null) {
                if (cs.equalsIgnoreCase("max")) {
                    wsd.setContextSize(Integer.MAX_VALUE);
                } else {
                    wsd.setContextSize(Integer.parseInt(cs));
                }
            } else {
                Logger.getLogger(RunWSD.class.getName()).log(Level.WARNING, "Context size {0} not valid...using default context size=5", cs);
                wsd.setContextSize(5);
            }
            String outFormat = OUTFORMAT_PLAIN;
            String of = props.getProperty("-of");
            if (of != null) {
                if (outFormat.equals(OUTFORMAT_PLAIN) || outFormat.equals(OUTFORMAT_TASK)) {
                    outFormat = of;
                } else {
                    throw new Exception("Output format not valid: " + of);
                }
            }
            String stemming = props.getProperty("-stem");
            if (stemming != null) {
                wsd.setStemming(Boolean.parseBoolean(stemming));
            } else {
                wsd.setStemming(false);
            }
            String outFile = props.getProperty("-o");
            BufferedWriter writer = null;
            if (outFile != null) {
                writer = new BufferedWriter(new FileWriter(outFile));
            }
            wsd.init();
            Logger.getLogger(RunWSD.class.getName()).log(Level.INFO, "Starting disambiguation...");
            List<Token> tokens = reader.getTokenList();
            while (tokens != null) {
                wsd.disambiguate(tokens, projection);
                if (writer == null) {
                    for (int i = 0; i < tokens.size(); i++) {
                        System.out.println(tokens.get(i).print());
                    }
                    System.out.println();
                } else {
                    if (outFormat.equals(OUTFORMAT_PLAIN)) {
                        for (int i = 0; i < tokens.size(); i++) {
                            writer.append(tokens.get(i).print());
                            writer.newLine();
                        }
                        writer.newLine();
                        writer.flush();
                    } else if (outFormat.equals(OUTFORMAT_TASK)) {
                        for (int i = 0; i < tokens.size(); i++) {
                            String id = tokens.get(i).getId();
                            if (id != null && tokens.get(i).isToDisambiguate() && !tokens.get(i).getSynsetList().isEmpty()) {
                                writer.append(id.substring(0, id.indexOf("."))).append(" ");
                                writer.append(id).append(" ");
                                //int index = tokens.get(i).getSynsetList().size() - 1;
                                //writer.append(tokens.get(i).getSynsetList().get(index).getSynset()).append(" ");
                                List<SynsetOut> out = tokens.get(i).getSynsetList();
                                if (out.size() == 1) {
                                    writer.append(out.get(0).getSynset()).append(" ");
                                } else {
                                    double variance = wsd.getVariance(out);
                                    if (out.get(out.size() - 1).getScore() - out.get(out.size() - 2).getScore() <= variance) {
                                        writer.append(out.get(out.size() - 1).getSynset()).append(" ").append(out.get(out.size() - 2).getSynset()).append(" ");
                                    } else {
                                        writer.append(out.get(out.size() - 1).getSynset()).append(" ");
                                    }
                                }
                                writer.append("!! lemma=" + tokens.get(i).getLemma()).append("#").append(tokens.get(i).getPos().name());
                                writer.newLine();
                            }
                        }
                        writer.flush();
                    }
                }
                tokens = reader.getTokenList();
            }
            reader.closeTextReader();
            if (writer != null) {
                writer.close();
            }
            System.out.println(wsd.getExecStats());
            wsd.close();
        } catch (Exception ex) {
            Logger.getLogger(RunWSD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
