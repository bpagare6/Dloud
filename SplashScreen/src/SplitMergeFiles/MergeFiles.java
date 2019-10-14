/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SplitMergeFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author bhushan
 */
public class MergeFiles {

    public void mergeFile(final String filename, final int numberOfParts) {
        String dir = "~/Downloads/";
        String suffix = ".splitPart";
        String iF = dir + "." + filename + ".";

        String oF = dir + filename;

        try {
            FileOutputStream output = new FileOutputStream(new File(oF));
            WritableByteChannel targetChannel = output.getChannel();
            Integer counter = 0;
            while (true) {
                String fname = iF.concat(counter.toString());
                System.out.println(fname);
                fname = fname.concat(suffix);
                System.out.println(fname);
                try (
                        FileInputStream input = new FileInputStream(fname);
                        FileChannel inputChannel = input.getChannel()) {
                    inputChannel.transferTo(0, inputChannel.size(), targetChannel);
                    counter++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
