/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.MainScreen;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author efren
 */
public class InitializeTable {

    public static void initialize(JTable directoryTable, HashMap<File, ArrayList<Object>> synchronizedPaths) throws IOException {
        directoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        directoryTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        directoryTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        directoryTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        directoryTable.getColumnModel().getColumn(4).setPreferredWidth(1);
        directoryTable.getColumnModel().getColumn(5).setPreferredWidth(1);
        directoryTable.getColumnModel().getColumn(6).setPreferredWidth(1);
        for (Map.Entry<File, ArrayList<Object>> entry : synchronizedPaths.entrySet()) {
            Object[] tableEntry = new Object[7];
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
            tableEntry[0] = entry.getKey().getCanonicalPath();
            tableEntry[1] = (entry.getKey().exists()) ? sdf.format(new Date(entry.getKey().lastModified())) : "Never";
            tableEntry[2] = ((File) entry.getValue().get(0)).getCanonicalPath();
            tableEntry[3] = (((File) entry.getValue().get(0)).exists()) ? sdf.format(new Date(((File) entry.getValue().get(0)).lastModified())) : "Never";
            tableEntry[4] = (boolean) entry.getValue().get(1);
            tableEntry[5] = (boolean) entry.getValue().get(2);
            tableEntry[6] = (boolean) entry.getValue().get(3);
           

            ((DefaultTableModel) directoryTable.getModel()).addRow(tableEntry);
            
        }
    }

    public static void update(JTable directoryTable, HashMap<File, ArrayList<Object>> synchronizedPaths) throws IOException {
        ((DefaultTableModel) directoryTable.getModel()).setRowCount(0);
        for (Map.Entry< File, ArrayList<Object>> entry : synchronizedPaths.entrySet()) {
            Object[] tableEntry = new Object[7];
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
            tableEntry[0] = entry.getKey().getCanonicalPath();
            tableEntry[1] = sdf.format(new Date(entry.getKey().lastModified()));
            tableEntry[2] = ((File) entry.getValue().get(0)).getCanonicalPath();
            tableEntry[3] = sdf.format(new Date(((File) entry.getValue().get(0)).lastModified()));
            tableEntry[4] = ((boolean) entry.getValue().get(1));
            tableEntry[5] = ((boolean) entry.getValue().get(2));
            tableEntry[6] = ((boolean) entry.getValue().get(3));

            ((DefaultTableModel) directoryTable.getModel()).addRow(tableEntry);
        }

    }

}
