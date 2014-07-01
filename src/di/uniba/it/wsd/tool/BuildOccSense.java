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
package di.uniba.it.wsd.tool;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class BuildOccSense {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
            Multiset<String> synset = HashMultiset.create();
            while (in.ready()) {
                String[] values = in.readLine().split("\\s+");
                String[] keys = values[0].split("%");
                String[] poss = keys[1].split(":");
                String offset = null;
                int occ = Integer.parseInt(values[3]);
                if (poss[0].equals("1")) {
                    offset = values[1] + "n";
                } else if (poss[0].equals("2")) {
                    offset = values[1] + "v";
                } else if (poss[0].equals("3") || poss[0].equals("5")) {
                    offset = values[1] + "a";
                } else if (poss[0].equals("4")) {
                    offset = values[1] + "r";
                }
                for (int i = 0; i < occ; i++) {
                    synset.add(offset);
                }
            }
            in.close();

            BufferedWriter out = new BufferedWriter(new FileWriter(new File(args[1])));
            Iterator<Multiset.Entry<String>> iterator = synset.entrySet().iterator();
            while (iterator.hasNext()) {
                Multiset.Entry<String> entry = iterator.next();
                out.append(entry.getElement()).append("\t").append(String.valueOf(entry.getCount()));
                out.newLine();
            }
            out.close();
        } catch (IOException | NumberFormatException ioex) {
            Logger.getLogger(BuildOccSense.class.getName()).log(Level.SEVERE, "IO Error", ioex);
        }
    }

}
