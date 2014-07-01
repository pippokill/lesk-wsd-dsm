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
public class RemapOcc {

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
                if (split.length > 1) {
                    String map = mapping.map(split[0]);
                    if (map == null) {
                        map = split[0];
                        Logger.getLogger(RemapOcc.class.getName()).log(Level.WARNING, "No mapping for {0}", split[0]);
                    }
                    writer.append(map).append("\t").append(split[1]);
                }
                writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(RemapOcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
