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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author alumno
 */
public class FileSynchronizer extends Thread {

    private File localSource;
    private File backupRoute;
    private boolean stop = false;
    private final JProgressBar progressBar;
    private final JLabel messageLabel;
    private boolean deleteOriginal;
    private HashMap<String, FileFilter> filters;

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.deleteOriginal = false;
    }

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel, HashMap<String, FileFilter> filters) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.filters = filters;
        this.deleteOriginal = false;
    }

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel, boolean deleteOriginal) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.filters = null;
        this.deleteOriginal = deleteOriginal;
    }

    public FileSynchronizer(File localSource, File backupRoute, JProgressBar progressBar, JLabel messageLabel, HashMap<String, FileFilter> filters, boolean deleteOriginal) {
        this.localSource = localSource;
        this.backupRoute = backupRoute;
        this.progressBar = progressBar;
        this.messageLabel = messageLabel;
        this.filters = filters;
        this.deleteOriginal = deleteOriginal;
    }

    public void copy() throws CustomExceptions, IOException {
        copy(null, null);
    }

    public void copy(FileFilter filter, String folder) throws CustomExceptions, IOException {
        ArrayList<File> allFiles = FileOperations.recursiveListFiles(localSource, filter);

        float totalFileSize = 0;
        float currentFileSize = 0;

        for (File file : allFiles) {
            totalFileSize += file.length();
        }
        final float total = totalFileSize;

        SwingUtilities.invokeLater(() -> {

            progressBar.setMaximum((int) total);
            progressBar.setMinimum(0);
        });

        for (File file : allFiles) {

            SwingUtilities.invokeLater(() -> {
                try {
                    messageLabel.setText("Synchronizing - " + ((file.getCanonicalPath().length() > 50) ? ("..." + (file.getCanonicalPath().substring(file.getCanonicalPath().length() - 50))) : file.getCanonicalPath()));
                } catch (IOException ex) {
                    Logger.getLogger(FileSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            File output;
            if (folder != null) {
                output = new File(backupRoute, folder + File.separator + file.getName());
            } else {
                output = new File(backupRoute.getCanonicalPath() + file.getCanonicalPath().replace(localSource.getCanonicalPath(), ""));
            }

            FileOperations.copyFileIfNewer(file, output);

            if (stop) {
                break;
            }

            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(progressBar.getValue() + (int) file.length());
            });
        }
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText("Finished synchronizing - " + localSource);
        });

    }

    @Override
    public void run() {
        try {
            for (String folder : filters.keySet()) {
                copy(filters.get(folder), folder);
            }
        } catch (IOException ex) {
            Logger.getLogger(MultiplePathsynchronizer.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (CustomExceptions ex) {
            Logger.getLogger(FileSynchronizer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setStop(boolean stop) {

        this.stop = stop;
    }

}
