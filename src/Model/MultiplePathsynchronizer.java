/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author alumno
 */
public class MultiplePathsynchronizer extends Thread {

    HashMap<String, String> synchronizedPaths;
    boolean stop = false;
    JProgressBar progressBar;
    
    JLabel messageLabel;

    public MultiplePathsynchronizer(HashMap<String, String> synchronizedPaths, JProgressBar progressBar,JLabel messageLabel) {
        this.synchronizedPaths = synchronizedPaths;
        this.progressBar = progressBar;
        this.messageLabel=messageLabel;
    }

    @Override
    public synchronized void run() {

        for (Map.Entry<String, String> file : synchronizedPaths.entrySet()) {
            String source = file.getKey();
            String destination = file.getValue();
            
            System.out.println(source+"  "+destination);

            FileSynchronizer backingUp = new FileSynchronizer(source, destination, stop, progressBar, messageLabel);
            backingUp.start();
            try {
                backingUp.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiplePathsynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
