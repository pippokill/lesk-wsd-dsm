/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.wsd.tool.msc;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pierpaolo
 */
public class MultiSemCorTransformer {
    
    private final Map<String, List<MscObject>> map = new HashMap<>();
    
    private final Multiset<String> occ = HashMultiset.create();
    
    private final Logger logger = Logger.getLogger(MultiSemCorTransformer.class.getName());
    
    private String convertWnsn(String wnsn) {
        StringBuilder sb = new StringBuilder();
        sb.append(wnsn.substring(2));
        sb.append(wnsn.substring(0, 1));
        return sb.toString();
    }
    
    private void processFile(File file) throws IOException, ParserConfigurationException, SAXException {
        logger.log(Level.INFO, "Processing file: {0}", file);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document parse = docBuilder.parse(file);
        NodeList structElems = parse.getElementsByTagName("struct");
        for (int i = 0; i < structElems.getLength(); i++) {
            NodeList feats = structElems.item(i).getChildNodes();
            String lemma = null;
            String wnsn = null;
            for (int j = 0; j < feats.getLength(); j++) {
                Node f = feats.item(j);
                if (f.getNodeType() == Node.ELEMENT_NODE && f.getNodeName().equals("feat")) {
                    String type = f.getAttributes().getNamedItem("type").getNodeValue();
                    String value = f.getTextContent().trim();
                    if (type.equals("lemma")) {
                        lemma = value;
                    } else if (type.equals("wnsn")) {
                        wnsn = value;
                    }
                }
            }
            if (lemma != null && wnsn != null) {
                String convWnsn=convertWnsn(wnsn);
                occ.add(convWnsn);
                String key = lemma + "#" + wnsn.substring(0, 1);
                List<MscObject> list = map.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(key, list);
                }
                int indexOf = list.indexOf(new MscObject(convWnsn));
                if (indexOf >= 0) {
                    list.get(indexOf).setScore(list.get(indexOf).getScore() + 1);
                } else {
                    list.add(new MscObject(convWnsn, 1));
                }
            }
        }
    }
    
    private void save(String outputDir) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + "/sense.occ"));
        Iterator<Multiset.Entry<String>> iterator = occ.entrySet().iterator();
        while (iterator.hasNext()) {
            Multiset.Entry<String> entry = iterator.next();
            writer.append(entry.getElement()).append("\t").append(String.valueOf(entry.getCount()));
            writer.newLine();
        }
        writer.close();
        
        writer = new BufferedWriter(new FileWriter(outputDir + "/sense.freq"));
        Iterator<String> iterator1 = map.keySet().iterator();
        while (iterator1.hasNext()) {
            String key = iterator1.next();
            List<MscObject> list = map.get(key);
            float norm = 0;
            for (MscObject o : list) {
                norm += o.getScore();
            }
            norm += (float) list.size();
            for (MscObject o : list) {
                o.setScore((o.getScore() + 1) / (norm));
            }
            writer.append(key);
            for (MscObject o : list) {
                writer.append("\t");
                writer.append(o.toFileLine());
            }
            writer.newLine();
        }
        writer.close();
    }
    
    private void processDir(File startDir, String outputPath) throws IOException, ParserConfigurationException, SAXException {
        map.clear();
        occ.clear();
        File[] listFiles = startDir.listFiles();
        for (File file : listFiles) {
            processFile(file);
        }
        //save
        save(outputPath);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MultiSemCorTransformer transformer = new MultiSemCorTransformer();
            transformer.processDir(new File(args[0]), args[1]);
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(MultiSemCorTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class MscObject implements Comparable<MscObject> {
        
        private String id;
        
        private float score;
        
        public MscObject(String id, float score) {
            this.id = id;
            this.score = score;
        }
        
        public MscObject(String id) {
            this.id = id;
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public float getScore() {
            return score;
        }
        
        public void setScore(float score) {
            this.score = score;
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.id);
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
            final MscObject other = (MscObject) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }
        
        @Override
        public int compareTo(MscObject o) {
            return Float.compare(o.score, score);
        }
        
        public String toFileLine() {
            return id + "\t" + String.valueOf(score);
        }
        
    }
    
}
