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

import it.uniroma1.lcl.babelnet.BabelSense;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Load information about senses distribution
 *
 * @author pierpaolo
 */
public class SenseFreqAPI {

    /**
     *
     */
    public Map<String, List<SenseFreq>> map = new HashMap<>();

    /**
     *
     */
    public Map<String, Integer> occs = new HashMap<>();

    private final File freqsFile;

    private final File occsFile;

    private static final Logger logger = Logger.getLogger(SenseFreqAPI.class.getName());

    /**
     *
     * @param freqsFile
     */
    public SenseFreqAPI(File freqsFile) {
        this.freqsFile = freqsFile;
        this.occsFile = null;
    }

    /**
     *
     * @param freqsFile
     * @param occsFile
     */
    public SenseFreqAPI(File freqsFile, File occsFile) {
        this.freqsFile = freqsFile;
        this.occsFile = occsFile;
    }

    private void initFreqs() throws IOException {
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
        in.close();
        logger.log(Level.INFO, "Loaded {0} sense freq info", map.size());
    }

    private void initOccs() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(occsFile));
        while (in.ready()) {
            String[] values = in.readLine().split("\t");
            if (values.length > 1) {
                int occ = Integer.parseInt(values[1]);
                occs.put(values[0], occ);
            }
        }
        in.close();
        logger.log(Level.INFO, "Loaded {0} sense occ info", occs.size());
    }

    /**
     *
     * @throws IOException
     */
    public void init() throws IOException {
        destroy();
        initFreqs();
        initOccs();
    }

    /**
     *
     */
    public void destroy() {
        map.clear();
        occs.clear();
    }

    /**
     *
     * @param key
     * @return
     */
    public List<SenseFreq> getSenseProbabilityList(String key) {
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
     * @param synsetSize
     * @return
     */
    public float getSynsetProbability(String key, String offset, int synsetSize) {
        float score = 1 / (float) synsetSize;
        List<SenseFreq> senseFreq = getSenseProbabilityList(key);
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
     * @param offset
     * @return
     */
    public Float getSynsetOccurrences(String offset) {
        Integer get = occs.get(offset);
        if (get == null) {
            return null;
        } else {
            return get.floatValue();
        }
    }

    /**
     *
     * @param sense
     * @return
     */
    public float getMaxSenseOccurrences(BabelSense sense) {
        float maxOcc = 0;
        for (int l = 0; l < sense.getSynset().getWordNetOffsets().size(); l++) {
            Float occ = getSynsetOccurrences(sense.getSynset().getWordNetOffsets().get(l).getID());
            if (occ != null && occ > maxOcc) {
                maxOcc = occ;
            }
        }
        return maxOcc;
    }

    /**
     *
     * @param key
     * @param sense
     * @param maxSize
     * @return
     */
    public float getMaxSenseProbability(String key, BabelSense sense, int maxSize) {
        float maxProb = 0;
        for (int l = 0; l < sense.getSynset().getWordNetOffsets().size(); l++) {
            float prob = getSynsetProbability(key, sense.getSynset().getWordNetOffsets().get(l).getID(), maxSize);
            if (prob > maxProb) {
                maxProb = prob;
            }
        }
        return maxProb;
    }

    /**
     *
     * @param senses
     * @return
     */
    public float[] getOccurrencesArray(List<BabelSense> senses) {
        float[] a = new float[senses.size()];
        float norma = 0;
        for (int i = 0; i < senses.size(); i++) {
            a[i] = getMaxSenseOccurrences(senses.get(i));
            norma += a[i];
        }
        float size = (float) a.length;
        for (int i = 0; i < a.length; i++) {
            a[i] = (a[i] + 1) / (norma + size);
        }
        return a;
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
