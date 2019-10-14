/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

import java.io.File;

/**
 *
 * @author bhushan
 */
public class CleanPartFiles {

    String filename;

    public CleanPartFiles(String filename) {
        this.filename = filename;
    }

    public void cleanup() {
        System.out.println("Cleaning up the files");
        File folder = new File(".");
        for (File file : folder.listFiles()) {
            if (file.getName().startsWith("." + filename)) {
                file.delete();
            }
        }
    }

}
