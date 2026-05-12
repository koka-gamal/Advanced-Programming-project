package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

// Screen used to display and update appointments for one selected doctor.
public class ViewAppointmentsForm extends JFrame {
    private JComboBox<Doctor> doctorCombo;
    private JTable table;
    private DefaultTableModel model;

    // Builds the doctor filter, appointment table, and status buttons.
    public ViewAppointmentsForm() {
        setTitle("View Doctor Appointments");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel displayed above the appointment table.
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));

        // Label displayed before the doctor dropdown.
        topPanel.add(new JLabel("Choose Doctor:"));

        // Doctor dropdown displayed in the top panel.
        doctorCombo = new JComboBox<>();
        for (Doctor doctor : DataStore.doctors) {
            doctorCombo.addItem(doctor);
        }
        topPanel.add(doctorCombo);
        add(topPanel, BorderLayout.NORTH);

        // Table columns displayed in the doctor appointment table.
        String[] columns = {"Appt ID", "Patient ID", "Doctor", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // Scroll pane displayed around the appointment table.
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button that marks the selected appointment as Completed.
        JButton btnComplete = new JButton("Complete Appointment");

        // Button that marks the selected appointment as Cancelled.
        JButton btnCancel = new JButton("Cancel Appointment");

        // Button that closes this form.
        JButton btnBack = new JButton("Back");

        btnComplete.setBackground(new Color(60, 160, 90));
        btnComplete.setForeground(Color.WHITE);
        btnComplete.setFocusPainted(false);

        btnCancel.setBackground(new Color(220, 80, 80));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        // Panel displayed at the bottom for appointment action buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.add(btnComplete);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        doctorCombo.addActionListener(event -> loadAppointments());
        btnComplete.addActionListener(event -> updateSelectedAppointment(Appointment.STATUS_COMPLETED));
        btnCancel.addActionListener(event -> updateSelectedAppointment(Appointment.STATUS_CANCELLED));
        btnBack.addActionListener(event -> dispose());

        loadAppointments();
    }

    // Reloads the table using appointments for the selected doctor.
    private void loadAppointments() {
        model.setRowCount(0);

        Doctor selectedDoctor = (Doctor) doctorCombo.getSelectedItem();
        if (selectedDoctor == null) {
            return;
        }

        for (Appointment appointment : DataStore.getAppointmentsByDoctor(selectedDoctor.getName())) {
            model.addRow(new Object[]{
                appointment.getAppointmentId(),
                appointment.getPatientId(),
                appointment.getDoctorName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getStatus()
            });
        }
    }

    // Updates the selected appointment status and refreshes the table.
    private void updateSelectedAppointment(String newStatus) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select an appointment first.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int appointmentId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
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
        loadAppointments();

        JOptionPane.showMessageDialog(
            this,
            "Appointment marked as " + newStatus + ".",
            "Updated",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
