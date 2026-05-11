package clinic.model;

/**
 * Represents one predefined clinic doctor.
 *
 * Doctors are created once in DataStore so every form uses the same list and
 * the booking/view screens stay consistent.
 */
public class Doctor {
    private int id;
    private String name;
    private String specialization;

    /**
     * Creates a doctor with an ID, display name, and medical specialization.
     */
    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    /**
     * Returns the unique doctor ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the doctor ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the doctor's display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the doctor's display name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the doctor's specialization.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Updates the doctor's specialization.
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Shows the doctor's name inside Swing combo boxes.
     */
    @Override
    public String toString() {
        return name;
    }
}
