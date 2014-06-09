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

package di.uniba.it.wsd.data;

import java.util.Objects;

/**
 * Container for storing information about the output synset assigned to the word
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class SynsetOut implements Comparable<SynsetOut> {
    
    private String synset;
    
    private String wnSynset;
    
    private double score;

    /**
     *
     * @param synset
     * @param score
     */
    public SynsetOut(String synset, double score) {
        this.synset = synset;
        this.score = score;
    }

    /**
     *
     * @param synset
     * @param wnSynset
     * @param score
     */
    public SynsetOut(String synset, String wnSynset, double score) {
        this.synset = synset;
        this.wnSynset = wnSynset;
        this.score = score;
    }

    /**
     *
     * @return
     */
    public String getWnSynset() {
        return wnSynset;
    }

    /**
     *
     * @param wnSynset
     */
    public void setWnSynset(String wnSynset) {
        this.wnSynset = wnSynset;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.synset);
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
        final SynsetOut other = (SynsetOut) obj;
        if (!Objects.equals(this.synset, other.synset)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getSynset() {
        return synset;
    }

    /**
     *
     * @param synset
     */
    public void setSynset(String synset) {
        this.synset = synset;
    }

    /**
     *
     * @return
     */
    public double getScore() {
        return score;
    }

    /**
     *
     * @param score
     */
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(SynsetOut o) {
        return Double.compare(this.score, o.getScore());
    }

    @Override
    public String toString() {
        return "SynsetOut{" + "synset=" + synset + ", score=" + score + '}';
    }
    
    
    
}
