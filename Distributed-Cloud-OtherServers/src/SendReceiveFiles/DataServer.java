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
            Logger.getLogger(SendFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

        // sendf thread 
        Thread sendf = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SendFiles sendFiles = new SendFiles(server);
                    sendFiles.sendData();
                }
            }
        });

        // receivef thread
        Thread receivef = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ReceiveFiles receiveFiles = new ReceiveFiles(server);
                    receiveFiles.receiveFiles();
                }
            }
        });

        sendf.start();
        receivef.start();
    }

}
