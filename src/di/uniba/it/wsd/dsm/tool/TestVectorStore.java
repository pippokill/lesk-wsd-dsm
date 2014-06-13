/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.wsd.dsm.tool;

import di.uniba.it.wsd.dsm.DataVectorStore;
import di.uniba.it.wsd.dsm.LuceneVectorStore;
import di.uniba.it.wsd.dsm.SpaceResult;
import di.uniba.it.wsd.dsm.VectorStore;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class TestVectorStore {

    /**
     * Find similar words in a Word Space
     *
     * @param args vector_file_path word number_of_similar_words
     * vectorstore_type
     */
    public static void main(String[] args) {
        try {
            VectorStore space;
            if (args[3].equalsIgnoreCase("lucene")) {
                space = new LuceneVectorStore();
            } else if (args[3].equals("java")) {
                space = new DataVectorStore();
            } else {
                throw new IllegalArgumentException("No valid vector type: " + args[3]);
            }
            space.init(new File(args[0]));
            List<SpaceResult> similar = space.findSimilar(args[1], Integer.parseInt(args[2]));
            for (SpaceResult sr : similar) {
                System.out.println(sr);
            }
        } catch (IllegalArgumentException | IOException ex) {
            Logger.getLogger(TestVectorStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
