package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Appointment;
import clinic.model.Doctor;
import clinic.model.Patient;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Form used to book a patient appointment with a predefined doctor.
 */
public class BookAppointmentForm extends JFrame {
    private JTextField txtPatientId;
    private JComboBox<Doctor> cmbDoctor;
    private JSpinner dateSpinner;
    private JComboBox<String> cmbTime;

    /**
     * Builds the booking screen and prepares the doctor/date/time controls.
     */
    public BookAppointmentForm() {
        setTitle("Book Appointment");
        setSize(440, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("Book an Appointment", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        txtPatientId = new JTextField();
        cmbDoctor = new JComboBox<>();
        for (Doctor doctor : DataStore.doctors) {
            cmbDoctor.addItem(doctor);
        }

        Date today = stripTime(new Date());
        SpinnerDateModel dateModel = new SpinnerDateModel(
            today,
            today,
            null,
            Calendar.DAY_OF_MONTH
        );
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        cmbTime = new JComboBox<>(generateTimeSlots());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(txtPatientId);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(cmbDoctor);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateSpinner);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(cmbTime);

        JButton btnBook = new JButton("Book Appointment");
        JButton btnBack = new JButton("Back");

        btnBook.setBackground(new Color(60, 120, 200));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.add(btnBook);
        buttonPanel.add(btnBack);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBook.addActionListener(event -> bookAppointment());
        btnBack.addActionListener(event -> dispose());
    }

    /**
     * Removes hours, minutes, and seconds from a date.
     *
     * The date spinner uses this so the minimum date compares only by calendar
     * day, not by the current time of day.
     */
    private Date stripTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Creates 30-minute appointment slots from 8:00 AM to 7:30 PM.
     */
    private String[] generateTimeSlots() {
        java.util.List<String> slots = new java.util.ArrayList<>();

        for (int hour = 8; hour < 20; hour++) {
            int displayHour = hour > 12 ? hour - 12 : hour;
            String period = hour < 12 ? "AM" : "PM";

            slots.add(String.format("%02d:00 %s", displayHour, period));
            slots.add(String.format("%02d:30 %s", displayHour, period));
        }

        return slots.toArray(new String[0]);
    }

    /**
     * Validates the form, checks the patient exists, and saves the appointment.
     */
    private void bookAppointment() {
        String patientIdText = txtPatientId.getText().trim();

        if (patientIdText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Patient ID is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int patientId;
        try {
            patientId = Integer.parseInt(patientIdText);
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
            JOptionPane.showMessageDialog(
                this,
                "No patient found with ID: " + patientId + ".",
                "Patient Not Found",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Doctor selectedDoctor = (Doctor) cmbDoctor.getSelectedItem();
        if (selectedDoctor == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please choose a doctor.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String date = new SimpleDateFormat("dd/MM/yyyy").format((Date) dateSpinner.getValue());
        String time = (String) cmbTime.getSelectedItem();
        String doctorName = selectedDoctor.getName();

        int appointmentId = DataStore.nextAppointmentId++;
        Appointment appointment = new Appointment(
            appointmentId,
            patientId,
            doctorName,
            date,
            time
        );
        DataStore.appointments.add(appointment);
        DataStore.saveData();

        JOptionPane.showMessageDialog(
            this,
            "Appointment booked successfully!\n\n"
                + "Appointment ID : " + appointmentId + "\n"
                + "Patient        : " + patient.getName() + "\n"
                + "Doctor         : " + doctorName + "\n"
                + "Date           : " + date + "\n"
                + "Time           : " + time,
            "Booking Confirmed",
            JOptionPane.INFORMATION_MESSAGE
        );

        resetForm();
    }

    /**
     * Resets the booking controls after a successful booking.
     */
    private void resetForm() {
        txtPatientId.setText("");
        dateSpinner.setValue(stripTime(new Date()));
        cmbTime.setSelectedIndex(0);
    }
}
