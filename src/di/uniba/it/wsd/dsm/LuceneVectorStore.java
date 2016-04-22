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
package di.uniba.it.wsd.dsm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;

/**
 * This class reads and stores in a Map the word vectors
 *
 * @author pierpaolo
 */
public class LuceneVectorStore implements VectorStore {

    private IndexInput indexInput;
    private final Map<String, float[]> memory = new HashMap<>();

    /**
     *
     * @param file
     * @throws IOException
     */
    public void init(File file) throws IOException {
        FSDirectory dir = FSDirectory.open(file.getParentFile());
        this.indexInput = dir.openInput(file.getName(), IOContext.DEFAULT);
        String header = indexInput.readString(); //skip header
        if ((header.equalsIgnoreCase("-dimensions"))) {
            ObjectVector.vecLength = indexInput.readInt();
        } else if (header.contains("-dimension")) {
            int index = header.indexOf("-dimension");
            ObjectVector.vecLength = Integer.parseInt(header.substring(index + 10).trim());
        }
        loadInRam();
    }

    /**
     *
     * @throws IOException
     */
    private void loadInRam() throws IOException {
        this.indexInput.seek(0);
        memory.clear();
        String header = indexInput.readString(); //skip header
        if ((header.equalsIgnoreCase("-dimensions"))) {
            ObjectVector.vecLength = indexInput.readInt();
        } else if (header.contains("-dimension")) {
            int index = header.indexOf("-dimension");
            ObjectVector.vecLength = Integer.parseInt(header.substring(index + 10).trim());
        }
        while (indexInput.getFilePointer() < indexInput.length()) {
            String term = indexInput.readString();
            float[] v = new float[ObjectVector.vecLength];
            for (int k = 0; k < v.length; k++) {
                v[k] = Float.intBitsToFloat(indexInput.readInt());
            }
            memory.put(term, v);
        }
        Logger.getLogger(LuceneVectorStore.class.getName()).log(Level.INFO, "Loaded {0} vectors", memory.size());
    }

    /**
     *
     * @param set
     * @return
     * @throws IOException
     */
    public Map<String, float[]> prefetch(Set<String> set) throws IOException {
        Logger.getLogger(LuceneVectorStore.class.getName()).log(Level.INFO, "Prefetching for {0} vectors", set.size());
        this.indexInput.seek(0);
        Map<String, float[]> map = new HashMap<>();
        String header = indexInput.readString(); //skip header
        if ((header.equalsIgnoreCase("-dimensions"))) {
            ObjectVector.vecLength = indexInput.readInt();
        } else if (header.contains("-dimension")) {
            int index = header.indexOf("-dimension");
            ObjectVector.vecLength = Integer.parseInt(header.substring(index + 10).trim());
        }
        while (indexInput.getFilePointer() < indexInput.length()) {
            String term = indexInput.readString();
            if (set.contains(term)) {
                float[] v = new float[ObjectVector.vecLength];
                for (int k = 0; k < v.length; k++) {
                    v[k] = Float.intBitsToFloat(indexInput.readInt());
                }
                map.put(term, v);
            } else {
                this.indexInput.seek(indexInput.getFilePointer() + ObjectVector.vecLength * 4);
            }
        }
        Logger.getLogger(LuceneVectorStore.class.getName()).log(Level.INFO, "Prefetched {0} vectors", map.size());
        return map;
    }

    /**
     *
     * @param term
     * @return
     * @throws IOException
     */
    public float[] getFileVector(String term) throws IOException {
        this.indexInput.seek(0);
        String header = indexInput.readString(); //skip header
        if ((header.equalsIgnoreCase("-dimensions"))) {
            ObjectVector.vecLength = indexInput.readInt();
        } else if (header.contains("-dimension")) {
            int index = header.indexOf("-dimension");
            ObjectVector.vecLength = Integer.parseInt(header.substring(index + 10).trim());
        }
        while (indexInput.getFilePointer() < indexInput.length()) {
            String key = indexInput.readString();
            if (key.equals(term)) {
                float[] v = new float[ObjectVector.vecLength];
                for (int k = 0; k < v.length; k++) {
                    v[k] = Float.intBitsToFloat(indexInput.readInt());
                }
                return v;
            } else {
                this.indexInput.seek(indexInput.getFilePointer() + ObjectVector.vecLength * 4);
            }
        }
        throw new IOException("Vector for " + term + " not found");
    }

