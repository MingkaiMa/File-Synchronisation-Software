package userinterface;

import core.ActionList;
import core.FileList;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;


public class FileTableModel extends AbstractTableModel {

    private FileList sourceList;
    private FileList targetList;
    private ActionList actionList;
    private Boolean[] checkList;

    private int lengthOfPathSource;
    private int lengthOfPathTarget;

    private final String[] COLUMN_NAME = {"Name(Source)", "Size(B)", "Last modified", "Action",
            "Enable action", "Name(Target)", "Size(B)", "Last modified"};

    private Class[] columnClasses = new Class[]{
            String.class, Long.class, SimpleDateFormat.class, String.class,
            Boolean.class, String.class, Long.class, SimpleDateFormat.class
    };

    /**
     * Initialize the Table model
     */
    public FileTableModel() {
        this.checkList = new Boolean[1000];
        // Initialize all the values of checkList to false
        for (int i = 0; i < 1000; i++) {
            this.checkList[i] = true;
        }
    }

    /**
     * Refresh table content.
     * Called when any changes is applied to source or target directory.
     * @param sourceList FileList stores files and folders in source directory
     * @param targetList FileList stores files and folders in target directory
     * @param actionList List of actions should be perform for each pair of source file and target file
     */
    public void refreshTableModel(FileList sourceList, FileList targetList, ActionList actionList) {
        this.sourceList = sourceList;
        this.targetList = targetList;
        this.actionList = actionList;
        lengthOfPathSource = sourceList.getLengthOfPath();
        lengthOfPathTarget = targetList.getLengthOfPath();
        for (int i = 0; i < 1000; i++) {
            this.checkList[i] = true;
        }
        this.fireTableDataChanged();
    }

    public int getColumnCount() {
        return 8;
    }  // A constant for this model

    public int getRowCount() {
        return targetList.size();
    }  // # of files in dir


    public String getColumnName(int col) {
        return COLUMN_NAME[col];
    }


    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        // Only allow user edit cell's value at column 4 (checkbox for enable or disable the action)
        if (col == 4)
            return true;
        else return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (checkList[rowIndex])
            checkList[rowIndex] = false;
        else checkList[rowIndex] = true;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                if (sourceList.get(row) != null)
                    return sourceList.get(row).toString().substring(lengthOfPathSource + 1);
                else return "";
            case 1:
                if (sourceList.get(row) != null) {
                    if (!sourceList.get(row).isDirectory())
                        return new Long((sourceList.get(row).length()));
                    else return "<Folder>";
                } else return "";

            case 2:
                SimpleDateFormat sourceDate = new SimpleDateFormat("HH:mm:ss");
                if (sourceList.get(row) != null)
                    return sourceDate.format(sourceList.get(row).lastModified());
                else return "";

            case 3:
                if (actionList.get(row) != null)
                    return actionList.get(row);
                else return "";

            case 4:
                return checkList[row];

            case 5:
                if (targetList.get(row) != null)
                    return targetList.get(row).toString().substring(lengthOfPathTarget + 1);
                else return "";

            case 6:
                if (targetList.get(row) != null) {
                    if (!targetList.get(row).isDirectory())
                        return new Long((targetList.get(row).length()));
                    else return "<Folder>";
                } else return "";

            case 7:
                SimpleDateFormat targetDate = new SimpleDateFormat("HH:mm:ss");
                if (targetList.get(row) != null)
                    return targetDate.format(targetList.get(row).lastModified());
                else return "";

            default:
                return null;
        }
    }

}

