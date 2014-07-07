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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class RemapFreq {

    /** Remap meanings probability info between two different releases of WordNet
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            WnMapping mapping = new WnMapping(new File(args[0]));
            mapping.init();
            BufferedReader reader = new BufferedReader(new FileReader(args[1]));
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] split = line.split("\t");
                if (split.length > 2) {
                    writer.append(split[0]);
                    for (int k = 1; k < split.length; k = k + 2) {
                        String map = mapping.map(split[k]);
                        if (map == null) {
                            map = split[k];
                            Logger.getLogger(RemapFreq.class.getName()).log(Level.WARNING, "No mapping for {0}", split[k]);
                        }
                        writer.append("\t").append(map).append("\t");
                        writer.append(split[k + 1]);
                    }
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(RemapFreq.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
