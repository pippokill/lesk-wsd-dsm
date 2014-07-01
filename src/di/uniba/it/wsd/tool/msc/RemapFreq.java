/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    /**
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
