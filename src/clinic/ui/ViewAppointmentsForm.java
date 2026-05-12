package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

// Screen used to display appointments for one selected doctor.
public class ViewAppointmentsForm extends JFrame {
    private JComboBox<Doctor> doctorCombo;
    private JTable table;
    private DefaultTableModel model;

    // Builds the doctor filter and appointment table.
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
        String[] columns = {"Patient ID", "Doctor", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // Scroll pane displayed around the appointment table.
        add(new JScrollPane(table), BorderLayout.CENTER);

        doctorCombo.addActionListener(event -> loadAppointments());
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
                appointment.getPatientId(),
                appointment.getDoctorName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getStatus()
            });
        }
    }
}
