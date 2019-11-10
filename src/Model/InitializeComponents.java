/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alumno
 */
public class InitializeComponents {

    private HashMap<String, String> synchronizedPaths;

    public static void run(HashMap<String, String> synchronizedPaths, JTable mainTable) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
        for (String synchronizedPath : synchronizedPaths.keySet()) {
            ArrayList<Object> entry = new ArrayList<>();
            entry.add(synchronizedPath);

            entry.add(dateFormat.format(new Date(new File(synchronizedPath).lastModified())));
            entry.add(synchronizedPaths.get(synchronizedPath));
            entry.add(dateFormat.format(new Date(new File(synchronizedPaths.get(synchronizedPath)).lastModified())));

            ((DefaultTableModel) mainTable.getModel()).addRow(entry.toArray());
        }
    }

}
