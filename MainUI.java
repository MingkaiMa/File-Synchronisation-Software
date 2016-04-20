package userinterface;

import core.ActionList;
import core.FileList;
import core.FileOperator;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * Main class of the application
 */
public class MainUI {

    private JFrame mainFrame;

    private JPanel pnlSouth;
    private JPanel pnlWest;
    private JPanel pnlEast;
    private JPanel pnlCenter;
    private JPanel pnlNorth;
    private JPanel pnlNorthEast;
    private JPanel pnlNorthWest;
    private JPanel pnlNorthCenter;

    private JButton btnSync;
    private JButton btnSwap;
    private JButton btnBrowseSource;
    private JButton btnBrowseTarget;
    private JButton btnRefresh;

    private JLabel lblSource;
    private JLabel lblTarget;

    // Display the paths of the source and target directories which are selected
    private JTextField txtSource;
    private JTextField txtTarget;

    // Visualize the contents of source and target directories
    private JTable tbl;
    private FileTableModel tblModel;

    private JScrollPane scroll;

    private Integer[] BUTTON_SYNC_SIZE = {200, 40};

    private String sourcePath;
    private String targetPath;

    private File sourceDirectory;
    private File targetDirectory;
    private FileList sourceList;
    private FileList targetList;
    private ActionList actionList;
    private Boolean[] enableList;

