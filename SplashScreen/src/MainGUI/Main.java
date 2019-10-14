/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainGUI;

import CreateConnections.ConnectMainServer;

/**
 *
 * @author bhushan
 */
public class Main {

    private static ConnectMainServer connectMainServer;

    public static void main(String[] args) {
        connectMainServer = new ConnectMainServer();
        connectMainServer.connect();
        
        // Show splash screen
        Splash splash = new Splash();
        splash.show_splash();
        
        // Show login screen
        Login login = new Login();
        login.show_login(connectMainServer.getClient());
        
//        connectMainServer.close();
    }

}
