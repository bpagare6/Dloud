/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

import CreateConnections.ConnectionEntry;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class SendParts {

    private List<ConnectionEntry> ipConnectionList = new ArrayList<>();
    private final String filename;
    private final int numParts;

    public SendParts(String filename, List<ConnectionEntry> ipConnectionList, int numParts) {
        this.filename = filename;
        this.ipConnectionList = ipConnectionList;
        this.numParts = numParts;
    }

    public void sendData() {
        ConnectionEntry connEntry;
        int startPoint, endPoint;
        int partEachPC = numParts / ipConnectionList.size();
        startPoint = 0;
        endPoint = partEachPC;
        for (ConnectionEntry ipConnection : ipConnectionList) {
            connEntry = ipConnection;
            Thread t = new ThreadedSending(startPoint, endPoint, connEntry.getSocket(), filename, connEntry.getDataOutputStream());
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SendParts.class.getName()).log(Level.SEVERE, null, ex);
            }
            startPoint += partEachPC;
            if (connEntry == ipConnectionList.get(ipConnectionList.size() - 1)) {
                endPoint = numParts;
            } else {
                endPoint += partEachPC;
            }
        }
    }

}

class ThreadedSending extends Thread {

    int startCounter, endCounter;
    Socket s;
    String filename;
    OutputStream out;

    public ThreadedSending(int startCounter, int endCounter, Socket s, String filename, OutputStream out) {
        this.startCounter = startCounter;
        this.endCounter = endCounter;
        this.s = s;
        this.filename = filename;
        this.out = out;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            BufferedInputStream bis;
            FileInputStream file;
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            dos.flush();
            dos.writeUTF("receive");
            dos.flush();
            String flag = dis.readUTF();
            if (flag.equals("1")) {
                System.out.println("Request to upload " + this.filename);
//                dos.flush();
                dos.writeUTF(this.filename);
                dos.writeInt(this.startCounter);
                dos.writeInt(this.endCounter);
                System.out.println("Startcounter: " + this.startCounter + " Endcounter: " + this.endCounter);
                for (int i = startCounter; i < endCounter; i++) {
                    String fname = "." + filename + "." + i + ".splitPart";
                    file = new FileInputStream(fname);
                    bis = new BufferedInputStream(file);
                    dos.writeInt((int) file.getChannel().size());
                    sendBytes(bis, out, (int) file.getChannel().size());
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ThreadedSending.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ThreadedSending.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Thread Time : " + (System.currentTimeMillis() - startTime) / 1000);
    }

    private static void sendBytes(BufferedInputStream in, OutputStream out, int filesize) throws Exception {

        byte[] data = new byte[filesize];
        int bytes = 0;
        int c = in.read(data, 0, filesize);
        out.write(data, 0, filesize);
        out.flush();
    }
}
