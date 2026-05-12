# Project Lifecycle And Class Functions

## Overview

The Clinic Appointment System is a Java Swing desktop application. It manages
patients, doctors, and clinic appointments using model classes, shared lists,
and GUI forms.

## Application Lifecycle

1. The program starts from `clinic.app.Main`.
2. `MainMenuForm` opens and displays the main navigation buttons.
3. The user adds patients through `AddPatientForm`.
4. The user adds doctors through `AddDoctorForm`.
5. The user books appointments through `BookAppointmentForm`.
6. Appointment data is stored in `DataStore.appointments`.
7. The user views appointments by doctor through `ViewAppointmentsForm`.
8. The user searches patient history through `PatientHistoryForm`.
9. The user can complete or cancel appointments from appointment table screens.
10. `DataStore.saveData()` stores patient, doctor, and appointment records in text files.

## Validation Lifecycle

1. GUI forms collect user input from text fields, combo boxes, and tables.
2. Empty fields are checked before data is used.
3. Numeric values are parsed inside `try-catch` blocks.
4. Patient IDs are checked before appointments are booked.
5. Error messages are shown with `JOptionPane`.
6. Valid actions update the shared lists in `DataStore`.

## Class Function Summary

### `clinic.app.Main`

- `main(String[] args)`: Starts the Swing application and opens the main menu.

### `clinic.data.DataStore`

- `loadDefaultDoctors()`: Adds the fixed doctors used by the system.
- `saveData()`: Saves patient, doctor, and appointment data.
- `saveDoctors()`: Writes doctor records to `doctors.txt`.
- `savePatients()`: Writes patient records to `patients.txt`.
- `saveAppointments()`: Writes appointment records to `appointments.txt`.
- `loadData()`: Loads saved patient, doctor, and appointment records.
- `loadDoctors()`: Reads doctors from `doctors.txt`.
- `loadPatients()`: Reads patients from `patients.txt`.
- `loadAppointments()`: Reads appointments from `appointments.txt`.
- `findPatientById(int id)`: Finds a patient by ID.
- `getAppointmentsByDoctor(String doctorName)`: Returns appointments for one doctor.
- `getAppointmentsByPatient(int patientId)`: Returns appointments for one patient.
- `findAppointmentById(int appointmentId)`: Finds an appointment by ID.
- `findDoctorByName(String name)`: Finds a doctor by name.
- `updateNextDoctorId()`: Sets the next doctor ID after the largest stored ID.

### `clinic.model.Patient`

- `Patient(int id, String name, int age, String contact)`: Creates a patient record.
- `toString()`: Returns a short patient label.

### `clinic.model.Doctor`

- `Doctor(int id, String name, String specialization)`: Creates a doctor record.
- `toString()`: Returns the doctor name for combo boxes.

### `clinic.model.Appointment`

- `Appointment(int appointmentId, int patientId, String doctorName, String date, String time)`: Creates a scheduled appointment.
- `toString()`: Returns a readable appointment summary.

### `clinic.ui.MainMenuForm`

- `MainMenuForm()`: Builds the main menu screen.
- `createButton(String text)`: Creates a styled menu button.
- `confirmExit()`: Confirms before closing the application.

### `clinic.ui.AddPatientForm`

- `AddPatientForm()`: Builds the add patient screen.
- `addPatient()`: Validates and saves a patient.
- `clearForm()`: Clears the patient input fields.

### `clinic.ui.AddDoctorForm`

- `AddDoctorForm()`: Builds the add doctor screen.
- `addDoctor()`: Validates and saves a doctor.
- `clearForm()`: Clears the doctor input fields.

### `clinic.ui.BookAppointmentForm`

- `BookAppointmentForm()`: Builds the appointment booking screen.
- `stripTime(Date date)`: Removes time values from a date.
- `generateTimeSlots()`: Creates available appointment times.
- `bookAppointment()`: Validates and saves an appointment.
- `resetForm()`: Clears booking input after saving.

### `clinic.ui.SearchPatientForm`

- `SearchPatientForm()`: Builds the patient search screen.
- `search()`: Searches patients by name or contact.
- `matchesSearch(Patient patient, String keyword)`: Checks whether a patient matches the search.
- `showSearchResults(ArrayList<Patient> matches)`: Displays matching patients in the table.
- `clearSearch()`: Clears the search field and table.

### `clinic.ui.ViewAppointmentsForm`

- `ViewAppointmentsForm()`: Builds the doctor appointments screen.
- `loadAppointments()`: Loads appointments for the selected doctor.
- `updateSelectedAppointment(String newStatus)`: Completes or cancels the selected appointment.

### `clinic.ui.PatientHistoryForm`

- `PatientHistoryForm()`: Builds the patient history screen.
- `searchAppointments()`: Searches appointments by patient ID.
- `loadPatientAppointments(int patientId, boolean showEmptyMessage)`: Loads appointment rows for one patient.
- `updateSelectedAppointment(String newStatus)`: Completes or cancels the selected appointment.
