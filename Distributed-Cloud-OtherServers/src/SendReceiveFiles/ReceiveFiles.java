/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ReceiveFiles {

    private final Socket client;
    private final InputStream is;
    private final OutputStream os;
    private final DataInputStream dis;

    public ReceiveFiles(Socket client, InputStream is, OutputStream os, DataInputStream dis) {
        this.client = client;
        this.is = is;
        this.os = os;
        this.dis = dis;
    }

    public void receiveFiles() {
        System.out.println("Receiving files");
        String foldername = "Files/";
        try {
            String filename = dis.readUTF();
//            String filename = (String) is.read();
            System.out.println("Filename: " + filename);
            int startCounter = dis.readInt();
            int endCounter = dis.readInt();
            String name;

            int fsize;
            for (int i = startCounter; i < endCounter; i++) {
                fsize = dis.readInt();
                try {
                    boolean complete = true;
                    name = "." + filename + "." + i + ".splitPart";
                    File directory = new File(foldername);
                    if (!directory.exists()) {
                        directory.mkdir();
                    }

                    int size = fsize;
                    byte[] data = new byte[size];
                    File fc = new File(directory, name);
                    System.out.println("FC: " + fc);
                    try (FileOutputStream fileOut = new FileOutputStream(fc)) {
                        DataOutputStream dataOut = new DataOutputStream(fileOut);

                        byte[] buf = new byte[fsize];
                        int fileSize = fsize;
                        int n;
                        while (fileSize > 0 && (n = is.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
                            fileOut.write(buf, 0, n);
                            fileSize -= n;
                        }
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ReceiveFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
