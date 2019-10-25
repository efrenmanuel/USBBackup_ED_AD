/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author alumno
 */
public class FileOperations {

    /**
     * Returns an array of files that are named like the string passed.
     *
     * @param uri Root of the directory we are searching in.
     * @param searchTerm Name of the file to find.
     * @return Array of files that are named like so.
     * @throws CustomExceptions
     * @throws IOException
     */
    public static ArrayList<File> findFiles(String uri, String searchTerm) throws CustomExceptions, IOException {
        ArrayList<File> results = new ArrayList<>();

        uri = (uri.equals("")) ? File.listRoots()[0].toPath().toString() : uri;

        File searchRoot = new File(uri);
        if (!searchRoot.isDirectory()) {
            throw new CustomExceptions.NotADirectory(uri);
        }
        File[] content = searchRoot.listFiles();

        if (content != null) {
            for (File file : content) {
                if (!file.isDirectory() && file.getName().equalsIgnoreCase(searchTerm)) {
                    results.add(file);

                } else {
                    results.addAll(findFiles(file.getCanonicalPath(), searchTerm));
                }
            }

            if (results.isEmpty()) {
                throw new CustomExceptions.EmptyFolder(searchRoot.getAbsolutePath());
            }
        }
        return results;
    }

    /**
     * Returns an array of files in the directory
     *
     * @param searchRoot Root of the directory we are searching in.
     * @return Array of files that are named like so.
     * @throws CustomExceptions
     * @throws IOException
     */
    public static ArrayList<File> recursiveListFiles(File searchRoot) throws CustomExceptions, IOException {
        ArrayList<File> results = new ArrayList<>();

        if (!searchRoot.isDirectory()) {
            throw new CustomExceptions.NotADirectory(searchRoot.getCanonicalPath());
        }
        File[] content = searchRoot.listFiles();

        if (content != null) {
            for (File file : content) {
                if (!file.isDirectory()) {
                    results.add(file);
                } else {
                    results.add(file);
                    results.addAll(recursiveListFiles(file.getCanonicalPath()));
                }
            }

        }
        return results;
    }

    public static boolean copyFileIfNewer(File input, String output) throws IOException {
        return copyFileIfNewer(input, new File(output));
    }

    public static boolean copyFileIfNewer(String input, String output) throws IOException {
        return copyFileIfNewer(new File(input), new File(output));
    }

    public static boolean copyFileIfNewer(File input, File output) throws IOException {
        return copyFile(input, output, input.lastModified() > output.lastModified());
    }

    public static boolean copyFile(File input, String output) throws IOException {
        return copyFile(input, new File(output));
    }

    public static boolean copyFile(String input, String output) throws IOException {
        return copyFile(new File(input), new File(output));
    }

    public static boolean copyFile(File input, File output) throws IOException {
        return copyFile(input, output, false);
    }

    public static boolean copyFile(File input, File output, boolean force) throws IOException {
        boolean result;
        if (!output.exists()){
            Files.createDirectories(output.toPath().getParent());
        }
        try {
            if (force) {
                Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(input.toPath(), output.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
            }
            result = true;
        } catch (FileAlreadyExistsException ex) {
            result = false;
        }
        return result;

    }

    /**
     * Returns an array of files in the directory
     *
     * @param uri Root of the directory we are searching in.
     * @return Array of files that are named like so.
     * @throws CustomExceptions
     * @throws IOException
     */
    public static ArrayList<File> recursiveListFiles(String uri) throws CustomExceptions, IOException {
        uri = (uri.equals("")) ? File.listRoots()[0].toPath().toString() : uri;
        File searchRoot = new File(uri);
        return recursiveListFiles(searchRoot);
    }

}
