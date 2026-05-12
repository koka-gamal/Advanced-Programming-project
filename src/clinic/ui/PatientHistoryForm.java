package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

// Screen used to search patient appointment history and cancel appointments.
public class PatientHistoryForm extends JFrame {

    // Input field displayed beside the Patient ID label.
    private JTextField patientIdField;

    // Button displayed in the top panel to search appointments.
    private JButton searchButton;

    // Button displayed in the top panel to cancel the selected appointment.
    private JButton cancelButton;

    // Table displayed in the center of the screen.
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    // Builds the patient history window, search controls, and appointment table.
    public PatientHistoryForm() {

        // Window setup.
        setTitle("Patient History");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel displayed above the appointment table.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));

        // Label displayed before the patient ID field.
        JLabel patientIdLabel = new JLabel("Patient ID:");

        patientIdField = new JTextField(15);

        // Button displayed beside the patient ID field.
        searchButton = new JButton("Search");

        // Button displayed beside the search button.
        cancelButton = new JButton("Cancel Appointment");

        topPanel.add(patientIdLabel);
        topPanel.add(patientIdField);
        topPanel.add(searchButton);
        topPanel.add(cancelButton);

        // Table columns displayed in the appointment results table.
        String[] columns = {
            "Patient ID",
            "Doctor",
            "Date",
            "Time",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0);

        appointmentTable = new JTable(tableModel);

        // Scroll pane displayed around the appointment table.
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(event -> searchAppointments());
        cancelButton.addActionListener(event -> cancelAppointment());

        setVisible(true);
    }

    // Searches appointments using the patient ID entered by the user.
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

    // Cancels the appointment selected in the table.
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

                    // Change status to Cancelled.
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
