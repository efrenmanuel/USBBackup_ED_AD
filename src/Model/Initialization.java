/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author efren
 */
public class Initialization {

    public static void initialize(JTable directoryTable, HashMap<File, File> synchronizedPaths) throws IOException {
        directoryTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        directoryTable.getColumnModel().getColumn(1).setPreferredWidth(1);
        directoryTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        directoryTable.getColumnModel().getColumn(3).setPreferredWidth(1);
         for (Map.Entry<File, File> entry :synchronizedPaths.entrySet()){
             String[] tableEntry=new String[4];
             SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy - hh:mm");
             tableEntry[0]=entry.getKey().getCanonicalPath();
             tableEntry[1]=sdf.format(new Date(entry.getKey().lastModified()));
             tableEntry[2]=entry.getValue().getCanonicalPath();
             tableEntry[3]=sdf.format(new Date(entry.getValue().lastModified()));
             
             ((DefaultTableModel)directoryTable.getModel()).addRow(tableEntry);
         }
    }

}
