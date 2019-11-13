/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

/**
 *
 * @author andre
 */
public class Filters {

    public static class FilterDirectory implements FilenameFilter{

        @Override
        public boolean accept(File dir, String name) {
            return new File(dir, name).isDirectory();

        }
    }

    public static class FilterMinSize implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return new File(dir, name).length() > 1024;

        }
    }

    public static class FilterLastDay implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            double Date = 0;
            return new File(dir, name).lastModified() > new Date().getTime() - 1000 * 60 * 60 * 24;

        }
    }

    public static class FilterDocument implements FilenameFilter {

        @Override
        public boolean accept(File file, String name) {
            return name.endsWith(".doc") || name.endsWith(".txt") || name.endsWith(".docx") || name.endsWith(".pdf")||file.isDirectory();
        }

    }

    public static class FilterImage implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {

            return name.endsWith(".gif") || name.endsWith(".jpg") || name.endsWith(".tiff") || name.endsWith(".png")||dir.isDirectory();
        }
    }

    public static class FilterVideo implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".avi") || name.endsWith(".mp4") || name.endsWith(".mkv")||dir.isDirectory();

        }

    }

    public static class FilterOthers implements FilenameFilter {

        @Override
        public boolean accept(File file, String name) {
            return !(new FilterDocument().accept(file, name) || new FilterVideo().accept(file, name) || new FilterImage().accept(file, name))||file.isDirectory();
        }

    }

}
