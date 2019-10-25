/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author alumno
 */
public class CustomExceptions extends Exception {

    public CustomExceptions(String string) {
        super(string);
    }

    public static class EmptyFolder extends CustomExceptions {

        public EmptyFolder(String dir) {
            super("This directory is empty: " + dir);
        }

    }

    public static class ItsADirectory extends CustomExceptions {

        public ItsADirectory(String dir) {
            super("Cannot read the file as it is a directory: " + dir);
        }
    }

    public static class NotADirectory extends CustomExceptions {

        public NotADirectory(String dir) {
            super("That route is not a directory: " + dir);
        }

    }

    public static class NoExisteElDirectorio extends CustomExceptions {

        public NoExisteElDirectorio(String dir) {
            super("El directorio no existe: " + dir);
        }

    }

    public static class TheDirectoryAlreadyExists extends CustomExceptions {

        public TheDirectoryAlreadyExists(String dir) {
            super("The directory already exists: " + dir);
        }

    }
}
