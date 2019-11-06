/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author alumno
 */
public class FileSynchronizer extends Thread {

    private File localSource;
    private File backupRoute;
    private boolean stop = false;
    private JProgressBar progressBar;
    private JLabel messageLabel;
    private boolean clasify;
    private boolean deleteOriginal;

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.clasify = false;
        this.deleteOriginal=false;
    }

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel, boolean clasify, boolean deleteOriginal) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.clasify = clasify;
        this.deleteOriginal = this.deleteOriginal;
    }

    @Override
    public void run() {
        try {

            ArrayList<File> allFiles = FileOperations.recursiveListFiles(localSource);

            progressBar.setMinimum(0);

            float totalFileSize = 0;
            float currentFileSize = 0;

            for (File file : allFiles) {
                totalFileSize += file.length();
            }
            progressBar.setMaximum((int) totalFileSize);

            for (File file : allFiles) {
                messageLabel.setText("Synchronizing - " + ((file.getCanonicalPath().length() > 50) ? ("..." + (file.getCanonicalPath().substring(file.getCanonicalPath().length() - 50))) : file.getCanonicalPath()));

                FileOperations.copyFileIfNewer(file, backupRoute.getCanonicalPath() + file.getCanonicalPath().replace(localSource.getCanonicalPath(), ""));
                if (stop) {
                    break;
                }

                progressBar.setValue(progressBar.getValue() + (int) file.length());
            }
            messageLabel.setText("Finished synchronizing - " + localSource);
        } catch (IOException ex) {
            Logger.getLogger(MultiplePathsynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomExceptions ex) {
            Logger.getLogger(FileSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setStop(boolean stop) {

        this.stop = stop;
    }

}
