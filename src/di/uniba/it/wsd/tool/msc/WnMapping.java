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
package di.uniba.it.wsd.tool.msc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class WnMapping {

    private final Map<String, String> map = new HashMap<>();

    private final File dir;

    /**
     * Create a new Mapper between two different releases of WordNet
     * @param dir
     */
    public WnMapping(File dir) {
        this.dir = dir;
    }

    private String chooseBestCandidate(String[] split) {
        int i = 1;
        double max = 0;
        String syn = null;
        while (i < split.length) {
            double score = Double.parseDouble(split[i + 1]);
            if (score > max) {
                syn = split[i];
            }
            i = i + 2;
        }
        return syn;
    }

    /**
     * Init mapper
     * @throws IOException
     */
    public void init() throws IOException {
        map.clear();
        File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            String suf = "";
            if (file.getName().endsWith("noun")) {
                suf = "n";
            } else if (file.getName().endsWith("verb")) {
                suf = "v";
            } else if (file.getName().endsWith("adj")) {
                suf = "a";
            } else if (file.getName().endsWith("adv")) {
                suf = "r";
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] split = line.split(" ");
                String syn = chooseBestCandidate(split);
                map.put(split[0] + suf, syn + suf);
            }
            reader.close();
        }
    }
    
    /**
     * Map a synset
     * @param key source synset
     * @return mapped synset
     */
    public String map(String key) {
        return map.get(key);
    }

}
