/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.wsd.tool.msc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class WnMapping {

    private Map<String, String> map = new HashMap<>();

    private File dir;

    public WnMapping(File dir) {
        this.dir = dir;
    }

    private String chooseBestCandidate(String[] split) {
        int i = 1;
        double max = 0;
        String syn = null;
        while (i < split.length) {
            double score = Double.parseDouble(split[i + 1]);
            if (score > max) {
                syn = split[i];
            }
            i = i + 2;
        }
        return syn;
    }

    public void init() throws IOException {
        map.clear();
        File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            String suf = "";
            if (file.getName().endsWith("noun")) {
                suf = "n";
            } else if (file.getName().endsWith("verb")) {
                suf = "v";
            } else if (file.getName().endsWith("adj")) {
                suf = "a";
            } else if (file.getName().endsWith("adv")) {
                suf = "r";
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] split = line.split(" ");
                String syn = chooseBestCandidate(split);
                map.put(split[0] + suf, syn + suf);
            }
            reader.close();
        }
    }
    
    public String map(String key) {
        return map.get(key);
    }

}
