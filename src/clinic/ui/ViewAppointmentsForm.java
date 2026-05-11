package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Appointment;
import clinic.model.Doctor;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/**
 * Form used to display appointments for one selected doctor.
 */
public class ViewAppointmentsForm extends JFrame {
    private JComboBox<Doctor> doctorCombo;
    private JTable table;
    private DefaultTableModel model;

    /**
     * Builds the doctor filter and appointment table.
     */
    public ViewAppointmentsForm() {
        setTitle("View Doctor Appointments");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Choose Doctor:"));

        doctorCombo = new JComboBox<>();
        for (Doctor doctor : DataStore.doctors) {
            doctorCombo.addItem(doctor);
        }
        topPanel.add(doctorCombo);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Patient ID", "Doctor", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        doctorCombo.addActionListener(event -> loadAppointments());
        loadAppointments();
    }

    /**
     * Reloads the table using appointments for the selected doctor.
     */
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
