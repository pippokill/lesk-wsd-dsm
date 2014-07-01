/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package di.uniba.it.wsd.tool;

import edu.mit.jwi.item.POS;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class TestBabelNet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BabelNet babelNet = BabelNet.getInstance();
            List<BabelSense> senses = babelNet.getSenses(Language.IT, "suono",POS.NOUN,BabelSenseSource.WNTR);
            for (BabelSense sense:senses) {
                System.out.println(sense.getSynset().getId()+"\t"+sense.getPosition());
            }
        } catch (IOException ex) {
            Logger.getLogger(TestBabelNet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
