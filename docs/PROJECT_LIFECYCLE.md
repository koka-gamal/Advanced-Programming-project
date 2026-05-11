# Project Lifecycle

This document explains how the Clinic Appointment System should be built,
tested, integrated, and finalized.

## 1. Requirements

The system is a Java Swing GUI application for basic clinic appointment
management. The required user actions are:

- Add a patient.
- Book an appointment for an existing patient ID.
- View appointments for a selected doctor.
- Search a patient's appointment history.
- Cancel a scheduled appointment.

## 2. Design

The project follows a simple object-oriented structure:

- Model classes store data only.
- `DataStore` holds shared `ArrayList`s.
- GUI forms read from and write to `DataStore`.
- Each form validates user input before changing shared data.

This matches the UML design: the main menu opens forms, forms use `DataStore`,
and appointments connect patients to doctors through patient IDs and doctor
names.

## 3. Implementation Order

1. Create model classes: `Patient`, `Doctor`, and `Appointment`.
2. Create `DataStore` with shared lists and predefined doctors.
3. Build `MainMenuForm` to open each feature screen.
4. Build `AddPatientForm` and validate patient input.
5. Build `BookAppointmentForm` and validate existing patient IDs.
6. Build `ViewAppointmentsForm` and filter appointments by doctor.
7. Build `PatientHistoryForm` and support appointment cancellation.
8. Test each form separately.
9. Merge teammate files and resolve package/import differences.
10. Run final compile and GUI testing.

## 4. Validation Rules

Every form should prevent common user mistakes:

- Empty required fields are rejected.
- Numeric fields are parsed inside `try-catch` blocks.
- Patient IDs are checked with `DataStore.findPatientById`.
- Missing or invalid selections show `JOptionPane` messages.
- Runtime errors should not close the application unexpectedly.

## 5. Testing Flow

Use this manual test path after every integration:

1. Start the app from `clinic.app.Main`.
2. Add a patient and record the generated patient ID.
3. Search for the patient to confirm the record exists.
4. Book an appointment using that patient ID.
5. View appointments for the selected doctor.
6. Open patient history and search by patient ID.
7. Cancel the appointment.
8. Search the same patient again and confirm the status is `Cancelled`.

## 6. Final Delivery

Before submitting the project:

- Confirm all Java files compile from the project root.
- Confirm the GUI opens without console errors.
- Keep screenshots of each required screen.
- Keep teammate code comments readable and consistent.
- Do not add extra features during final integration unless the team agrees.
