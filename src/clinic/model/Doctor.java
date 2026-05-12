package clinic.model;

// Represents one predefined clinic doctor.
public class Doctor {
    private int id;
    private String name;
    private String specialization;

    // Creates a doctor record used by appointment booking and doctor filtering.
    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Shows the doctor's name inside Swing combo boxes.
    @Override
    public String toString() {
        return name;
    }
}
