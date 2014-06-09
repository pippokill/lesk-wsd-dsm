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

package di.uniba.it.wsd.dsm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexOutput;

/**
 *
 * @author pierpaolo
 */
public class Text2Bin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            args=new String[] {"/media/pierpaolo/storage/data/wiki/it/termsvector.text",
                "/media/pierpaolo/storage/data/wiki/it/termsvector.29.bin"};
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            File file = new File(args[1]);
            FSDirectory fs = FSDirectory.open(file.getParentFile());
            IndexOutput output = fs.createOutput(file.getName());
            String header = in.readLine();
            output.writeString("-dimensions");
            output.writeInt(Integer.parseInt(header));
            while (in.ready()) {
                String line = in.readLine();
                String[] split = line.split("\t");
                output.writeString(split[0]);
                for (int i=1;i<split.length;i++) {
                    output.writeInt(Float.floatToIntBits(Float.parseFloat(split[i])));
                }
            }
            in.close();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(Text2Bin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
