    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendReceiveFiles;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhushan
 */
public class SendFiles {

    private final Socket client;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    
    public SendFiles(Socket client, DataInputStream dis, DataOutputStream dos) {
        this.client = client;
        this.dos = dos;
        this.dis = dis;
    }

    public void sendData() {
        System.out.println("Sending data");
        long startTime = System.currentTimeMillis();
        try {
            BufferedInputStream bis;
            FileInputStream file;
            String filename = dis.readUTF();
            System.out.println("Filename: " + filename);
            OutputStream out = this.client.getOutputStream();
            int startCounter = 0, endCounter = 0;
            File folder = new File("Files/");
            List<String> filelist = new ArrayList<>();
            List<Integer> fileIndexes = new ArrayList<>();

            for (File ffile : folder.listFiles()) {
                String f = ffile.getName();
                if (f.startsWith("." + filename)) {
                    filelist.add(f);
                }
            }

            for (String ffile : filelist) {
                String[] filenameParts = ffile.split("\\.");
                for (String part : filenameParts) {
                }
                fileIndexes.add(Integer.parseInt(filenameParts[filenameParts.length - 2]));
            }

            try {
                endCounter = Collections.max(fileIndexes) + 1;
                startCounter = Collections.min(fileIndexes);
            } catch (Exception e) {
                System.out.println(e);
            }

            dos.writeInt(startCounter);
            dos.writeInt(endCounter);
            for (int i = startCounter; i < endCounter; i++) {
                String fname = "Files/." + filename + "." + i + ".splitPart";
                file = new FileInputStream(fname);
                bis = new BufferedInputStream(file);
                dos.writeInt((int) file.getChannel().size());
                sendBytes(bis, out, (int) file.getChannel().size());
            }
        } catch (IOException ex) {
            Logger.getLogger(SendFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SendFiles.class.getName()).log(Level.SEVERE, null, ex);
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
