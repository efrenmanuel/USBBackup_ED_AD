/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

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
     * @param includeFolders
     * @param filter
     * @return Array of files that are named like so.
     * @throws CustomExceptions
     * @throws IOException
     */
    public static ArrayList<File> recursiveListFiles(File searchRoot, boolean includeFolders, FilenameFilter filter) throws CustomExceptions, IOException {
        ArrayList<File> results = new ArrayList<>();

        if (!searchRoot.isDirectory()) {
            throw new CustomExceptions.NotADirectory(searchRoot.getCanonicalPath());
        }
        File[] content = searchRoot.listFiles(filter);

        if (content != null) {
            for (File file : content) {
                if (!file.isDirectory()) {

                    results.add(file);

                } else {
                    if (includeFolders) {
                        results.add(file);

                    }
                    results.addAll(recursiveListFiles(file, includeFolders));
                }
            }

        }
        return results;
    }

    public static ArrayList<File> recursiveListFiles(File searchRoot) throws CustomExceptions, IOException {
        return recursiveListFiles(searchRoot, true, null);
    }

    public static ArrayList<File> recursiveListFiles(File searchRoot, boolean includeFolders) throws CustomExceptions, IOException {
        return recursiveListFiles(searchRoot, includeFolders, null);
    }

    public static ArrayList<File> recursiveListFiles(File searchRoot, FilenameFilter filter) throws CustomExceptions, IOException {
        return recursiveListFiles(searchRoot, true, filter);
    }

    public static boolean copyFileIfNewer(File input, String output, boolean deleteSource) throws IOException {
        return copyFileIfNewer(input, new File(output), deleteSource);
    }

    public static boolean copyFileIfNewer(String input, String output, boolean deleteSource) throws IOException {
        return copyFileIfNewer(new File(input), new File(output), deleteSource);
    }

    public static boolean copyFileIfNewer(File input, File output, boolean deleteSource) throws IOException {
        if (input.lastModified() > output.lastModified()) {
            return copyFile(input, output, true, deleteSource);
        } else {
            return false;
        }
    }

    public static boolean copyFile(File input, String output) throws IOException {
        return copyFile(input, new File(output));
    }

    public static boolean copyFile(String input, String output) throws IOException {
        return copyFile(new File(input), new File(output));
    }

    public static boolean copyFile(File input, File output) throws IOException {
        return copyFile(input, output, false, false);
    }

    public static boolean copyFile(File input, File output, boolean force, boolean deleteSource) throws IOException {
        boolean result;
        if (!input.isDirectory()) {
            if (!output.exists()) {
                Files.createDirectories(output.toPath().getParent());
            }
            try {
                if (force) {
                    Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    if (deleteSource) {

                    }
                } else {
                    Files.copy(input.toPath(), output.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                }
                result = true;
            } catch (FileAlreadyExistsException ex) {
                result = false;
            }
        } else {
            if (!output.getParentFile().exists()) {
                Files.createDirectories(output.toPath());
            }
            result = true;

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

    public static ArrayList<File> findDuplicates(String root) throws IOException, CustomExceptions {
        return findDuplicates(root, null);
    }

    public static ArrayList<File> findDuplicates(File root) throws IOException, CustomExceptions {
        return findDuplicates(root, null);
    }

    public static ArrayList<File> findDuplicates(String root, File original) throws IOException, CustomExceptions {
        return findDuplicates(new File(root), original);
    }

    public static ArrayList<File> findDuplicates(File root, File original) throws IOException, CustomExceptions {

        if (!root.isDirectory()) {
            throw new CustomExceptions.TheDirectoryDoesntExists(root.getCanonicalPath());
        }

        ArrayList<File> allFiles = recursiveListFiles(root, false);
        allFiles.sort(new Comparator() {
            @Override
            public int compare(Object filein1, Object filein2) {
                File file1 = (File) filein1;
                File file2 = (File) filein2;

                switch (Long.compare(file1.length(), file2.length())) {
                    case 0:
                        if (file1.getName().equals(file2.getName())) {
                            return 0;
                        } else {
                            return file1.getName().compareTo(file2.getName());
                        }
                    default:
                        return file1.getName().compareTo(file2.getName());

                }

            }
        });

        ArrayList<File> duplicated = new ArrayList<>();
        if (original == null) {

            for (int file = 0; file < allFiles.size(); file++) {
                int comparingWith = file + 1;
                while (comparingWith < allFiles.size() && Arrays.equals(Files.readAllBytes(allFiles.get(comparingWith).toPath()), Files.readAllBytes(allFiles.get(file).toPath()))) {
                    if (!duplicated.contains(allFiles.get(file))) {
                        duplicated.add(allFiles.get(file));
                    }

                    duplicated.add(allFiles.get(comparingWith));
                    file++;
                    comparingWith++;
                }
            }
            return duplicated;
        } else {
            if (original.isDirectory()) {
                throw new CustomExceptions.ItsADirectory(root.getCanonicalPath());
            }

            for (File file : recursiveListFiles(root)) {
                if (compareFiles(file, original)) {

                    duplicated.add(file);

                }
            }
            return duplicated;
        }

    }

    public static boolean compareFiles(File file1, File file2) {
        return (file1.getName().equals(file2.getName()) && file1.length() == file2.length());
    }

    public static long getFreeSpace(String root) {
        return getFreeSpace(new File(root));
    }

    public static long getFreeSpace(File root) {
        return root.getFreeSpace();
    }
}
