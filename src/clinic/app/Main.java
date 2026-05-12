package clinic.app;

import clinic.ui.*;

import javax.swing.*;

// Starts the Clinic Appointment System.
public class Main {

    // Opens the main menu on the Swing event thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuForm().setVisible(true));
    }
}
