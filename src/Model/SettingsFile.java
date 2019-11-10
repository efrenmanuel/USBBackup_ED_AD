/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author alumno
 */
public class SettingsFile {

    public static void overwriteSettings(HashMap<File, ArrayList<Object>> synchronizedPaths) throws FileNotFoundException, IOException {
        new File("./enabledPaths.cfg").delete();
        RandomAccessFile synchronizedPathsFile = new RandomAccessFile("./enabledPaths.cfg", "rw");
        while (synchronizedPathsFile.skipBytes(100) > 0) {
        }
        for (HashMap.Entry<File,ArrayList<Object>> path: synchronizedPaths.entrySet()){
            addNewPath(path);
        }

        synchronizedPathsFile.close();
    }
    
    public static void addNewPaths(HashMap<File,ArrayList<Object>> pathsToAdd) throws FileNotFoundException, IOException{
        for (HashMap.Entry<File,ArrayList<Object>> path:pathsToAdd.entrySet()){
            addNewPath(path);
        }
    }

    public static void addNewPath(HashMap.Entry<File, ArrayList<Object>> pathToAdd) throws FileNotFoundException, IOException {

        RandomAccessFile synchronizedPathsFile = new RandomAccessFile("./enabledPaths.cfg", "rw");
        while (synchronizedPathsFile.skipBytes(100) > 0) {
        }
        String entry="";
        entry+="\n"+pathToAdd.getKey();
        ArrayList<Object> values=pathToAdd.getValue();
        for (int path=0; path<values.size();path+=4 ) {
            entry+=","+((File)values.get(path)).getCanonicalPath()+",";
            entry+=((boolean)values.get(path+1))+",";
            entry+=((boolean)values.get(path+2))+",";
            entry+=((boolean)values.get(path+3));
        }
        synchronizedPathsFile.writeBytes(entry );
        synchronizedPathsFile.close();
    }

    public static HashMap<File, ArrayList<Object>> getSyncedPaths() throws FileNotFoundException, IOException {
        HashMap<File, ArrayList<Object>> synchronizedPaths = new HashMap<>();
        RandomAccessFile synchronizedPathsFile = new RandomAccessFile("./enabledPaths.cfg", "r");
        String pathPair;
        while ((pathPair = synchronizedPathsFile.readLine()) != null) {
            if (!pathPair.equals("")) {
                String[] paths=pathPair.split(",");
                ArrayList<Object> backups= new ArrayList<>();
                for (int path=1;path<paths.length;path=path+4){
                    backups.add(new File(paths[path]));
                    backups.add(Boolean.valueOf(paths[path+1]));
                    backups.add(Boolean.valueOf(paths[path+2]));
                    backups.add(Boolean.valueOf(paths[path+3]));
                }
                synchronizedPaths.put(new File(paths[0]),backups );
            }
        }
        synchronizedPathsFile.close();
        return synchronizedPaths;

    }

}
