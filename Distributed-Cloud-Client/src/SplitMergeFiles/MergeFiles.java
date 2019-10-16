/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SplitMergeFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class MergeFiles {

    public void mergeFile(final String filename, final int numberOfParts) {
        String home = System.getProperty("user.home");
        String dir = home + "/Downloads/";
        String suffix = ".splitPart";
        String iF = dir + "." + filename + ".";

        String oF = dir + filename;

        try {
            File file = new File(oF);
            file.createNewFile();
            FileOutputStream output = new FileOutputStream(new File(oF));
            WritableByteChannel targetChannel = output.getChannel();
            Integer counter = 0;
            while (counter <= numberOfParts) {
                String fname = iF.concat(counter.toString());
                fname = fname.concat(suffix);
                try (
                        FileInputStream input = new FileInputStream(fname);
                        FileChannel inputChannel = input.getChannel()) {
                    inputChannel.transferTo(0, inputChannel.size(), targetChannel);
                    counter++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MergeFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
