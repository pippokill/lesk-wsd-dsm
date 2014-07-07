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
