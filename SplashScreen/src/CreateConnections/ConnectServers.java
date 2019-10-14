/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CreateConnections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ConnectServers {

    private List<ConnectionEntry> ipListConnected;

    public List<ConnectionEntry> connectSideServers(DataOutputStream dos, DataInputStream dis) {

        ipListConnected = new ArrayList<>();
        
        List<String> ipList = new ArrayList<>();;
        try {
            dos.writeUTF("Get IP List");
            // Get the ip list from server
            int size = Integer.parseInt(dis.readUTF());
            for (int i = 0; i < size; i++) {
                ipList.add((String) dis.readUTF());
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectServers.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Connect to as many servers as possible
        ipList.stream().forEach((String ip) -> {
            try {
                Socket socket = new Socket(ip, 8001);
                ConnectionEntry connectionEntry = new ConnectionEntry(socket);
                ipListConnected.add(connectionEntry);
            } catch (IOException ex) {
                Logger.getLogger(ConnectServers.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        return ipListConnected;

    }

}
