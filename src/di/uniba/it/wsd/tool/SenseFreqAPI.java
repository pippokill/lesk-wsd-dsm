/**
   Copyright (c) 2014, the LESK-WSD-DSM AUTHORS.

   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:

 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided
   with the distribution.

 * Neither the name of the University of Bari nor the names
   of its contributors may be used to endorse or promote products
   derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    
   GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 **/

package di.uniba.it.wsd.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Load information about senses distribution
 * @author pierpaolo
 */
public class SenseFreqAPI {

    /**
     *
     */
    public Map<String, List<SenseFreq>> map = new HashMap<>();

    private final File freqsFile;

    /**
     *
     * @param freqsFile
     */
    public SenseFreqAPI(File freqsFile) {
        this.freqsFile = freqsFile;
    }

    /**
     *
     * @throws IOException
     */
    public void init() throws IOException {
        destroy();
        BufferedReader in = new BufferedReader(new FileReader(freqsFile));
        while (in.ready()) {
            String[] values = in.readLine().split("\\s+");
            List<SenseFreq> list = new ArrayList<>();
            if (values.length >= 3) {
                for (int i = 1; i < values.length; i = i + 2) {
                    SenseFreq sf = new SenseFreq(values[i], Float.parseFloat(values[i + 1]));
                    list.add(sf);
                }
                Collections.sort(list);
                map.put(values[0], list);
            }
        }
    }

    /**
     *
     */
    public void destroy() {
        map.clear();
    }

    /**
     *
     * @param key
     * @return
     */
    public List<SenseFreq> getSenseFreqList(String key) {
        List<SenseFreq> get = map.get(key);
        if (get == null) {
            return new ArrayList<>();
        } else {
            return get;
        }
    }

    /**
     *
     * @param key
     * @param offset
     * @return
     */
    /*public float getFreq(String key, String offset) {
        List<SenseFreq> senseFreq = getSenseFreqList(key);
        float score;
        if (senseFreq.isEmpty())
            score=0;
        else {
            score=1f/(float) senseFreq.size();
        }
        for (SenseFreq elem : senseFreq) {
            if (elem.getId().equals(offset)) {
                score = elem.getFreq();
                break;
            }
        }
        return score;
    }*/
    /*public Float getFreq(String key, String offset) {
        List<SenseFreq> senseFreq = getSenseFreqList(key);
        Float score=null;
        for (SenseFreq elem : senseFreq) {
            if (elem.getId().equals(offset)) {
                score = elem.getFreq();
                break;
            }
        }
        return score;
    }*/
    public float getFreq(String key, String offset) {
        List<SenseFreq> senseFreq = getSenseFreqList(key);
        float score;
        if (senseFreq.isEmpty())
            score=0;
        else {
            score=1f/(float) senseFreq.size();
        }
        for (SenseFreq elem : senseFreq) {
            if (elem.getId().equals(offset)) {
                score = elem.getFreq();
                break;
            }
        }
        return score;
    }

    /**
     *
     */
    public class SenseFreq implements Comparable<SenseFreq> {

        private String id;

        private float freq;

        /**
         *
         * @param id
         * @param freq
         */
        public SenseFreq(String id, float freq) {
            this.id = id;
            this.freq = freq;
        }

        /**
         *
         * @return
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         */
        public float getFreq() {
            return freq;
        }

        /**
         *
         * @param freq
         */
        public void setFreq(float freq) {
            this.freq = freq;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + Objects.hashCode(this.id);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SenseFreq other = (SenseFreq) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(SenseFreq o) {
            return Float.compare(o.freq, freq);
        }

    }

}
