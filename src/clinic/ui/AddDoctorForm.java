package clinic.ui;

import clinic.data.*;
import clinic.model.*;

import javax.swing.*;
import java.awt.*;

// Screen used to add a new doctor to the system.
public class AddDoctorForm extends JFrame {
    private JTextField txtName;
    private JTextField txtSpecialization;

    // Builds the add doctor form and its controls.
    public AddDoctorForm() {
        setTitle("Add New Doctor");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Title label displayed at the top of the form.
        JLabel title = new JLabel("Add New Doctor", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));

        // Text field displayed beside the Doctor Name label.
        txtName = new JTextField();

        // Text field displayed beside the Specialization label.
        txtSpecialization = new JTextField();

        // Label and input area displayed in the center of the screen.
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 20, 25));
        formPanel.setBorder(BorderFactory.createEmptyBorder(120, 250, 120, 250));
        formPanel.add(new JLabel("Doctor Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(txtSpecialization);

        // Button that saves the doctor.
        JButton btnAdd = new JButton("Add Doctor");

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

        btnAdd.addActionListener(event -> addDoctor());
        btnClear.addActionListener(event -> clearForm());
        btnBack.addActionListener(event -> dispose());
    }

    // Validates the entered doctor details and stores the doctor.
    private void addDoctor() {
        String name = txtName.getText().trim();
        String specialization = txtSpecialization.getText().trim();

        if (name.isEmpty() || specialization.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "All fields are required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (DataStore.findDoctorByName(name) != null) {
            JOptionPane.showMessageDialog(
                this,
                "A doctor with this name already exists.",
                "Duplicate Doctor",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int id = DataStore.nextDoctorId++;
        Doctor doctor = new Doctor(id, name, specialization);
        DataStore.doctors.add(doctor);
        DataStore.saveData();

        JOptionPane.showMessageDialog(
            this,
            "Doctor added successfully!\nDoctor ID: " + id,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );

        clearForm();
    }

    // Clears the form after saving or when the Clear button is pressed.
    private void clearForm() {
        txtName.setText("");
        txtSpecialization.setText("");
    }
}
