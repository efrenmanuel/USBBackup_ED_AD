/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;

/**
 *
 * @author alumno
 */
public class Table_Map_Synchronizer {
    
    private JTable table;
    private HashMap<File,ArrayList<Object>> map;

    public Table_Map_Synchronizer(JTable table, HashMap<File, ArrayList<Object>> map) {
        this.table = table;
        this.map = map;
    }
    
    public void updateMap(){
        map=new HashMap<>();
        for(int row=0;row<table.getModel().getRowCount();row++){
            File source;
            File destination;
            boolean backup;
            boolean clasify;
            boolean deleteSource;
        }
    }
    
}
