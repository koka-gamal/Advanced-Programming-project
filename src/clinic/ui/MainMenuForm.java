package clinic.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Main navigation window for the Clinic Appointment System.
 *
 * This form does not store data itself. It only opens the feature screens used
 * to add patients, book appointments, view doctor appointments, and manage
 * patient history.
 */
public class MainMenuForm extends JFrame {

    /**
     * Builds the main menu screen and connects each button to its form.
     */
    public MainMenuForm() {
        setTitle("Clinic Appointment System");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("Clinic Appointment System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        JLabel subtitle = new JLabel("Main Menu", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton btnAddPatient = createButton("Add Patient");
        JButton btnSearchPatient = createButton("Search Patient");
        JButton btnBookAppointment = createButton("Book Appointment");
        JButton btnViewDoctor = createButton("View Doctor Appointments");
        JButton btnPatientHistory = createButton("Patient History / Cancel");
        JButton btnExit = createButton("Exit");

        btnExit.setBackground(new Color(220, 80, 80));
        btnExit.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 20, 50));
        buttonPanel.add(btnAddPatient);
        buttonPanel.add(btnSearchPatient);
        buttonPanel.add(btnBookAppointment);
        buttonPanel.add(btnViewDoctor);
        buttonPanel.add(btnPatientHistory);
        buttonPanel.add(btnExit);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(title);
        headerPanel.add(subtitle);

        setLayout(new BorderLayout(0, 5));
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        btnAddPatient.addActionListener(event -> new AddPatientForm().setVisible(true));
        btnSearchPatient.addActionListener(event -> new SearchPatientForm().setVisible(true));
        btnBookAppointment.addActionListener(event -> new BookAppointmentForm().setVisible(true));
        btnViewDoctor.addActionListener(event -> new ViewAppointmentsForm().setVisible(true));
        btnPatientHistory.addActionListener(event -> new PatientHistoryForm().setVisible(true));
        btnExit.addActionListener(event -> confirmExit());
    }

    /**
     * Creates one consistently styled menu button.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Asks the user before closing the application.
     */
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
