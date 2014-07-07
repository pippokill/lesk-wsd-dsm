/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package di.uniba.it.wsd.dsm;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author pierpaolo
 */
public interface VectorStore {
    
    /**
     *
     * @param inputFile
     * @throws IOException
     */
    public void init(File inputFile) throws IOException;
    
    /**
     *
     * @param term
     * @return
     */
    public float[] getVector(String term);
    
    /**
     *
     * @param word
     * @param n
     * @return
     */
    public List<SpaceResult> findSimilar(String word, int n);
    
}
