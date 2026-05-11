package clinic.app;

import clinic.ui.MainMenuForm;

import javax.swing.SwingUtilities;

/**
 * Program entry point for the Clinic Appointment System.
 */
public class Main {

    /**
     * Starts the Swing application on the Event Dispatch Thread.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuForm().setVisible(true));
    }
}
