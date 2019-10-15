/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CreateConnections;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ConnectMainServer {
    
    private Socket client;
    
    public void connect() {
        try {
            client = new Socket("localhost", 8000);
        } catch (IOException ex) {
            Logger.getLogger(ConnectMainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectMainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getClient() {
        return client;
    }
    
}
