package clinic.ui;

import clinic.data.DataStore;
import clinic.model.Patient;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Form used to register a new patient in the shared DataStore.
 */
public class AddPatientForm extends JFrame {
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtContact;

    /**
     * Builds the add-patient window and connects its buttons.
     */
    public AddPatientForm() {
        setTitle("Add New Patient");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("Add New Patient", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        txtName = new JTextField();
        txtAge = new JTextField();
        txtContact = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(txtAge);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(txtContact);

        JButton btnAdd = new JButton("Add Patient");
        JButton btnClear = new JButton("Clear");
        JButton btnBack = new JButton("Back");

        btnAdd.setBackground(new Color(60, 160, 90));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
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

    /**
     * Validates the entered patient details and stores the patient.
     */
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

    /**
     * Clears all text fields after saving or when the user presses Clear.
     */
    private void clearForm() {
        txtName.setText("");
        txtAge.setText("");
        txtContact.setText("");
    }
}
