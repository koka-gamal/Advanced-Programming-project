package clinic.model;

/**
 * Stores one clinic appointment.
 *
 * The appointment connects a patient ID with a doctor name, date, time, and
 * status. The project only needs "Scheduled" and "Cancelled" statuses.
 */
public class Appointment {
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_CANCELLED = "Cancelled";

    private int appointmentId;
    private int patientId;
    private String doctorName;
    private String date;
    private String time;
    private String status;

    /**
     * Creates a new appointment and starts it as Scheduled.
     */
    public Appointment(int appointmentId, int patientId, String doctorName,
                       String date, String time) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.status = STATUS_SCHEDULED;
    }

    /**
     * Returns the unique appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Updates the appointment ID.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Returns the patient ID attached to this appointment.
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Updates the patient ID attached to this appointment.
     */
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the doctor's name for display and filtering.
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * Updates the doctor's name for this appointment.
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * Returns the appointment date as text in dd/MM/yyyy format.
     */
    public String getDate() {
        return date;
    }

    /**
     * Updates the appointment date text.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the selected appointment time.
     */
    public String getTime() {
        return time;
    }

    /**
     * Updates the selected appointment time.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns whether the appointment is Scheduled or Cancelled.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the appointment status after a user cancels it.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Shows a readable appointment summary for debugging.
     */
    @Override
    public String toString() {
        return appointmentId + " | Patient " + patientId + " | " + doctorName
            + " | " + date + " " + time + " | " + status;
    }
}
