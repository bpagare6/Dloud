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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ReceiveFiles {

    private ServerSocket server;

    public ReceiveFiles(ServerSocket server) {
        this.server = server;
    }

    public void receiveFiles() {
        while (true) {
            try {
                Socket client = server.accept();
                Thread thread = new ThreadedServer(client);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(ReceiveFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

class ThreadedServer extends Thread {

    private final String foldername = "Files/";
    private final Socket client;

    public ThreadedServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream is = client.getInputStream();
            OutputStream os = client.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            String filename = dis.readUTF();
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
            Logger.getLogger(ThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