    /**
     *
     * @param vector
     * @param n
     * @return
     * @throws IOException
     */
    public List<SpaceResult> findFileSimilar(float[] vector, int n) throws IOException {
        PriorityQueue<SpaceResult> queue = new PriorityQueue<>();
        indexInput.seek(0);
        String header = indexInput.readString(); //skip header
        if ((header.equalsIgnoreCase("-dimensions"))) {
            ObjectVector.vecLength = indexInput.readInt();
        } else if (header.contains("-dimension")) {
            int index = header.indexOf("-dimension");
            ObjectVector.vecLength = Integer.parseInt(header.substring(index + 10).trim());
        }
        while (indexInput.getFilePointer() < indexInput.length()) {
            String key = indexInput.readString();
            float[] v = new float[ObjectVector.vecLength];
            for (int k = 0; k < v.length; k++) {
                v[k] = Float.intBitsToFloat(indexInput.readInt());
            }
            float score = VectorUtils.scalarProduct(vector, v);
            if (queue.size() < n) {
                queue.offer(new SpaceResult(key, score));
            } else {
                queue.poll();
                queue.offer(new SpaceResult(key, score));
            }

        }
        queue.poll();
        List<SpaceResult> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }

    /**
     *
     * @param term
     * @return
     */
    @Override
    public float[] getVector(String term) {
        return memory.get(term);
    }

    /**
     *
     * @param word
     * @param n
     * @return
     */
    @Override
    public List<SpaceResult> findSimilar(String word, int n) {
        float[] v1 = memory.get(word);
        if (v1 == null) {
            Logger.getLogger(LuceneVectorStore.class.getName()).log(Level.WARNING, "No vector for term: {0}", word);
            return new ArrayList<>();
        }
        PriorityQueue<SpaceResult> queue = new PriorityQueue<>();
        Iterator<String> iterator = memory.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            float[] v2 = memory.get(key);
            float score = VectorUtils.scalarProduct(v1, v2);
            if (queue.size() < n) {
                queue.offer(new SpaceResult(key, score));
            } else {
                queue.poll();
                queue.offer(new SpaceResult(key, score));
            }
        }
        queue.poll();
        List<SpaceResult> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }

    /**
     *
     * @param vector
     * @param n
     * @return
     */
    public List<SpaceResult> findSimilar(float[] vector, int n) {
        PriorityQueue<SpaceResult> queue = new PriorityQueue<>();
        Iterator<String> iterator = memory.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            float[] v2 = memory.get(key);
            float score = VectorUtils.scalarProduct(vector, v2);
            if (queue.size() < n) {
                queue.offer(new SpaceResult(key, score));
            } else {
                queue.poll();
                queue.offer(new SpaceResult(key, score));
            }
        }
        queue.poll();
        List<SpaceResult> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }

    /**
     *
     * @param map
     * @param word
     * @param n
     * @return
     */
    public List<SpaceResult> findSimilar(Map<String, float[]> map, String word, int n) {
        float[] v1 = map.get(word);
        if (v1 == null) {
            Logger.getLogger(LuceneVectorStore.class.getName()).log(Level.WARNING, "No vector for term: {0}", word);
            return new ArrayList<>();
        }
        PriorityQueue<SpaceResult> queue = new PriorityQueue<>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            float[] v2 = map.get(key);
            float score = VectorUtils.scalarProduct(v1, v2);
            if (queue.size() < n) {
                queue.offer(new SpaceResult(key, score));
            } else {
                queue.poll();
                queue.offer(new SpaceResult(key, score));
            }
        }
        queue.poll();
        List<SpaceResult> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }

    /**
     *
     * @param map
     * @param vector
     * @param n
     * @return
     */
    public List<SpaceResult> findSimilar(Map<String, float[]> map, float[] vector, int n) {
        PriorityQueue<SpaceResult> queue = new PriorityQueue<>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            float[] v2 = map.get(key);
            float score = VectorUtils.scalarProduct(vector, v2);
            if (queue.size() < n) {
                queue.offer(new SpaceResult(key, score));
            } else {
                queue.poll();
                queue.offer(new SpaceResult(key, score));
            }
        }
        queue.poll();
        List<SpaceResult> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }
}