    private File initFile;

    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainUI myGUI = new MainUI();
                    myGUI.mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application
     */
    public MainUI() {
        // Create the user configuration file if it does not exist
        try {
            initFile = new File("src//data//init.txt");
            if (!initFile.exists())
                initFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createGUI();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createGUI() {

        // Overall frame and layout
        mainFrame = new JFrame("File Synchronization");
        mainFrame.setMinimumSize(new Dimension(900, 500));
        mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // The South part of the frame is used for button Sync
        pnlSouth = new JPanel();
        mainFrame.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setPreferredSize(new Dimension(900, 70));

        btnSync = new JButton("Sync");
        btnSync.setFont(btnSync.getFont().deriveFont(Font.BOLD));
        btnSync.setToolTipText("Synchronize the contents of two directories");
        btnSync.setPreferredSize(new Dimension(BUTTON_SYNC_SIZE[0], BUTTON_SYNC_SIZE[1]));

        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(btnRefresh.getFont().deriveFont(Font.BOLD));
        btnRefresh.setToolTipText("Refresh the content of the table");
        btnRefresh.setPreferredSize(new Dimension(BUTTON_SYNC_SIZE[0], BUTTON_SYNC_SIZE[1]));

        pnlSouth.add(btnSync);
        pnlSouth.add(btnRefresh);


        // The West part of the frame works like the gap between table and edge of frame
        pnlWest = new JPanel();
        mainFrame.getContentPane().add(pnlWest, BorderLayout.WEST);
        pnlWest.setPreferredSize(new Dimension(20, 300));


        // The East part of the frame works like the gap between table and edge of frame
        pnlEast = new JPanel();
        mainFrame.getContentPane().add(pnlEast, BorderLayout.EAST);
        pnlEast.setPreferredSize(new Dimension(20, 300));


        // The Center area is a table for visualizing content of the source directory
        pnlCenter = new JPanel();
        mainFrame.getContentPane().add(pnlCenter, BorderLayout.CENTER);
        pnlCenter.setLayout(new BorderLayout(0, 0));

        // put the table in Scroll Pane later
        scroll = new JScrollPane();
        pnlCenter.add(scroll, BorderLayout.CENTER);
        // create a Table Model in order to implement the method Action Listener for Table Model
        tblModel = new FileTableModel();


        // North part is for displaying directory's path and browse, swap buttons
        pnlNorth = new JPanel();
        mainFrame.getContentPane().add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.setLayout(new BorderLayout());


        // North West is for Source directory
        pnlNorthWest = new JPanel();
        pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
        pnlNorthWest.setPreferredSize(new Dimension(400, 70));
        pnlNorthWest.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 20));

        lblSource = new JLabel("Source");
        txtSource = new JTextField(17);
        btnBrowseSource = new JButton("Browse");
        btnBrowseSource.setToolTipText("Choose and visualize the content of source directory");

        pnlNorthWest.add(lblSource);
        pnlNorthWest.add(txtSource);
        pnlNorthWest.add(btnBrowseSource);


        // North East is for Target directory
        pnlNorthEast = new JPanel();
        pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
        pnlNorthEast.setPreferredSize(new Dimension(400, 70));
        pnlNorthEast.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 20));

        lblTarget = new JLabel("Target");
        txtTarget = new JTextField(17);
        btnBrowseTarget = new JButton("Browse");
        btnBrowseTarget.setToolTipText("Choose and visualize the content of target directory");

        pnlNorthEast.add(btnBrowseTarget);
        pnlNorthEast.add(txtTarget);
        pnlNorthEast.add(lblTarget);


        // North Center is for button swap
        pnlNorthCenter = new JPanel();
        pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);
        pnlNorthCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        btnSwap = new JButton("Swap");
        btnSwap.setToolTipText("Swap two chosen directories");
        pnlNorthCenter.add(btnSwap, BorderLayout.CENTER);


        // Read the configuration file
        readUserConfig();

        /**
         *
         */
        btnSwap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ((sourcePath != null) || (targetPath != null)) {

                    // Exchange the values of 2 Strings: source directory and target directory
                    String tempPath;
                    tempPath = sourcePath;
                    sourcePath = targetPath;
                    targetPath = tempPath;

                    // Display the new contents to textfields
                    txtSource.setText(sourcePath);
                    txtTarget.setText(targetPath);

                    // Set up new table
                    setUpTable(sourcePath, targetPath);

                    // Save changes from user input
                    saveChange();
                }
            }
        });

        btnBrowseSource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int response = fc.showOpenDialog(mainFrame);
                if (response == JFileChooser.APPROVE_OPTION) {
                    sourcePath = fc.getSelectedFile().toString();
                    txtSource.setText(sourcePath);

                    // Visualize the content of source directory to the table
                    setUpTable(sourcePath, targetPath);

                    // Save changes from user input
                    saveChange();
                }
            }
        });

        btnBrowseTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int response = fc.showOpenDialog(mainFrame);
                if (response == JFileChooser.APPROVE_OPTION) {
                    targetPath = fc.getSelectedFile().toString();
                    txtTarget.setText(targetPath);

                    // Visualize the content of target directory to the table
                    setUpTable(sourcePath, targetPath);

                    // Save changes from user input
                    saveChange();
                }
            }
        });

        btnSync.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (actionList != null) {
                    btnSync.setText("Syncing...");
                    sync();
                    btnSync.setText("Sync Completed!");
                }
            }

        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpTable(sourcePath, targetPath);
                btnSync.setText("Sync");
            }
        });


        /*
         * Add an Key pressed Listener to txtSource.
         * Whenever the user edits the path of source directory and press Enter,
         * update new value for String Source Directory,
         * the content in the visualizing table must be changed as well.
         */
        txtSource.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sourcePath = txtSource.getText();

                    // Change the content of the table
                    setUpTable(sourcePath, targetPath);
                }
            }
        });

        /*
         * Add an Key pressed Listener to txtTarget.
         * Whenever the user edits the path of Target directory and press Enter,
         * update new value for String Target Directory,
         * the content in the visualizing table must be changed as well.
         */
        txtTarget.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    targetPath = txtTarget.getText();

                    // Change the content of the table
                    setUpTable(sourcePath, targetPath);
                }
            }
        });
        
        tblModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                for (int i = 0; i < tblModel.getRowCount(); i++) {
                    enableList[i] = (Boolean) tblModel.getValueAt(i, 4);
                }
            }
        });
    }

    /**
     * Set up data for the table and show it to scroll panel.
     * @param source path of Source directory.
     * @param target path of Target directory.
     */
    private void setUpTable(String source, String target) {
        if (source != null && target != null) {

            System.out.println(sourcePath + targetPath);
            sourceDirectory = new File(source);
            targetDirectory = new File(target);
            sourceList = new FileList(sourceDirectory);
            sourceList.listAllFile(sourceDirectory);
            targetList = new FileList(targetDirectory);
            targetList.listAllFile(targetDirectory);
            targetList.compareList(sourceList);
            sourceList.extendList(targetList);
            actionList = new ActionList(sourceList, targetList);
            enableList = new Boolean[sourceList.size()];

            // First, every action is enabled
            for (int i = 0; i < sourceList.size(); i++) {
                enableList[i] = true;
            }
            tblModel.refreshTableModel(sourceList, targetList, actionList);
            tbl = new JTable(tblModel);

            // Adjust the column's width a bit
            tbl.getColumnModel().getColumn(0).setPreferredWidth(220);
            tbl.getColumnModel().getColumn(1).setPreferredWidth(68);
            tbl.getColumnModel().getColumn(2).setPreferredWidth(83);
            tbl.getColumnModel().getColumn(3).setPreferredWidth(55);
            tbl.getColumnModel().getColumn(4).setPreferredWidth(85);
            tbl.getColumnModel().getColumn(5).setPreferredWidth(220);
            tbl.getColumnModel().getColumn(6).setPreferredWidth(68);
            tbl.getColumnModel().getColumn(7).setPreferredWidth(83);

            // Show the table
            scroll.setViewportView(tbl);
        }
    }

    /**
     * Perform Synchronization task
     */
    private void sync() {
        for (int i = 0; i < actionList.size(); i++)
            if (enableList[i]) {
                switch (actionList.get(i)) {
                    case "CREATE":
                        FileOperator.create(sourcePath, targetPath, sourceList.get(i));
                        break;
                    case "DELETE":
                        FileOperator.delete(targetList.get(i));
                        break;
                    case "UPDATE":
                        FileOperator.copy(sourcePath, targetPath, sourceList.get(i), targetList.get(i));
                        break;
                    case "RESTORE":
                        FileOperator.copy(sourcePath, targetPath, sourceList.get(i), targetList.get(i));
                }

            }
        // show the change in two directories to the table
        setUpTable(sourcePath, targetPath);
    }

    /**
     * Read Source and Target paths (saved from the last use) from the text file
     */
    private void readUserConfig() {
        try {
            // Open init.txt file to load source and target paths
            InputStream ips = new FileInputStream(initFile);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            sourcePath = br.readLine();
            targetPath = br.readLine();
            br.close();
            System.out.println("Load configuration from last use successfully!");

            // Display on GUI
            txtSource.setText(sourcePath);
            txtTarget.setText(targetPath);
            setUpTable(sourcePath, targetPath);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     *  Save changes of source and target directory to the configuration text file.
     */
    private void saveChange() {
        btnSync.setText("Sync");
        try {
            // Open init.txt to write new source and target paths
            FileWriter fw = new FileWriter(initFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(sourcePath);
            bw.write("\n");
            bw.write(targetPath);
            bw.close();
            System.out.println("Change has been saved!");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}