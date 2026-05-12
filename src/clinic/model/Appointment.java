package clinic.model;

// Stores one clinic appointment.
public class Appointment {
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_CANCELLED = "Cancelled";

    private int appointmentId;
    private int patientId;
    private String doctorName;
    private String date;
    private String time;
    private String status;

    // Creates a new appointment and starts it as Scheduled.
    public Appointment(int appointmentId, int patientId, String doctorName,
                       String date, String time) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.status = STATUS_SCHEDULED;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Displays a readable appointment summary.
    @Override
    public String toString() {
        return appointmentId + " | Patient " + patientId + " | " + doctorName
            + " | " + date + " " + time + " | " + status;
    }
}
