package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

// Screen used to search patient appointment history and update appointments.
public class PatientHistoryForm extends JFrame {

    // Input field displayed beside the Patient ID label.
    private JTextField patientIdField;

    // Button displayed in the top panel to search appointments.
    private JButton searchButton;

    // Table displayed in the center of the screen.
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    // Builds the patient history window, search controls, appointment table, and status buttons.
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

        topPanel.add(patientIdLabel);
        topPanel.add(patientIdField);
        topPanel.add(searchButton);

        // Table columns displayed in the appointment results table.
        String[] columns = {
            "Appt ID",
            "Patient ID",
            "Doctor",
            "Date",
            "Time",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(28);

        // Scroll pane displayed around the appointment table.
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        // Button that marks the selected appointment as Completed.
        JButton completeButton = new JButton("Complete Appointment");

        // Button that marks the selected appointment as Cancelled.
        JButton cancelButton = new JButton("Cancel Appointment");

        // Button that closes this form.
        JButton backButton = new JButton("Back");

        completeButton.setBackground(new Color(60, 160, 90));
        completeButton.setForeground(Color.WHITE);
        completeButton.setFocusPainted(false);

        cancelButton.setBackground(new Color(220, 80, 80));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        // Panel displayed at the bottom for appointment action buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(event -> searchAppointments());
        patientIdField.addActionListener(event -> searchAppointments());
        completeButton.addActionListener(event -> updateSelectedAppointment(Appointment.STATUS_COMPLETED));
        cancelButton.addActionListener(event -> updateSelectedAppointment(Appointment.STATUS_CANCELLED));
        backButton.addActionListener(event -> dispose());

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

        loadPatientAppointments(patientId, true);
    }

    // Loads appointment rows for the entered patient ID.
    private void loadPatientAppointments(int patientId, boolean showEmptyMessage) {
        tableModel.setRowCount(0);

        boolean found = false;

        // Loop through appointments and show only the selected patient's rows.
        for (Appointment appointment : DataStore.appointments) {

            if (appointment.getPatientId() == patientId) {

                Object[] row = {
                    appointment.getAppointmentId(),
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
        if (!found && showEmptyMessage) {

            JOptionPane.showMessageDialog(
                this,
                "No appointments found"
            );
        }
    }

    // Updates the selected appointment status and refreshes the table.
    private void updateSelectedAppointment(String newStatus) {

        int selectedRow = appointmentTable.getSelectedRow();

        // Make sure a row is selected.
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Select an appointment first"
            );

            return;
        }

        int appointmentId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        Appointment appointment = DataStore.findAppointmentById(appointmentId);

        if (appointment == null) {
            JOptionPane.showMessageDialog(
                this,
                "Appointment could not be found.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (appointment.getStatus().equals(newStatus)) {
            JOptionPane.showMessageDialog(
                this,
                "Appointment is already " + newStatus + ".",
                "No Change",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        if (!appointment.getStatus().equals(Appointment.STATUS_SCHEDULED)) {
            JOptionPane.showMessageDialog(
                this,
                "Only Scheduled appointments can be updated.",
                "Invalid Action",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        appointment.setStatus(newStatus);
        DataStore.saveData();
        loadPatientAppointments(appointment.getPatientId(), false);

        JOptionPane.showMessageDialog(
            this,
            "Appointment marked as " + newStatus + ".",
            "Updated",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
