/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CreateConnections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class ConnectionEntry {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ConnectionEntry(Socket socket) {
        this.socket = socket;
        try {
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
            this.dis = new DataInputStream(this.is);
            this.dos = new DataOutputStream(this.os);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return is;
    }

    public OutputStream getOutputStream() {
        return os;
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

}
