package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

// Screen used to find patient IDs by name or contact number.
public class SearchPatientForm extends JFrame {
    private JTextField txtSearch;
    private JRadioButton rbName;
    private JRadioButton rbPhone;
    private DefaultTableModel tableModel;
    private JLabel lblResults;

    // Builds the search patient screen and its controls.
    public SearchPatientForm() {
        setTitle("Search Patient");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title label displayed at the top of the form.
        JLabel title = new JLabel("Search Patient", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        // Radio button displayed for name search mode.
        rbName = new JRadioButton("By Name", true);

        // Radio button displayed for phone search mode.
        rbPhone = new JRadioButton("By Phone Number", false);

        ButtonGroup group = new ButtonGroup();
        group.add(rbName);
        group.add(rbPhone);

        // Panel displayed below the title for search type controls.
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        radioPanel.add(new JLabel("Search:"));
        radioPanel.add(rbName);
        radioPanel.add(rbPhone);

        // Text field displayed beside the Keyword label.
        txtSearch = new JTextField(25);

        // Button that runs the patient search.
        JButton btnSearch = new JButton("Search");

        // Button that clears search results.
        JButton btnClear = new JButton("Clear");

        btnSearch.setBackground(new Color(60, 120, 200));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        // Panel displayed below the search type controls.
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchBar.add(new JLabel("Keyword:"));
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnClear);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(radioPanel);
        topPanel.add(searchBar);

        // Result label displayed above the patient table.
        lblResults = new JLabel("  Enter a search term and press Search.");
        lblResults.setFont(new Font("Arial", Font.ITALIC, 14));
        lblResults.setForeground(Color.GRAY);
        lblResults.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 0));

        // Table columns displayed in the patient result table.
        String[] columns = {"Patient ID", "Name", "Age", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);

        // Button that closes this form.
        JButton btnBack = new JButton("Back");
        btnBack.setFocusPainted(false);

        // Panel displayed at the bottom of the form.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 20));
        buttonPanel.add(btnBack);

        // Panel displayed at the top with title, controls, and result label.
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

    // Searches patients by the selected search type and updates the table.
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

    // Checks whether one patient matches the selected search mode.
    private boolean matchesSearch(Patient patient, String keyword) {
        if (rbName.isSelected()) {
            return patient.getName().toLowerCase().contains(keyword);
        }

        return patient.getContact().toLowerCase().contains(keyword);
    }

    // Fills the patient table and updates the result label.
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

    // Clears the search box, result label, and table.
    private void clearSearch() {
        txtSearch.setText("");
        tableModel.setRowCount(0);
        lblResults.setText("  Enter a search term and press Search.");
        lblResults.setForeground(Color.GRAY);
    }
}
