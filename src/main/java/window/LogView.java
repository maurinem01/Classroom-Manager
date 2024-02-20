package window;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import object.Log;

/**
 *
 * @author Maurine
 */
public class LogView extends Window {

    private static final long serialVersionUID = 1L;
    private JButton exportButton;
    private JScrollPane logScrollPane;
    private JTable logView;
	    
    // feed List<Log> from LogDAO.read() to table row model
	public LogView(List<Log> logs) {
        initComponents();
    }

    protected void initComponents() {

        logScrollPane = new JScrollPane();
        logView = new JTable();
        exportButton = new JButton("Export to CSV");

        logView.setAutoCreateRowSorter(true);
        logView.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        logScrollPane.setViewportView(logView);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(logScrollPane, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logScrollPane, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportButton)
                .addContainerGap())
        );

        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Attendance Log");
        getContentPane().requestFocusInWindow();
    }

}
