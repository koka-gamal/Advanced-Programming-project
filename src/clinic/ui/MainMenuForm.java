package clinic.ui;

import javax.swing.*;
import java.awt.*;

// Main navigation screen for the Clinic Appointment System.
public class MainMenuForm extends JFrame {

    // Builds the main menu and connects each button to its screen.
    public MainMenuForm() {
        setTitle("Clinic Appointment System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main title label displayed at the top of the screen.
        JLabel title = new JLabel("Clinic Appointment System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));

        // Subtitle label displayed below the main title.
        JLabel subtitle = new JLabel("Main Menu", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Button that opens the Add Patient screen.
        JButton btnAddPatient = createButton("Add Patient");

        // Button that opens the Search Patient screen.
        JButton btnSearchPatient = createButton("Search Patient");

        // Button that opens the Add Doctor screen.
        JButton btnAddDoctor = createButton("Add Doctor");

        // Button that opens the Book Appointment screen.
        JButton btnBookAppointment = createButton("Book Appointment");

        // Button that opens the doctor appointments screen.
        JButton btnViewDoctor = createButton("View Doctor Appointments");

        // Button that opens the patient history and cancel screen.
        JButton btnPatientHistory = createButton("Patient History / Cancel");

        // Button that exits the application.
        JButton btnExit = createButton("Exit");
        btnExit.setBackground(new Color(220, 80, 80));
        btnExit.setForeground(Color.WHITE);

        // Panel that displays the main menu buttons.
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 300, 90, 300));
        buttonPanel.add(btnAddPatient);
        buttonPanel.add(btnSearchPatient);
        buttonPanel.add(btnAddDoctor);
        buttonPanel.add(btnBookAppointment);
        buttonPanel.add(btnViewDoctor);
        buttonPanel.add(btnPatientHistory);
        buttonPanel.add(btnExit);

        // Panel that displays the title and subtitle.
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(title);
        headerPanel.add(subtitle);

        setLayout(new BorderLayout(0, 5));
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        btnAddPatient.addActionListener(event -> new AddPatientForm().setVisible(true));
        btnSearchPatient.addActionListener(event -> new SearchPatientForm().setVisible(true));
        btnAddDoctor.addActionListener(event -> new AddDoctorForm().setVisible(true));
        btnBookAppointment.addActionListener(event -> new BookAppointmentForm().setVisible(true));
        btnViewDoctor.addActionListener(event -> new ViewAppointmentsForm().setVisible(true));
        btnPatientHistory.addActionListener(event -> new PatientHistoryForm().setVisible(true));
        btnExit.addActionListener(event -> confirmExit());
    }

    // Creates one menu button with matching style.
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false);
        return button;
    }

    // Shows a confirmation message before closing the application.
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
