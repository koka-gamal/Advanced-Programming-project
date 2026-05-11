# Clinic Appointment System

A simplified Java Swing application for managing clinic patients and appointments.

The system lets users add patients, book appointments with predefined doctors,
view appointments by doctor, search patient history, and cancel scheduled
appointments. The project is intentionally lightweight: shared data is held in
`ArrayList`s inside `DataStore`, with a small text-file backup for local testing.

## Project Structure

```text
Clinic Appointment System
|-- src/
|   `-- clinic/
|       |-- app/
|       |   `-- Main.java
|       |-- data/
|       |   `-- DataStore.java
|       |-- model/
|       |   |-- Appointment.java
|       |   |-- Doctor.java
|       |   `-- Patient.java
|       `-- ui/
|           |-- AddPatientForm.java
|           |-- BookAppointmentForm.java
|           |-- MainMenuForm.java
|           |-- PatientHistoryForm.java
|           |-- SearchPatientForm.java
|           `-- ViewAppointmentsForm.java
|-- data/
|   |-- appointments.txt
|   `-- patients.txt
|-- docs/
|   |-- INTEGRATION_GUIDE.md
|   `-- PROJECT_LIFECYCLE.md
`-- build/
    `-- classes/
```

## Main Parts

`clinic.app.Main` starts the application and opens the main menu.

`clinic.data.DataStore` is the shared storage layer. It owns the public static
lists for patients, doctors, and appointments, plus helper search methods used
by the GUI screens.

`clinic.model` contains the simple object classes:

- `Patient`: patient ID, name, age, and contact.
- `Doctor`: predefined doctor ID, name, and specialization.
- `Appointment`: appointment ID, patient ID, doctor name, date, time, and status.

`clinic.ui` contains all Swing windows:

- `MainMenuForm`: opens the other screens.
- `AddPatientForm`: validates and stores new patients.
- `BookAppointmentForm`: validates patient ID and books appointments.
- `ViewAppointmentsForm`: filters appointments by doctor.
- `PatientHistoryForm`: searches patient appointments and cancels scheduled ones.
- `SearchPatientForm`: optional helper for finding patient IDs.

## Compile And Run

From the project root:

```powershell
javac -d build\classes (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp build\classes clinic.app.Main
```

## Data Rules

- Patients are stored in `DataStore.patients`.
- Doctors are predefined in `DataStore.doctors`.
- Appointments are stored in `DataStore.appointments`.
- New appointments start with status `Scheduled`.
- Cancelled appointments change status to `Cancelled`.

The `data/` text files are only a simple local backup. They are not a database,
and they can be cleared during testing if the team wants a fresh run.
