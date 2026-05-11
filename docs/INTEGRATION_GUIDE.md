# Integration Guide

Use this guide when adding files created by other team members.

## Package Locations

Place files in these folders:

- `src/clinic/model`: data classes such as `Patient`, `Doctor`, `Appointment`.
- `src/clinic/data`: shared storage such as `DataStore`.
- `src/clinic/ui`: Swing forms such as `BookAppointmentForm`.
- `src/clinic/app`: the application entry point.

Each file must start with the correct package line:

```java
package clinic.model;
package clinic.data;
package clinic.ui;
package clinic.app;
```

Only use one package line per file, based on the folder.

## Shared Data Contract

All members should use these shared lists:

```java
DataStore.patients
DataStore.doctors
DataStore.appointments
```

Important helper methods:

```java
DataStore.findPatientById(int id)
DataStore.getAppointmentsByDoctor(String doctorName)
DataStore.getAppointmentsByPatient(int patientId)
DataStore.findAppointmentById(int appointmentId)
DataStore.saveData()
```

Do not create separate patient or appointment lists inside forms. Forms should
read from and write to `DataStore`.

## Incoming Appointment Files

Expected teammate files:

- `Appointment.java`
- `BookAppointmentForm.java`

Integration target:

- `Appointment.java` goes in `src/clinic/model`.
- `BookAppointmentForm.java` goes in `src/clinic/ui`.

Allowed cleanup while integrating:

- Add the correct `package` line.
- Add missing `import` statements.
- Rename variables to match the shared style.
- Add or improve comments.
- Preserve the teammate's booking logic unless it conflicts with the shared
  `DataStore` contract.

Required behavior:

- Doctor dropdown exists.
- Patient ID field exists.
- Date and time inputs exist.
- Empty patient ID is rejected.
- Non-numeric patient ID is rejected.
- Unknown patient ID is rejected.
- Valid appointments are added to `DataStore.appointments`.
- Successful and failed actions use `JOptionPane` messages.

## Incoming Patient History File

Expected teammate file:

- `PatientHistoryForm.java`

Integration target:

- `PatientHistoryForm.java` goes in `src/clinic/ui`.

Allowed cleanup while integrating:

- Add the correct `package` line.
- Add missing `import` statements.
- Rename variables for clarity.
- Add comments.
- Keep the search/cancel flow intact unless it breaks the shared contract.

Required behavior:

- Search by patient ID.
- Display patient appointments in a `JTable`.
- Include a cancel appointment button.
- Change selected appointment status to `Cancelled`.
- Refresh the table after cancellation.

## Naming Style

Use these naming rules to reduce merge problems:

- Classes use `PascalCase`: `BookAppointmentForm`.
- Variables and methods use `camelCase`: `patientId`, `loadAppointments`.
- Swing fields use readable prefixes: `txtPatientId`, `cmbDoctor`, `btnCancel`.
- Constants use uppercase: `STATUS_CANCELLED`.

## Merge Checklist

After adding a teammate file:

1. Check the package line matches the folder.
2. Check imports point to `clinic.data` and `clinic.model`.
3. Confirm the file uses `DataStore` instead of local shared lists.
4. Compile all source files.
5. Run the manual test flow in `PROJECT_LIFECYCLE.md`.
6. Add comments only where they make the logic easier to understand.
