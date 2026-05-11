package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Appointment;
import clinic.model.Patient;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Form used to search a patient's appointment history and cancel appointments.
 */
public class PatientHistoryForm extends JFrame {
    private JTextField txtPatientId;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblPatientInfo;
    private int currentPatientId = -1;

    /**
     * Builds the patient history screen, including search, table, and buttons.
     */
    public PatientHistoryForm() {
        setTitle("Patient History");
        setSize(660, 460);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Patient History & Appointment Actions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        txtPatientId = new JTextField(10);
        JButton btnSearch = new JButton("Search");
        btnSearch.setFocusPainted(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchPanel.add(new JLabel("Patient ID:"));
        searchPanel.add(txtPatientId);
        searchPanel.add(btnSearch);

        lblPatientInfo = new JLabel(" ");
        lblPatientInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblPatientInfo.setBorder(BorderFactory.createEmptyBorder(0, 15, 6, 0));

        String[] columns = {"Appt ID", "Doctor", "Date", "Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JButton btnCancel = new JButton("Cancel Appointment");
        JButton btnBack = new JButton("Back");

        btnCancel.setBackground(new Color(220, 80, 80));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnBack);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(title, BorderLayout.NORTH);
        northWrapper.add(searchPanel, BorderLayout.CENTER);
        northWrapper.add(lblPatientInfo, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 5));
        add(northWrapper, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(event -> searchPatient());
        txtPatientId.addActionListener(event -> searchPatient());
        btnCancel.addActionListener(event -> cancelSelectedAppointment());
        btnBack.addActionListener(event -> dispose());
    }

    /**
     * Validates the patient ID and loads the matching patient history.
     */
    private void searchPatient() {
        String idText = txtPatientId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a Patient ID.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int patientId;
        try {
            patientId = Integer.parseInt(idText);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Patient ID must be a number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Patient patient = DataStore.findPatientById(patientId);
        if (patient == null) {
            currentPatientId = -1;
            lblPatientInfo.setText(" ");
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(
                this,
                "No patient found with ID: " + patientId,
                "Not Found",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        currentPatientId = patientId;
        lblPatientInfo.setText(
            "  Patient: " + patient.getName()
                + "   |   Age: " + patient.getAge()
                + "   |   Contact: " + patient.getContact()
        );

        loadPatientHistory(patientId, true);
    }

    /**
     * Loads the patient's appointments into the table.
     */
    private void loadPatientHistory(int patientId, boolean showEmptyMessage) {
        tableModel.setRowCount(0);

        ArrayList<Appointment> history = DataStore.getAppointmentsByPatient(patientId);
        for (Appointment appointment : history) {
            tableModel.addRow(new Object[]{
                appointment.getAppointmentId(),
                appointment.getDoctorName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getStatus()
            });
        }

        if (showEmptyMessage && history.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No appointments found for this patient.",
                "No History",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Cancels the selected appointment and refreshes the table.
     */
    private void cancelSelectedAppointment() {
        if (currentPatientId == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Search for a patient before cancelling an appointment.",
                "No Patient Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an appointment.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);
        if (Appointment.STATUS_CANCELLED.equals(currentStatus)) {
            JOptionPane.showMessageDialog(
                this,
                "Appointment is already Cancelled.",
                "No Change",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (!Appointment.STATUS_SCHEDULED.equals(currentStatus)) {
            JOptionPane.showMessageDialog(
                this,
                "Only Scheduled appointments can be cancelled.",
                "Invalid Action",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Cancel this appointment?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment appointment = DataStore.findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(Appointment.STATUS_CANCELLED);
            DataStore.saveData();
        }

        loadPatientHistory(currentPatientId, false);
        JOptionPane.showMessageDialog(
            this,
            "Appointment cancelled successfully.",
            "Updated",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
