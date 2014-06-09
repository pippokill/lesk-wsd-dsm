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
package di.uniba.it.wsd.data;

import it.uniroma1.lcl.babelnet.BabelSynset;
import java.util.Objects;

/**
 * Container for storing a related synset
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class RelatedSynset {

    private BabelSynset synset;
    private int distance;
    private boolean visited = false;

    /**
     *
     * @param synset
     */
    public RelatedSynset(BabelSynset synset) {
        this.synset = synset;
    }

    /**
     *
     * @param synset
     * @param distance
     */
    public RelatedSynset(BabelSynset synset, int distance) {
        this.synset = synset;
        this.distance = distance;
    }

    /**
     *
     * @return
     */
    public BabelSynset getSynset() {
        return synset;
    }

    /**
     *
     * @param synset
     */
    public void setSynset(BabelSynset synset) {
        this.synset = synset;
    }

    /**
     *
     * @return
     */
    public int getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     *
     * @param visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.synset);
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
        final RelatedSynset other = (RelatedSynset) obj;
        if (!Objects.equals(this.synset, other.synset)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RelatedSynset{" + "synset=" + synset + ", distance=" + distance + '}';
    }
}
