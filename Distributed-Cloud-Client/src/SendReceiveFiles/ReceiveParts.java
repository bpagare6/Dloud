package SendReceiveFiles;

import CreateConnections.ConnectionEntry;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bhushan
 */
public class ReceiveParts {

    private List<ConnectionEntry> ipListConnected;

    public ReceiveParts(List<ConnectionEntry> ipListConnected) {
        this.ipListConnected = ipListConnected;
    }

    public void receiveParts(String filename) {
        ipListConnected.stream().map((connectionEntry) -> {
            return new ThreadedServer(connectionEntry.getSocket(), filename);
        }).forEach((thread) -> {
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReceiveParts.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}

class ThreadedServer extends Thread {

    private final String foldername = "/home/bhushan/Downloads/";
    private final Socket client;
    private final String filename;

    public ThreadedServer(Socket client, String filename) {
        this.client = client;
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            System.out.println("Connection:" + client);
            InputStream is = client.getInputStream();
            OutputStream os = client.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF("send");
            dos.flush();
            String flag = dis.readUTF();
            if (flag.equals("1")) {
                dos.writeUTF(filename);
                System.out.println("Request to download " + filename);
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
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
