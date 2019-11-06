/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author alumno
 */
public class MultiplePathsynchronizer extends Thread {

    HashMap<File, ArrayList<Object>> synchronizedPaths;
    boolean stop = false;
    JProgressBar progressBar;
    JButton stopper;
    JLabel messageLabel;
    FileSynchronizer backingUp = null;

    public MultiplePathsynchronizer(HashMap<File, ArrayList<Object>> synchronizedPaths, JProgressBar progressBar, JLabel messageLabel, JButton stopper) {
        this.synchronizedPaths = synchronizedPaths;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.stopper = stopper;
    }

    @Override
    public synchronized void run() {

        for (Map.Entry<File, ArrayList<Object>> origin : synchronizedPaths.entrySet()) {
            File source = origin.getKey();
            for (int destination = 0; destination < origin.getValue().size(); destination += 3) {
                if ((boolean) origin.getValue().get(destination + 1)) {
                    File destinationFile = (File) origin.getValue().get(destination);

                    boolean clasify = (boolean) origin.getValue().get(destination + 2);
                    boolean deleteOriginal = (boolean) origin.getValue().get(destination + 2);
                    backingUp = new FileSynchronizer(source, destinationFile, progressBar, messageLabel, clasify, deleteOriginal);
                    backingUp.start();
                    try {
                        backingUp.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MultiplePathsynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (stop) {
                        messageLabel.setText("Finished synchronizing - Stopped by user.");
                        progressBar.setValue(progressBar.getMaximum());
                        break;
                    }
                }
            }

        }
        stopper.setEnabled(false);
    }

    public void setStop(boolean stop) {
        backingUp.setStop(true);
        this.stop = stop;
    }

}
