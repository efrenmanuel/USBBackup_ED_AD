/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author alumno
 */
public class MultiplePathsynchronizer extends Thread {

    private final HashMap<File, ArrayList<Object>> synchronizedPaths;
    private HashMap<String, FileFilter> filters;
    private boolean stop = false;
    private final JProgressBar progressBar;
    private final JButton stopper;
    private final JLabel messageLabel;
    private FileSynchronizer backingUp = null;

    public MultiplePathsynchronizer(HashMap<File, ArrayList<Object>> synchronizedPaths, JProgressBar progressBar, HashMap<String, FileFilter> filters, JLabel messageLabel, JButton stopper) {
        this.synchronizedPaths = synchronizedPaths;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.stopper = stopper;
        this.filters = filters;
    }

    @Override
    public synchronized void run() {

        for (Map.Entry<File, ArrayList<Object>> origin : synchronizedPaths.entrySet()) {
            File source = origin.getKey();
            for (int destination = 0; destination < origin.getValue().size(); destination += 3) {
                if ((boolean) origin.getValue().get(destination + 1)) {
                    File destinationFile = (File) origin.getValue().get(destination);

                    if (!(boolean) origin.getValue().get(destination + 2)) {
                        filters = null;
                    }
                    boolean deleteOriginal = (boolean) origin.getValue().get(destination + 2);
                    backingUp = new FileSynchronizer(source, destinationFile, progressBar, messageLabel, filters, deleteOriginal);
                    backingUp.start();
                    try {
                        backingUp.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MultiplePathsynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (stop) {
                        SwingUtilities.invokeLater(() -> {
                            messageLabel.setText("Finished synchronizing - Stopped by user.");
                            progressBar.setValue(progressBar.getMaximum());
                        });
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
