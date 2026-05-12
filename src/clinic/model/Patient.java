package clinic.model;

// Stores the basic information for one clinic patient.
public class Patient {
    private int id;
    private String name;
    private int age;
    private String contact;

    // Creates a patient record with the ID and details entered in the form.
    public Patient(int id, String name, int age, String contact) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // Displays a short patient label in combo boxes, tables, or debug output.
    @Override
    public String toString() {
        return "ID: " + id + " | " + name;
    }
}
