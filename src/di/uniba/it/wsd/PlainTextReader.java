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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * TextReader for the plain input format
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class PlainTextReader implements TextReader {

    private final File inputFile;
    private BufferedReader in;
    private int mode = TextReader.SENTENCE_MODE;
    private List<List<Token>> passages;
    private int pointer = 0;

    /**
     *
     * @param inputFile
     * @param mode
     */
    public PlainTextReader(File inputFile, int mode) {
        this.inputFile = inputFile;
        this.mode = mode;
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void openTextReader() throws Exception {
        in = new BufferedReader(new FileReader(inputFile));
        passages = loadPassages();
        pointer = 0;

    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void closeTextReader() throws Exception {
    }

    private List<List<Token>> loadPassages() throws Exception {
        List<List<Token>> list = new ArrayList<>();
        list.add(new ArrayList<Token>());
        int position = 0;
        while (in.ready()) {
            String line = in.readLine();
            if (line.length() > 0) {
                String[] split = line.split("\t");
                if (split.length >= 3) {
                    position++;
                    POSenum pos = POSenum.OTHER;
                    if (split[1].equalsIgnoreCase("n")) {
                        pos = POSenum.NOUN;
                    } else if (split[1].equalsIgnoreCase("v")) {
                        pos = POSenum.VERB;
                    } else if (split[1].equalsIgnoreCase("j")) {
                        pos = POSenum.ADJ;
                    } else if (split[1].equalsIgnoreCase("r")) {
                        pos = POSenum.ADV;
                    }
                    list.get(list.size() - 1).add(new Token(split[0], split[2], pos, position));
                }
            } else {
                if (mode == SENTENCE_MODE) {
                    position = 0;
                    list.add(new ArrayList<Token>());
                }
            }
        }
        in.close();
        return list;
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
