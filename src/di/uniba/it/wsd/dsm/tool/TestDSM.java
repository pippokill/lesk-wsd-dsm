/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.wsd.dsm.tool;

import di.uniba.it.wsd.dsm.SpaceResult;
import di.uniba.it.wsd.dsm.VectorSpace;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class TestDSM {

    /** 
     * Find similar words in a Word Space
     * @param args vector_file_path word number_of_similar_words
     */
    public static void main(String[] args) {
        try {
            VectorSpace space = new VectorSpace(new File(args[0]));
            space.init();
            space.loadInRam();
            List<SpaceResult> similar = space.findSimilar(args[1], Integer.parseInt(args[2]));
            for (SpaceResult sr : similar) {
                System.out.println(sr);
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDSM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
