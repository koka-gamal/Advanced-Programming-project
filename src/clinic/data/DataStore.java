package clinic.data;

import clinic.model.Appointment;
import clinic.model.Doctor;
import clinic.model.Patient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Central shared storage for the whole Clinic Appointment System.
 *
 * All GUI forms read from and write to these public static lists. This keeps
 * integration simple for the team because every screen uses the same patients,
 * doctors, and appointments.
 */
public class DataStore {
    public static ArrayList<Patient> patients = new ArrayList<>();
    public static ArrayList<Appointment> appointments = new ArrayList<>();
    public static ArrayList<Doctor> doctors = new ArrayList<>();

    public static int nextPatientId = 1;
    public static int nextAppointmentId = 1;

    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.txt";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";

    static {
        loadDefaultDoctors();
        loadData();
    }

    /**
     * Private constructor prevents accidental DataStore objects.
     *
     * DataStore is meant to be used through its static shared lists and helper
     * methods only.
     */
    private DataStore() {
    }

    /**
     * Creates the predefined doctors used by the booking and viewing forms.
     */
    private static void loadDefaultDoctors() {
        doctors.add(new Doctor(1, "Dr. Ahmed Hassan", "Cardiology"));
        doctors.add(new Doctor(2, "Dr. Sara Mohamed", "Dermatology"));
        doctors.add(new Doctor(3, "Dr. Khaled Nasser", "Orthopedics"));
        doctors.add(new Doctor(4, "Dr. Nour Ibrahim", "Pediatrics"));
        doctors.add(new Doctor(5, "Dr. Omar Farouk", "General Practice"));
    }

    /**
     * Saves patients and appointments to simple text files.
     *
     * The project still works through ArrayLists while it runs. These files are
     * only a lightweight backup so test data can survive closing the app.
     */
    public static void saveData() {
        try {
            new File(DATA_DIR).mkdirs();
            savePatients();
            saveAppointments();
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Writes patient records using a simple pipe-separated text format.
     */
    private static void savePatients() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_FILE))) {
            writer.write("nextId=" + nextPatientId);
            writer.newLine();

            for (Patient patient : patients) {
                writer.write(patient.getId() + "|" + patient.getName() + "|"
                    + patient.getAge() + "|" + patient.getContact());
                writer.newLine();
            }
        }
    }

    /**
     * Writes appointment records using a simple pipe-separated text format.
     */
    private static void saveAppointments() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            writer.write("nextId=" + nextAppointmentId);
            writer.newLine();

            for (Appointment appointment : appointments) {
                writer.write(appointment.getAppointmentId() + "|" + appointment.getPatientId()
                    + "|" + appointment.getDoctorName() + "|" + appointment.getDate()
                    + "|" + appointment.getTime() + "|" + appointment.getStatus());
                writer.newLine();
            }
        }
    }

    /**
     * Loads any saved patient and appointment data when the app starts.
     */
    public static void loadData() {
        patients.clear();
        appointments.clear();
        nextPatientId = 1;
        nextAppointmentId = 1;

        loadPatients();
        loadAppointments();
    }

    /**
     * Reads patients from the text file if it exists.
     */
    private static void loadPatients() {
        File patientFile = new File(PATIENTS_FILE);
        if (!patientFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(patientFile))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("nextId=")) {
                nextPatientId = Integer.parseInt(line.split("=")[1]);
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", 4);
                if (fields.length == 4) {
                    patients.add(new Patient(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        Integer.parseInt(fields[2]),
                        fields[3]
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }
    }

    /**
     * Reads appointments from the text file if it exists.
     */
    private static void loadAppointments() {
        File appointmentFile = new File(APPOINTMENTS_FILE);
        if (!appointmentFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("nextId=")) {
                nextAppointmentId = Integer.parseInt(line.split("=")[1]);
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", 6);
                if (fields.length == 6) {
                    Appointment appointment = new Appointment(
                        Integer.parseInt(fields[0]),
                        Integer.parseInt(fields[1]),
                        fields[2],
                        fields[3],
                        fields[4]
                    );
                    appointment.setStatus(fields[5]);
                    appointments.add(appointment);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    /**
     * Finds a patient by ID.
     *
     * Returns null when no patient matches, so forms can show a friendly error
     * instead of crashing.
     */
    public static Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Returns all appointments for the selected doctor name.
     */
    public static ArrayList<Appointment> getAppointmentsByDoctor(String doctorName) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorName().equals(doctorName)) {
                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Returns all appointments connected to a patient ID.
     */
    public static ArrayList<Appointment> getAppointmentsByPatient(int patientId) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == patientId) {
                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Finds an appointment by ID.
     *
     * This helper is used by cancellation logic so the table row can be matched
     * back to the real appointment object.
     */
    public static Appointment findAppointmentById(int appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }
}
