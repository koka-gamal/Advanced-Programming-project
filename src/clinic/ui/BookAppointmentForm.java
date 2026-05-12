package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;

// Screen used to book an appointment for an existing patient.
public class BookAppointmentForm extends JFrame {
    private JTextField txtPatientId;
    private JComboBox<Doctor> cmbDoctor;
    private JSpinner dateSpinner;
    private JComboBox<String> cmbTime;

    // Builds the appointment booking form and its controls.
    public BookAppointmentForm() {
        setTitle("Book Appointment");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title label displayed at the top of the booking form.
        JLabel title = new JLabel("Book an Appointment", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));

        // Text field displayed beside the Patient ID label.
        txtPatientId = new JTextField();

        // Doctor dropdown displayed beside the Doctor label.
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

        // Date picker displayed beside the Date label.
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        // Time dropdown displayed beside the Time label.
        cmbTime = new JComboBox<>(generateTimeSlots());

        // Label and input area displayed in the center of the screen.
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 20, 25));
        formPanel.setBorder(BorderFactory.createEmptyBorder(70, 250, 70, 250));
        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(txtPatientId);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(cmbDoctor);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateSpinner);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(cmbTime);

        // Button that books the appointment.
        JButton btnBook = new JButton("Book Appointment");

        // Button that closes this form.
        JButton btnBack = new JButton("Back");

        btnBook.setBackground(new Color(60, 120, 200));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);

        // Panel displayed at the bottom for form buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.add(btnBook);
        buttonPanel.add(btnBack);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBook.addActionListener(event -> bookAppointment());
        btnBack.addActionListener(event -> dispose());
    }

    // Removes hours, minutes, and seconds so date checks compare by day.
    private Date stripTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // Creates 30-minute appointment slots from 8:00 AM to 7:30 PM.
    private String[] generateTimeSlots() {
        ArrayList<String> slots = new ArrayList<>();

        for (int hour = 8; hour < 20; hour++) {
            int displayHour = hour > 12 ? hour - 12 : hour;
            String period = hour < 12 ? "AM" : "PM";

            slots.add(String.format("%02d:00 %s", displayHour, period));
            slots.add(String.format("%02d:30 %s", displayHour, period));
        }

        return slots.toArray(new String[0]);
    }

    // Validates the form, checks the patient exists, and saves the appointment.
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

    // Resets the booking fields after a successful booking.
    private void resetForm() {
        txtPatientId.setText("");
        dateSpinner.setValue(stripTime(new Date()));
        cmbTime.setSelectedIndex(0);
    }
}
