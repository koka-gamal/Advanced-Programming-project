package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Appointment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/**
 * Screen used to search a patient's appointment history and cancel appointments.
 */
public class PatientHistoryForm extends JFrame {

    // Input field for patient ID.
    private JTextField patientIdField;

    // Action buttons for searching and cancelling.
    private JButton searchButton;
    private JButton cancelButton;

    // Table components used to display appointment results.
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    /**
     * Builds the patient history window, search controls, and appointment table.
     */
    public PatientHistoryForm() {

        // Window setup.
        setTitle("Patient History");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel.
        JPanel topPanel = new JPanel();

        JLabel patientIdLabel = new JLabel("Patient ID:");

        patientIdField = new JTextField(15);

        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel Appointment");

        topPanel.add(patientIdLabel);
        topPanel.add(patientIdField);
        topPanel.add(searchButton);
        topPanel.add(cancelButton);

        // Table columns.
        String[] columns = {
            "Patient ID",
            "Doctor",
            "Date",
            "Time",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0);

        appointmentTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        // Add components to frame.
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions.
        searchButton.addActionListener(event -> searchAppointments());

        cancelButton.addActionListener(event -> cancelAppointment());

        setVisible(true);
    }

    /**
     * Searches appointments using the patient ID entered by the user.
     */
    private void searchAppointments() {

        String input = patientIdField.getText().trim();

        // Check if field is empty.
        if (input.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Enter patient ID"
            );

            return;
        }

        int patientId;

        // Make sure patient ID is numeric.
        try {

            patientId = Integer.parseInt(input);

        } catch (NumberFormatException exception) {

            JOptionPane.showMessageDialog(
                this,
                "Patient ID must be numeric"
            );

            return;
        }

        // Clear old rows before showing new results.
        tableModel.setRowCount(0);

        boolean found = false;

        // Loop through appointments and show only the selected patient's rows.
        for (Appointment appointment : DataStore.appointments) {

            if (appointment.getPatientId() == patientId) {

                Object[] row = {
                    appointment.getPatientId(),
                    appointment.getDoctorName(),
                    appointment.getDate(),
                    appointment.getTime(),
                    appointment.getStatus()
                };

                tableModel.addRow(row);

                found = true;
            }
        }

        // If no appointments were found.
        if (!found) {

            JOptionPane.showMessageDialog(
                this,
                "No appointments found"
            );
        }
    }

    /**
     * Cancels the appointment selected in the table.
     */
    private void cancelAppointment() {

        int selectedRow = appointmentTable.getSelectedRow();

        // Make sure a row is selected.
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Select an appointment first"
            );

            return;
        }

        // Get selected appointment data.
        int patientId = Integer.parseInt(
            tableModel.getValueAt(selectedRow, 0).toString()
        );

        String doctorName =
            tableModel.getValueAt(selectedRow, 1).toString();

        String date =
            tableModel.getValueAt(selectedRow, 2).toString();

        String time =
            tableModel.getValueAt(selectedRow, 3).toString();

        try {

            // Find matching appointment.
            for (Appointment appointment : DataStore.appointments) {

                if (appointment.getPatientId() == patientId
                    && appointment.getDoctorName().equals(doctorName)
                    && appointment.getDate().equals(date)
                    && appointment.getTime().equals(time)) {

                    // Change status to cancelled.
                    appointment.setStatus(Appointment.STATUS_CANCELLED);
                    DataStore.saveData();

                    JOptionPane.showMessageDialog(
                        this,
                        "Appointment cancelled"
                    );

                    // Refresh table.
                    searchAppointments();

                    return;
                }
            }

        } catch (Exception exception) {

            JOptionPane.showMessageDialog(
                this,
                "Something went wrong"
            );
        }
    }
}
