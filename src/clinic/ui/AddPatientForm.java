package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import java.awt.*;

// Screen used to register a new patient.
public class AddPatientForm extends JFrame {
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtContact;

    // Builds the add patient form and its controls.
    public AddPatientForm() {
        setTitle("Add New Patient");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title label displayed at the top of the form.
        JLabel title = new JLabel("Add New Patient", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));

        // Text field displayed beside the Full Name label.
        txtName = new JTextField();

        // Text field displayed beside the Age label.
        txtAge = new JTextField();

        // Text field displayed beside the Contact label.
        txtContact = new JTextField();

        // Label and input area displayed in the center of the screen.
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 20, 25));
        formPanel.setBorder(BorderFactory.createEmptyBorder(80, 250, 80, 250));
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(txtAge);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(txtContact);

        // Button that saves the patient.
        JButton btnAdd = new JButton("Add Patient");

        // Button that clears the text fields.
        JButton btnClear = new JButton("Clear");

        // Button that closes this form.
        JButton btnBack = new JButton("Back");

        btnAdd.setBackground(new Color(60, 160, 90));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        // Panel displayed at the bottom for form buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(event -> addPatient());
        btnClear.addActionListener(event -> clearForm());
        btnBack.addActionListener(event -> dispose());
    }

    // Validates the entered patient details and stores the patient.
    private void addPatient() {
        String name = txtName.getText().trim();
        String ageText = txtAge.getText().trim();
        String contact = txtContact.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "All fields are required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0 || age > 150) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Age must be a valid positive number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int id = DataStore.nextPatientId++;
        Patient patient = new Patient(id, name, age, contact);
        DataStore.patients.add(patient);
        DataStore.saveData();

        JOptionPane.showMessageDialog(
            this,
            "Patient added successfully!\nPatient ID: " + id,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        clearForm();
    }

    // Clears the form after saving or when the Clear button is pressed.
    private void clearForm() {
        txtName.setText("");
        txtAge.setText("");
        txtContact.setText("");
    }
}
