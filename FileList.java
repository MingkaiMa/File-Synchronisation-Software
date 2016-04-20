package core;

import java.io.File;
import java.util.ArrayList;

/**
 * FileList is an extension of ArrayList, which stores every file and folder in a given directory as its elements
 */
public class FileList extends ArrayList<File> {
    private ArrayList<File> list;
    private int count;

    // The length of directory path, for example "/user" has length of 5,
    // used to extract the name of file for further file name comparison.
    private int lengthOfPath;

    public FileList(File d) {
        list = new ArrayList<>();
        lengthOfPath = d.toString().length();
    }

    /**
     * List all files and subfolders inside a given directory.
     * This uses recursive method to go inside a subfolder and list all of its children.
     * @param dir: directory or file
     */
    public void listAllFile(File dir) {
        // List all files and folders in the given directory
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                    count++;
                    listAllFile(file);
                } else if (file.isFile()) {
                    list.add(file);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort this FileList based on a reference FileList. The method with loop for all files in reference List:
     * Suppose a file at index i in reference List has the name "abc", if in our list has a file with the same name,
     * then it must be stored in the list with index i too, otherwise ourList[i] must be null.
     * On the other hand, extend our list and file in our list which does not have matching name in reference List
     * will be push toward the end of our list.
     * @param referenceList : reference FileList, use too sort our list
     */
    public void compareList(FileList referenceList) {
        // Adding null values top of target list
        for (int i = 0; i < referenceList.size(); i++) {
            list.add(0, null);
            count++;
        }

        // Now move the correct value to replace null
        for (int i = 0; i < referenceList.size(); i++) {
            for (int j = referenceList.size(); j < list.size(); j++) {
                // get the name of the file in list (extracted from full Path)
                String s1 = referenceList.get(i).toString().substring(referenceList.lengthOfPath);
                // get the name of the file in referenceList (extracted from full Path)
                String s2 = list.get(j).toString().substring(lengthOfPath);
                // I do not know why comparison s1==s2 always return false;
                // Therefore I used hashCode to compare 2 strings instead
                if (s1.hashCode()==s2.hashCode()) {
                    list.set(i, list.get(j));
                    list.remove(j);
                    count--;
                }
            }
        }

    }

    /**
     * Extend the size of this list based on the size of reference List, by adding null value to the end.
     * @param referenceList: reference FileList, its size is not less than the size of our FileList
     */
    public void extendList(FileList referenceList) {
        if (referenceList != null) {
            for (int i = size(); i < referenceList.size(); i++) {
                list.add(null);
                count++;
            }
        }
    }

    public File get(int index) {
        return list.get(index);
    }

    public void setElement(int index, File f){
        list.set(index, f);
    }

    public int size(){
        return count;
    }

    /**
     * get Length of Directory (excluded the file name)
     * @return length in Integer
     */
    public int getLengthOfPath() {
        return lengthOfPath;
    }

    @Override
    public String toString() {
        return "list{" +
                "list=" + list +
                '}';
    }
}
