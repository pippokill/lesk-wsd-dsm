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

import java.util.ArrayList;
import java.util.List;

/**
 * Container for storing information about a token
 * @author Pierpaolo Basile pierpaolo.basile@gmail.com
 */
public class Token {

    private String token;
    private String lemma;
    private POSenum pos;
    private int position;
    private List<SynsetOut> synsetList = new ArrayList<>();
    private String id;
    private boolean toDisambiguate = true;

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public boolean isToDisambiguate() {
        return toDisambiguate;
    }

    /**
     *
     * @param toDisambiguate
     */
    public void setToDisambiguate(boolean toDisambiguate) {
        this.toDisambiguate = toDisambiguate;
    }

    /**
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @return
     */
    public String getLemma() {
        return lemma;
    }

    /**
     *
     * @param lemma
     */
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    /**
     *
     * @return
     */
    public POSenum getPos() {
        return pos;
    }

    /**
     *
     * @param pos
     */
    public void setPos(POSenum pos) {
        this.pos = pos;
    }

    /**
     *
     * @return
     */
    public int getPosition() {
        return position;
    }

    /**
     *
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     *
     * @return
     */
    public List<SynsetOut> getSynsetList() {
        return synsetList;
    }

    /**
     *
     * @param synsetList
     */
    public void setSynsetList(List<SynsetOut> synsetList) {
        this.synsetList = synsetList;
    }

    /**
     *
     * @param token
     * @param lemma
     * @param pos
     */
    public Token(String token, String lemma, POSenum pos) {
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
    }

    /**
     *
     * @param token
     * @param lemma
     * @param pos
     * @param position
     */
    public Token(String token, String lemma, POSenum pos, int position) {
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
        this.position = position;
    }

    /**
     *
     * @param token
     * @param lemma
     * @param pos
     * @param position
     * @param id
     * @param toDisambiguate
     */
    public Token(String token, String lemma, POSenum pos, int position, String id, boolean toDisambiguate) {
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
        this.position = position;
        this.id = id;
        this.toDisambiguate = toDisambiguate;
    }

    /**
     *
     * @param token
     * @param lemma
     * @param pos
     * @param position
     * @param toDisambiguate
     */
    public Token(String token, String lemma, POSenum pos, int position, boolean toDisambiguate) {
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
        this.position = position;
        this.toDisambiguate = toDisambiguate;
    }

    @Override
    public String toString() {
        return "Token{" + "token=" + token + ", lemma=" + lemma + ", pos=" + pos + ", position=" + position + ", synsetList=" + synsetList + '}';
    }

    /**
     *
     * @return
     */
    public String print() {
        return print(true);
    }

    /**
     *
     * @param score
     * @return
     */
    public String print(boolean score) {
        StringBuilder sb = new StringBuilder();
        sb.append(token).append("\t").append(pos.name()).append("\t").append(lemma).append("\t");
        if (this.getSynsetList().size() > 0) {
            sb.append(this.getSynsetList().get(this.getSynsetList().size() - 1).getSynset());
            if (score) {
                sb.append("/");
                sb.append(this.getSynsetList().get(this.getSynsetList().size() - 1).getScore());
            }
        } else {
            sb.append("U");
        }
        return sb.toString();
    }
}
