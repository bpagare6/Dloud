/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SplitMergeFiles;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class SplitFile {

    private static final String suffix = ".splitPart";
    private static Integer count = 0;

    public static Integer splitFile(final String filepath, final String filename, final int numSplits) throws IOException {

        if (numSplits <= 0) {
            throw new IllegalArgumentException("Number of Splits must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        final long sourceSize = Files.size(Paths.get(filepath));
        final long bytesPerSplit = sourceSize / numSplits;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(filepath, "r");
                FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, partFiles, filename);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, partFiles, filename);
            }
        }
        return count;
    }

    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel, List<Path> partFiles, String fname) {
        Path fileName = Paths.get("." + fname + "." + count + suffix);
        try (RandomAccessFile toFile = new RandomAccessFile(fileName.toFile(), "rw");
                FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        } catch (IOException ex) {
            Logger.getLogger(SplitFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        partFiles.add(fileName);
        count++;
    }

}
