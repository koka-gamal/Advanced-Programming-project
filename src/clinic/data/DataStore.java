package clinic.data;

import clinic.model.*;

import java.io.*;
import java.util.*;

// Central shared storage for patients, doctors, and appointments.
public class DataStore {
    public static ArrayList<Patient> patients = new ArrayList<>();
    public static ArrayList<Appointment> appointments = new ArrayList<>();
    public static ArrayList<Doctor> doctors = new ArrayList<>();

    public static int nextPatientId = 1;
    public static int nextAppointmentId = 1;
    public static int nextDoctorId = 6;

    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.txt";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.txt";

    static {
        loadDefaultDoctors();
        loadData();
    }

    private DataStore() {
    }

    // Adds the fixed list of doctors available in the system.
    private static void loadDefaultDoctors() {
        doctors.add(new Doctor(1, "Dr. Ahmed Hassan", "Cardiology"));
        doctors.add(new Doctor(2, "Dr. Sara Mohamed", "Dermatology"));
        doctors.add(new Doctor(3, "Dr. Khaled Nasser", "Orthopedics"));
        doctors.add(new Doctor(4, "Dr. Nour Ibrahim", "Pediatrics"));
        doctors.add(new Doctor(5, "Dr. Omar Farouk", "General Practice"));
    }

    // Saves patient and appointment records to simple text files.
    public static void saveData() {
        try {
            new File(DATA_DIR).mkdirs();
            saveDoctors();
            savePatients();
            saveAppointments();
        } catch (IOException exception) {
            System.err.println("Error saving data: " + exception.getMessage());
        }
    }

    // Writes doctor rows to the doctors text file.
    private static void saveDoctors() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTORS_FILE))) {
            writer.write("nextId=" + nextDoctorId);
            writer.newLine();

            for (Doctor doctor : doctors) {
                writer.write(doctor.getId() + "|" + doctor.getName() + "|"
                    + doctor.getSpecialization());
                writer.newLine();
            }
        }
    }

    // Writes patient rows to the patients text file.
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

    // Writes appointment rows to the appointments text file.
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

    // Loads saved records when the application starts.
    public static void loadData() {
        patients.clear();
        appointments.clear();
        nextPatientId = 1;
        nextAppointmentId = 1;
        nextDoctorId = 6;

        loadDoctors();
        loadPatients();
        loadAppointments();
    }

    // Reads doctor records from the doctors text file.
    private static void loadDoctors() {
        File doctorFile = new File(DOCTORS_FILE);
        if (!doctorFile.exists()) {
            updateNextDoctorId();
            return;
        }

        doctors.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(doctorFile))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("nextId=")) {
                nextDoctorId = Integer.parseInt(line.split("=")[1]);
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", 3);
                if (fields.length == 3) {
                    doctors.add(new Doctor(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        fields[2]
                    ));
                }
            }

            if (doctors.isEmpty()) {
                loadDefaultDoctors();
                updateNextDoctorId();
            }
        } catch (IOException | NumberFormatException exception) {
            System.err.println("Error loading doctors: " + exception.getMessage());
            doctors.clear();
            loadDefaultDoctors();
            updateNextDoctorId();
        }
    }

    // Reads patient records from the patients text file.
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
        } catch (IOException | NumberFormatException exception) {
            System.err.println("Error loading patients: " + exception.getMessage());
        }
    }

    // Reads appointment records from the appointments text file.
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
        } catch (IOException | NumberFormatException exception) {
            System.err.println("Error loading appointments: " + exception.getMessage());
        }
    }

    // Finds a patient by ID and returns null when no match exists.
    public static Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    // Returns all appointments for one doctor.
    public static ArrayList<Appointment> getAppointmentsByDoctor(String doctorName) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorName().equals(doctorName)) {
                result.add(appointment);
            }
        }

        return result;
    }

    // Returns all appointments for one patient.
    public static ArrayList<Appointment> getAppointmentsByPatient(int patientId) {
        ArrayList<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == patientId) {
                result.add(appointment);
            }
        }

        return result;
    }

    // Finds an appointment by ID and returns null when no match exists.
    public static Appointment findAppointmentById(int appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }

    // Finds a doctor by name and returns null when no match exists.
    public static Doctor findDoctorByName(String name) {
        for (Doctor doctor : doctors) {
            if (doctor.getName().equalsIgnoreCase(name)) {
                return doctor;
            }
        }
        return null;
    }

    // Sets the next doctor ID after the largest doctor ID currently stored.
    private static void updateNextDoctorId() {
        int largestId = 0;

        for (Doctor doctor : doctors) {
            if (doctor.getId() > largestId) {
                largestId = doctor.getId();
            }
        }

        nextDoctorId = largestId + 1;
    }
}
