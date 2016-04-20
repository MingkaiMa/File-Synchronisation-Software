package core;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 * Helper Class to perform file (or folder) operations.
 */
public class FileOperator {

    /**
     * Create a file (or folder) and copy the content from given source file (or folder)
     * After the operation, last modified times of two files are identical.
     * @param pathSource full path of the given file (or folder)
     * @param pathTarget full path of the file (or folder) which needs to be created
     * @param sourceFile the given source file (or folder)
     */
    public static void create(String pathSource, String pathTarget, File sourceFile) {
        // Obtain the name of target file (extracted from full path and directory path of source file)
        int length = pathSource.length();
        String fileName = sourceFile.toString().substring(length);

        // Do the create operation
        File targetFile = new File(pathTarget + fileName);
        if (!targetFile.exists()) {
            if (sourceFile.isDirectory()) {
                try {
                    FileUtils.copyDirectory(sourceFile, targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FileUtils.copyFile(sourceFile, targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Copy the content from a given source file (or folder) to another file (or folder).
     * After the operation, last modified times of two files are identical
     * @param pathSource full path of Source File (or folder)
     * @param pathTarget full path of Target File (or folder)
     * @param sourceFile file whose content will be copied from
     * @param targetFile file which will be created and copied content from source file
     */
    public static void copy(String pathSource, String pathTarget, File sourceFile, File targetFile) {
        delete(targetFile);
        create(pathSource, pathTarget, sourceFile);
    }

    /**
     * Delete a given file, if it is a folder, then delete this folder and every files and subfolder inside
     * @param file the given file or folder
     */
    public static void delete(File file) {
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
