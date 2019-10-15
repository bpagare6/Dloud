/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

import java.io.IOException;
import java.net.ServerSocket;
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
        } catch (IOException ex) {
            Logger.getLogger(DataServer.class.getName()).log(Level.SEVERE, null, ex);
        }
//          Socket client = server.accept();
//            System.out.println("Thread connected at: " + client.getInetAddress());
//        Thread thread = new ThreadedDataServer(server);
//        thread.start();
//        }
        
        Thread sendf = new Thread(() -> {
            while (true) {
                SendFiles sendFiles = new SendFiles(server);
                sendFiles.sendData();
            }
        });

        // receivef thread
        Thread receivef = new Thread(() -> {
            while (true) {
                ReceiveFiles receiveFiles = new ReceiveFiles(server);
                receiveFiles.receiveFiles();
            }
        });

        sendf.start();
        receivef.start();
    }

}

class ThreadedDataServer extends Thread {

    private Thread sendf;
    private Thread receivef;
    private final ServerSocket server;

    ThreadedDataServer(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        // sendf thread
        this.sendf = new Thread(() -> {
            while (true) {
                SendFiles sendFiles = new SendFiles(server);
                sendFiles.sendData();
            }
        });

        // receivef thread
        this.receivef = new Thread(() -> {
            while (true) {
                ReceiveFiles receiveFiles = new ReceiveFiles(server);
                receiveFiles.receiveFiles();
            }
        });

        sendf.start();
        receivef.start();
    }

}

//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package SendReceiveFiles;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author bhushan
// */
//public class DataServer {
//
//    private static ServerSocket server;
//
//    public static void main(String[] args) {
//        try {
//            server = new ServerSocket(8001);
//        } catch (IOException ex) {
//            Logger.getLogger(DataServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        while (true) {
//            try {
//                Socket client = server.accept();
//                System.out.println("Thread connected at: " + client.getInetAddress());
//                Thread thread = new ThreadedDataServer(client);
//                thread.start();
//            } catch (IOException ex) {
//                Logger.getLogger(DataServer.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//}
//
//class ThreadedDataServer extends Thread {
//
//    private Thread sendf;
//    private Thread receivef;
//    private final Socket client;
//
//    ThreadedDataServer(Socket client) {
//        this.client = client;
//    }
//
//    @Override
//    public void run() {
//        // sendf thread
//        this.sendf = new Thread(() -> {
//            while (true) {
//                SendFiles sendFiles = new SendFiles(client);
//                sendFiles.sendData();
//            }
//        });
//
//        // receivef thread
//        this.receivef = new Thread(() -> {
//            while (true) {
//                ReceiveFiles receiveFiles = new ReceiveFiles(client);
//                receiveFiles.receiveFiles();
//            }
//        });
//
//        sendf.start();
//        receivef.start();
//    }
//
//}
