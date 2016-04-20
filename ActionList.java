package core;

import java.io.File;
import java.util.ArrayList;

/**
 * Store one of the possible Synchronization Action ("Create", "Update", "Restore" or "Delete") as element.
 * Used with one Source FileList and one Target FileList to perform Synchronization Task
 */
public class ActionList extends ArrayList<String>{
    private ArrayList<String> list;
    private FileList sourceList;
    private FileList targetList;
    private File source;
    private File target;
    private int count;

    public ActionList(FileList sourceList, FileList targetList) {
        this.sourceList = sourceList;
        this.targetList = targetList;
        count = 0;

        // The Action List has to have the same size as Source and Target FileList.
        list = new ArrayList<>(sourceList.size());

        // Looping through the Action List and add values to elements
        for (int i = 0; i < sourceList.size(); i++){
            source = sourceList.get(i);
            target = targetList.get(i);
            list.add(setAction(source,target));
            count++;
        }
    }

    /**
     * Determine the which of the four possible sync actions should be performed based on two given Files
     * @param source: File in source directory
     * @param target: File in target directory
     * @return synchronization action
     */
    private String setAction(File source, File target) {
        if(source == null) return "DELETE";
        else if(target == null) return "CREATE";
        else {
            if (source.lastModified() < target.lastModified()) return "RESTORE";
            else if (source.lastModified() > target.lastModified()) return "UPDATE";
            else return "NONE";
        }
    }

    public String get(int index) {
        return list.get(index);
    }

    public int size(){
        return count;
    }

    @Override
    public String toString() {
        return "list=" + list +
                ", sourceList=" + sourceList +
                ", targetList=" + targetList +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
