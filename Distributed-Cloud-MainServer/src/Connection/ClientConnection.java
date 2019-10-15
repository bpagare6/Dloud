/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import DatabaseConnectivity.FilesDB;
import DatabaseConnectivity.UsersDB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ClientConnection {

    private ServerSocket server;
    private final String foldername;

    public ClientConnection() {
        foldername = "files/";
        try {
            server = new ServerSocket(8000);
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect_client() {
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = server.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostName() + "...");
                Thread thread = new ThreadedServer(clientSocket, foldername);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.connect_client();
    }

}

class ThreadedServer extends Thread {

    private final Socket clientSocket;
    private final String foldername;
    private UsersDB userDB;
    private FilesDB fileDB;

    ThreadedServer(Socket clientSocket, String foldername) {
        this.clientSocket = clientSocket;
        this.foldername = foldername;
        this.userDB = new UsersDB();
        this.fileDB = new FilesDB();
    }

    @Override
    public void run() {
        try {
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);

            while (true) {
                String request = dis.readUTF();

                switch (request) {
                    case "Validate User":
                        String username = dis.readUTF();
                        String password = dis.readUTF();
                        // validate user
                        dos.writeBoolean(userDB.validate_user(username, password));
                        break;
                    case "Get Files List":
                        List<String> fileList;
                        fileList = fileDB.get_file_list();
                        dos.writeUTF(Integer.toString(fileList.size()));
                        for (String file: fileList) {
                            dos.writeUTF(file);
                        }
                        break;
                    case "Get IP List":
                        List<String> ipList = new ArrayList<>();
                        // Add IPs to list for sending to client
                        ipList.add("192.168.1.102");
                        dos.writeUTF(Integer.toString(ipList.size()));
                        for (String ip: ipList) {
                            dos.writeUTF(ip);
                        }
                        break;
                    case "Add File":
                        String filename = dis.readUTF();
                        String owner = dis.readUTF();
                        fileDB.addFile(filename, owner);
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
