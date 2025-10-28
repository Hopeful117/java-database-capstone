## MySQL Database Design

### Patients table
- patient_id: INT, PRIMARY KEY, AUTO_INCREMENT
- first_name: VARCHAR(100), NOT NULL
- last_name: VARCHAR(100), NOT NULL
- email: VARCHAR(255), NOT NULL, UNIQUE
- phone: VARCHAR(20),NOT NULL
- date_of_birth: DATE,NULL
- gender: ENUM('Male','Female'),NULL
- password: VARCHAR(255),NOT NULL

**Notes**:

If a patient is deleted, their appointments should also be removed (ON DELETE CASCADE) to maintain consistency.

Email uniqueness prevents duplicate accounts.

### Doctors table
- doctor_id: INT, PRIMARY KEY, AUTO_INCREMENT
- first_name: VARCHAR(100), NOT NULL
- last_name: VARCHAR(100), NOT NULL
- specialization: VARCHAR(150), NOT NULL
- email: VARCHAR(255), NOT NULL , UNIQUE
- phone: VARCHAR(20),NOT NULL
- password: VARCHAR(255),NOT NULL
- availability: JSON / TEXT NULL

**Notes**:

Doctors can mark unavailability; overlapping appointments should be prevented via business logic (checked before insert/update).

### Appointements table
- appointement_id: INT, PRIMARY KEY, AUTO_INCREMENT
- patient_id: INT, FOREIGN KEY ,ON DELETE CASCADE
- doctor_id: INT, FOREIGN KEY ,ON DELETE CASCADE
- appointement_date: DATETIME, NOT NULL
- duration_minutes: INT, DEFAULT 60
- status: ENUM('Scheduled','Completed','Cancelled'), DEFAULT 'Scheduled'
- location_id: INT, FOREIGN KEY ,ON DELETE CASCADE

**Notes**:

No overlapping appointments for the same doctor — enforce via application logic or unique constraint (doctor_id + appointment_date).

If a patient is deleted, their appointments are automatically removed (ON DELETE CASCADE).

### Admin table
- admin_id: INT, PRIMARY KEY,AUTO_INCREMENT
- username: VARCHAR(100), NOT NULL, UNIQUE
- password: VARCHAR(255), NOT NULL
- email: VARCHAR(255), NOT NULL ,UNIQUE

**Notes**:

Admins can manage doctors, patients, and view statistics (via stored procedures).

### Clinic Locations table
- location_id: INT, PRIMARY KEY,AUTO_INCREMENT
- name: VARCHAR(100), NOT NULL
- address: VARCHAR(255), NOT NULL
- city: VARCHAR(100), NOT NULL
- phone: VARCHAR(20), NULL

### Payments table
- payement_id: INT, PRIMARY KEY,AUTO_INCREMENT
- appointment_id: INT, FOREIGN KEY,ON DELETE CASCADE
- amount: DECIMAL(10,2), NOT NULL
- payment_date: DATETIME, DEFAULT CURRENT_TIMESTAMP
- method: ENUM('Card','Cash','Online'), NOT NULL
- status: ENUM('Pending','Completed','Failed'),DEFAULT 'Pending'

**Notes**:

Deleting an appointment also deletes related payment records.

### General Constraints & Business Rules
- NOT NULL for all required fields (IDs, names, email, password, dates).

- AUTO_INCREMENT for all primary keys.

- UNIQUE for email fields and usernames.

- ON DELETE CASCADE for dependent records (appointments, payments).

- No overlapping appointments per doctor enforced at service layer.

- Validation for email/phone done via backend code (not database constraints).

## MongoDB Collection Design

### Collection presciptions
{
  "_id": { "$oid": "653f1b8e9f1a4c3b2a000001" },
  "prescriptionId": "RX-2025-000123",         // optional human-friendly id
  "patientId": 341,                           // reference to MySQL patients.patient_id
  "patientSnapshot": {                        // optional denormalized snapshot for quick reads
    "firstName": "Marie",
    "lastName": "Dupont",
    "dob": "1985-06-12",
    "phone": "+33 6 12 34 56 78"
  },
  "doctorId": 12,                             // reference to MySQL doctors.doctor_id
  "appointmentId": 987,                       // reference to MySQL appointments.appointment_id
  "issuedAt": { "$date": "2025-10-01T09:30:00Z" },
  "validUntil": { "$date": "2026-04-01T00:00:00Z" },
  "medications": [
    {
      "name": "Amoxicillin",
      "dose": "500mg",
      "frequency": "3 times a day",
      "durationDays": 7,
      "instructions": "Take after meals",
      "tags": ["antibiotic", "oral"]
    },
    {
      "name": "Paracetamol",
      "dose": "500mg",
      "frequency": "every 6 hours as needed",
      "durationDays": 5,
      "instructions": "Max 3g / day",
      "overTheCounter": true
    }
  ],
  "prescriptionNotes": {
    "freeForm": "Patient reports penicillin allergy; prescribed amoxicillin with monitoring.",
    "structuredWarnings": ["Allergy: Penicillin"],
    "icd10Codes": ["J01.90"]
  },
  "pharmacy": {
    "name": "Pharmacie Centrale",
    "address": "12 Rue de la Santé, Paris",
    "phone": "+33 1 23 45 67 89"
  },
  "attachments": [
    {
      "fileId": "obj_5f8d0c",
      "filename": "lab_result_2025-09-30.pdf",
      "mimeType": "application/pdf",
      "uploadedAt": { "$date": "2025-09-30T12:00:00Z" },
      "metadata": { "pages": 2, "sizeBytes": 524288 }
    }
  ],
  "audit": {
    "createdBy": { "userId": 12, "role": "doctor" },
    "createdAt": { "$date": "2025-10-01T09:30:00Z" },
    "lastModifiedAt": { "$date": "2025-10-01T09:45:00Z" },
    "modifiedBy": { "userId": 12, "role": "doctor" }
  },
  "tags": ["urgent", "follow-up"],
  "metadata": {
    "schemaVersion": 1,
    "source": "web-portal",
    "confidenceScore": 0.98
  }
}
