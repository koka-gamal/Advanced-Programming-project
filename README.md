# Clinic Appointment System

A Java Swing application for managing clinic patients and appointments.

The system supports adding patients, booking appointments with predefined
doctors, viewing appointments by doctor, searching patient history, and
cancelling appointments.

## Project Structure

```text
Clinic Appointment System
|-- run_project.bat
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
|   `-- PROJECT_LIFECYCLE.md
`-- build/
    `-- classes/
```

## How To Run

Double-click:

```text
run_project.bat
```

The batch file compiles the source files, then starts the application.

Manual commands:

```cmd
javac -d build\classes src\clinic\app\Main.java src\clinic\data\DataStore.java src\clinic\model\*.java src\clinic\ui\*.java
java -cp build\classes clinic.app.Main
```

## Main Features

- Add a new patient with name, age, and contact.
- Search patients by name or contact.
- Book an appointment using an existing patient ID.
- View appointments for a selected doctor.
- Search appointment history by patient ID.
- Cancel an appointment by changing its status to `Cancelled`.

## Data Rules

- Patients are stored in `DataStore.patients`.
- Doctors are stored in `DataStore.doctors`.
- Appointments are stored in `DataStore.appointments`.
- New appointments start with status `Scheduled`.
- Cancelled appointments use status `Cancelled`.
- Text files in `data/` store records between runs.
