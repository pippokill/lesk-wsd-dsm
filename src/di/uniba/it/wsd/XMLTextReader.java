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

import di.uniba.it.wsd.data.TextReader;
import di.uniba.it.wsd.data.POSenum;
import di.uniba.it.wsd.data.Token;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Text reader for the SemEval-2013 Task-2 XML format
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class XMLTextReader implements TextReader {

    private final File inputFile;
    private Document doc;
    private List<List<Token>> passages;
    private int pointer = 0;
    private int mode = TextReader.SENTENCE_MODE;

    /**
     *
     * @param inputFile
     * @param mode
     */
    public XMLTextReader(File inputFile, int mode) {
        this.inputFile = inputFile;
        this.mode = mode;
    }

    private List<Token> loadTokenFromSentence(Node sentence) throws Exception {
        List<Token> list = new ArrayList<>();
        int position = 0;
        NodeList childs = sentence.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node item = childs.item(i);
            if (item.getNodeName().equals("wf") || item.getNodeName().equals("instance")) {
                String token = item.getTextContent();
                Node nodeLemma = item.getAttributes().getNamedItem("lemma");
                String lemma = token;
                if (nodeLemma != null) {
                    lemma = item.getAttributes().getNamedItem("lemma").getNodeValue();
                }
                Node nodePos = item.getAttributes().getNamedItem("pos");
                String posTag = "UNKNOWN";
                if (nodePos != null) {
                    posTag = item.getAttributes().getNamedItem("pos").getNodeValue();
                }
                position++;
                POSenum pos = POSenum.OTHER;
                if (posTag.startsWith("N")) {
                    pos = POSenum.NOUN;
                } else if (posTag.startsWith("V")) {
                    pos = POSenum.VERB;
                } else if (posTag.startsWith("J")) {
                    pos = POSenum.ADJ;
                } else if (posTag.startsWith("R")) {
                    pos = POSenum.ADV;
                }
                if (item.getNodeName().equals("wf")) {
                    list.add(new Token(token, lemma, pos, position, false));
                }
                if (item.getNodeName().equals("instance")) {
                    String id = item.getAttributes().getNamedItem("id").getNodeValue();
                    list.add(new Token(token, lemma, pos, position, id, true));
                }

            }
        }

        return list;
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void openTextReader() throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = db.parse(inputFile);
        passages = new ArrayList<>();
        pointer = 0;
        if (mode == TextReader.SENTENCE_MODE) {
            NodeList sentences = doc.getElementsByTagName("sentence");
            for (int i = 0; i < sentences.getLength(); i++) {
                passages.add(loadTokenFromSentence(sentences.item(i)));
            }
        } else if (mode == TextReader.TEXT_MODE) {
            NodeList textNodes = doc.getElementsByTagName("text");
            for (int i = 0; i < textNodes.getLength(); i++) {
                passages.add(new ArrayList<Token>());
                NodeList childNodes = textNodes.item(i).getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE && childNodes.item(j).getNodeName().equals("sentence")) {
                        passages.get(i).addAll(loadTokenFromSentence(childNodes.item(j)));
                    }
                }
            }
        } else if (mode == TextReader.DOC_MODE) {
            passages.add(new ArrayList<Token>());
            NodeList sentences = doc.getElementsByTagName("sentence");
            for (int i = 0; i < sentences.getLength(); i++) {
                passages.get(0).addAll(loadTokenFromSentence(sentences.item(i)));
            }
        } else {
            throw new Exception("No valid reader mode");
        }
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void closeTextReader() throws Exception {
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Token> getTokenList() throws Exception {
        List<Token> tokens = null;
        if (pointer < passages.size()) {
            tokens = passages.get(pointer);
            pointer++;
        }
        return tokens;
    }

    /**
     *
     * @param writer
     * @throws Exception
     */
    public void write(Writer writer) throws Exception {
        for (int i = 0; i < this.getTokenList().size(); i++) {
            writer.write(this.getTokenList().get(i).print());
            writer.write("\n");
        }
    }
}
