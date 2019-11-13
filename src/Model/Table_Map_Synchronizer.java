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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author alumno
 */
public class Table_Map_Synchronizer {

    private JTable table;
    private HashMap<File, ArrayList<Object>> map;

    public Table_Map_Synchronizer(JTable table, HashMap<File, ArrayList<Object>> map) {
        this.table = table;
        this.map = map;

    }

    public void updateMap() {
        //map;
        for (int row = 0; row < table.getModel().getRowCount(); row++) {
            File source = new File((String) table.getValueAt(row, 0));
            File destination = new File((String) table.getValueAt(row, 2));;
            boolean backup = (boolean) table.getValueAt(row, 4);
            boolean clasify = (boolean) table.getValueAt(row, 5);
            boolean deleteSource = (boolean) table.getValueAt(row, 6);

            if (map.containsKey(source)) {
                ArrayList<Object> backups = map.get(source);
                for (int path = 0; path < backups.size(); path += 4) {
                    if (((File) backups.get(path)).equals(destination)) {
                       
                        backups.set(path + 1, backup);
                        backups.set(path + 2, clasify);
                        backups.set(path + 3, deleteSource);
                        break;
                    }
                }
            } else {
                ArrayList<Object> backups = new ArrayList<>();
                backups.add(destination);
                backups.add(backup);
                backups.add(clasify);
                backups.add(deleteSource);
                map.put(source, backups);
            }

        }
    }

}
