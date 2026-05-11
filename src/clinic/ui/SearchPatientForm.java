package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Patient;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

/**
 * Helper form used to find patient IDs by name or contact number.
 */
public class SearchPatientForm extends JFrame {
    private JTextField txtSearch;
    private JRadioButton rbName;
    private JRadioButton rbPhone;
    private DefaultTableModel tableModel;
    private JLabel lblResults;

    /**
     * Builds the search screen and connects the search controls.
     */
    public SearchPatientForm() {
        setTitle("Search Patient");
        setSize(580, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("Search Patient", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        rbName = new JRadioButton("By Name", true);
        rbPhone = new JRadioButton("By Phone Number", false);

        ButtonGroup group = new ButtonGroup();
        group.add(rbName);
        group.add(rbPhone);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        radioPanel.add(new JLabel("Search:"));
        radioPanel.add(rbName);
        radioPanel.add(rbPhone);

        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnClear = new JButton("Clear");

        btnSearch.setBackground(new Color(60, 120, 200));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchBar.add(new JLabel("Keyword:"));
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnClear);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(radioPanel);
        topPanel.add(searchBar);

        lblResults = new JLabel("  Enter a search term and press Search.");
        lblResults.setFont(new Font("Arial", Font.ITALIC, 12));
        lblResults.setForeground(Color.GRAY);
        lblResults.setBorder(BorderFactory.createEmptyBorder(4, 10, 2, 0));

        String[] columns = {"Patient ID", "Name", "Age", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        JButton btnBack = new JButton("Back");
        btnBack.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttonPanel.add(btnBack);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(title, BorderLayout.NORTH);
        northWrapper.add(topPanel, BorderLayout.CENTER);
        northWrapper.add(lblResults, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 5));
        add(northWrapper, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(event -> search());
        txtSearch.addActionListener(event -> search());
        btnClear.addActionListener(event -> clearSearch());
        btnBack.addActionListener(event -> dispose());
    }

    /**
     * Searches patients by the selected search type and updates the table.
     */
    private void search() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a search term.",
                "Empty Search",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        tableModel.setRowCount(0);
        ArrayList<Patient> matches = new ArrayList<>();

        for (Patient patient : DataStore.patients) {
            if (matchesSearch(patient, keyword)) {
                matches.add(patient);
            }
        }

        showSearchResults(matches);
    }

    /**
     * Checks whether one patient matches the current search mode.
     */
    private boolean matchesSearch(Patient patient, String keyword) {
        if (rbName.isSelected()) {
            return patient.getName().toLowerCase().contains(keyword);
        }

        return patient.getContact().toLowerCase().contains(keyword);
    }

    /**
     * Fills the table and result label after a search.
     */
    private void showSearchResults(ArrayList<Patient> matches) {
        if (matches.isEmpty()) {
            lblResults.setText("  No patients found matching: \"" + txtSearch.getText().trim() + "\"");
            lblResults.setForeground(new Color(180, 60, 60));
            return;
        }

        for (Patient patient : matches) {
            tableModel.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getContact()
            });
        }

        lblResults.setText(
            "  Found " + matches.size() + " patient(s). Use the Patient ID to book or view history."
        );
        lblResults.setForeground(new Color(40, 130, 60));
    }

    /**
     * Clears the search box, result label, and table.
     */
    private void clearSearch() {
        txtSearch.setText("");
        tableModel.setRowCount(0);
        lblResults.setText("  Enter a search term and press Search.");
        lblResults.setForeground(Color.GRAY);
    }
}
