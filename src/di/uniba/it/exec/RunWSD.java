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
package di.uniba.it.exec;

import di.uniba.it.wsd.OldXMLTextReader;
import di.uniba.it.wsd.PlainTextReader;
import di.uniba.it.wsd.RevisedLesk;
import di.uniba.it.wsd.Utils;
import di.uniba.it.wsd.XMLTextReader;
import di.uniba.it.wsd.data.SynsetOut;
import di.uniba.it.wsd.data.TextReader;
import di.uniba.it.wsd.data.Token;
import di.uniba.it.wsd.dsm.DataVectorStore;
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
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class RunWSD {

    private static final String OUTFORMAT_PLAIN = "plain";
    private static final String OUTFORMAT_TASK = "task";

    /**
     * The main class which requires the following parameters:
     *
     * -i *input_file*: the file which contains the text that will be
     * disambiguated
     *
     * -o *output_file*: the file in which you want to save the output
     *
     * -cm *sent|doc|text*: indicates how the input text will be read and
     * disambiguated. The available values are: "sent" one sentence per time,
     * "doc" one doc per time, "text" read the full file
     *
     * -f *xml|oldxml|plain*: indicates the input file format. The available
     * values are: "xml" for the SemEval-2013 Task 12 file format
     * (http://www.cs.york.ac.uk/semeval-2013/task12/), "oldxml" old SemEval XML
     * format, "plain" for plain text. The plain text must contain one token for
     * each line. Each line must contains three values separated by TAB: token,
     * pos-tag, lemma. The pos-tag has four possible values: n (noun), v (verb),
     * j (adjective), r (adverb), any other character for other tags. An example
     * is reported in the *text* folder
     *
     * -dsm *file_path*: the binary file which describes the distributional
     * semantic model. A model for the English language is provided into the
     * *resoruces/dsm* folder
     *
     * -lang *it|en|es|de|fr*: the language of the input text, *en* is the
     * default value
     *
     * -sc *file_path*: the file which contains information about synsets
     * distribution. A file extracted from WordNet 3.0 is provided in
     * *resources/sense* folder
     *
     * -sg *true|false*: enable/disable gloss scoring function (true is the
     * default value)
     *
     * -sf *wn|bn*: output synset format: "wn" uses WordNet offset, while "bn"
     * uses BabelNet id (bn is the default value)
     *
     * -c *integer value|max*: indicates the context size, "max" considers the
     * whole text
     *
     * -of *plain|task*: output format: "plain" uses plain text file to store
     * word meanings, while task uses the file format defined in SemEval-2013
     * Task 12
     *
     * -stem *true|false*: enable/disable glosses stemming
     *
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
                switch (modeS) {
                    case "sent":
                        mode = TextReader.SENTENCE_MODE;
                        break;
                    case "doc":
                        mode = TextReader.DOC_MODE;
                        break;
                    case "text":
                        mode = TextReader.TEXT_MODE;
                        break;
                    default:
                        Logger.getLogger(RunWSD.class.getName()).log(Level.WARNING, "Mode {0} not valid...using default SENTENCE_MODE", modeS);
                        break;
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
                switch (langS) {
                    case "en":
                        language = Language.EN;
                        break;
                    case "es":
                        language = Language.ES;
                        break;
                    case "de":
                        language = Language.DE;
                        break;
                    case "it":
                        language = Language.IT;
                        break;
                    case "fr":
                        language = Language.FR;
                        break;
                    default:
                        Logger.getLogger(RunWSD.class.getName()).log(Level.WARNING, "Language {0} not valid...using default language {1}", new Object[]{langS, language});
                        break;
                }
            }
            String dsmFilename = props.getProperty("-dsm");
            RevisedLesk wsd;
            if (dsmFilename == null) {
                wsd = new RevisedLesk(language);
                wsd.setStemming(true);
            } else {
                String dsmType = props.getProperty("-dsmType");
                VectorStore dsm = null;
                if (dsmType != null && dsmType.equals("java")) {
                    dsm = new DataVectorStore();
                } else {
                    dsm = new LuceneVectorStore();
                }
                dsm.init(new File(dsmFilename));
                wsd = new RevisedLesk(language, dsm);
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
                wsd.setOutType(RevisedLesk.OUT_BABELNET);
            } else if (sf.equals("wn")) {
                wsd.setOutType(RevisedLesk.OUT_WORDNET);
            } else if (sf.equals("bn")) {
                wsd.setOutType(RevisedLesk.OUT_BABELNET);
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
                wsd.disambiguate(tokens);
                if (writer == null) {
                    for (Token token : tokens) {
                        System.out.println(token.print());
                    }
                    System.out.println();
                } else {
                    switch (outFormat) {
                        case OUTFORMAT_PLAIN:
                            for (Token token : tokens) {
                                writer.append(token.print());
                                writer.newLine();
                            }
                            writer.newLine();
                            writer.flush();
                            break;
                        case OUTFORMAT_TASK:
                            for (Token token : tokens) {
                                String id = token.getId();
                                if (id != null && token.isToDisambiguate() && !token.getSynsetList().isEmpty()) {
                                    writer.append(id.substring(0, id.indexOf("."))).append(" ");
                                    writer.append(id).append(" ");
                                    List<SynsetOut> out = token.getSynsetList();
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
                                    writer.append("!! lemma=" + token.getLemma()).append("#").append(token.getPos().name());
                                    writer.newLine();
                                }
                            }
                            //write sentence to file
                            writer.flush();
                            break;
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
