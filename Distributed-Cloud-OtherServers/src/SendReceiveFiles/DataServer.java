/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package SendReceiveFiles;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class DataServer {

    private static ServerSocket server;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(8001);
            while (true) {
                Socket client = server.accept();
                System.out.println("Client connected: " + client.getInetAddress());
                Thread thread = new ThreadedDataServer(client);
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(DataServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

class ThreadedDataServer extends Thread {

    private Thread sendf;
    private Thread receivef;
    private final Socket client;
    private final ExecutorService receiveExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private final ExecutorService sendExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public ThreadedDataServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream is = this.client.getInputStream();
            OutputStream os = this.client.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            while (true) {
                String request = dis.readUTF();

                if (request.equals("send")) {
                    // sending files
                    dos.writeUTF("1");
                    SendFiles sendFiles = new SendFiles(client, dis, dos);
                    sendFiles.sendData();
                } else {
                    // receiving files
                    dos.writeUTF("1");
                    ReceiveFiles receiveFiles = new ReceiveFiles(client, is, os, dis);
                    receiveFiles.receiveFiles();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadedDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
