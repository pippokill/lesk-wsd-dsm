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

package di.uniba.it.wsd.tool.wn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Build synsets probability information exploiting the index.sense WordNet file
 * @author pierpaolo
 */
public class BuildFreqSense {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(args[1])));
            String currentKey = "";
            double tot = 0;
            List<SynsetObject> currentList = new ArrayList<>();
            while (in.ready()) {
                String[] values = in.readLine().split("\\s+");
                String[] keys = values[0].split("%");
                String[] poss = keys[1].split(":");
                if (currentKey.equals(keys[0] + "%" + poss[0])) {
                    SynsetObject so = new SynsetObject(values[1]);
                    double occ = Double.parseDouble(values[3]);
                    so.setScore(occ + 1);
                    tot += occ;
                    if (poss[0].equals("1")) {
                        so.setPos("n");
                    } else if (poss[0].equals("2")) {
                        so.setPos("v");
                    } else if (poss[0].equals("3") || poss[0].equals("5")) {
                        so.setPos("a");
                    } else if (poss[0].equals("4")) {
                        so.setPos("r");
                    }
                    currentList.add(so);
                } else {
                    //save current lemma
                    if (currentList.size() > 0) {
                        for (SynsetObject so : currentList) {
                            so.setScore(so.getScore() / (tot + (double) currentList.size()));
                        }
                        Collections.sort(currentList);
                        out.append(currentKey.split("%")[0]).append("#").append(currentList.get(0).getPos());
                        for (SynsetObject so : currentList) {
                            out.append(" ").append(so.toFile());
                        }
                        out.newLine();
                    }
                    currentKey = keys[0] + "%" + poss[0];
                    currentList.clear();
                    tot=0;
                    SynsetObject so = new SynsetObject(values[1]);
                    double occ = Double.parseDouble(values[3]);
                    so.setScore(occ + 1);
                    tot += occ;
                    if (poss[0].equals("1")) {
                        so.setPos("n");
                    } else if (poss[0].equals("2")) {
                        so.setPos("v");
                    } else if (poss[0].equals("3") || poss[0].equals("5")) {
                        so.setPos("a");
                    } else if (poss[0].equals("4")) {
                        so.setPos("r");
                    }
                    currentList.add(so);
                }
            }
            in.close();
            out.close();
        } catch (Exception ioex) {
            Logger.getLogger(BuildFreqSense.class.getName()).log(Level.SEVERE, "IO Error", ioex);
        }
    }

    private static class SynsetObject implements Comparable<SynsetObject> {

        String id;

        String pos;

        double score;

        public SynsetObject(String id, String pos, double score) {
            this.id = id;
            this.pos = pos;
            this.score = score;
        }

        public SynsetObject(String id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + Objects.hashCode(this.id);
            hash = 11 * hash + Objects.hashCode(this.pos);
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
            final SynsetObject other = (SynsetObject) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.pos, other.pos)) {
                return false;
            }
            return true;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public int compareTo(SynsetObject o) {
            return Double.compare(this.score, o.score);
        }

        public String toFile() {
            return id + pos + " " + score;
        }

    }

}
