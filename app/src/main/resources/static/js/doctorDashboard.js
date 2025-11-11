import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

const tableBody = document.getElementById("patientTableBody");
let selectedDate = null;// YYYY-MM-DD
const token = localStorage.getItem("token");
let patientName = null; // For filtering by patient name

// Search bar event listener
const searchBar = document.getElementById("searchBar");
searchBar.addEventListener("input", (event) => {
  const input = event.target.value.trim();
  if (input) {
    patientName = input;
  } else {
    patientName = "null"; // Backend expects "null" for no filter
  }
  loadAppointments();
});

// Today button event listener
const todayBtn = document.getElementById("todayButton");
todayBtn.addEventListener("click", () => {
  selectedDate = new Date().toISOString().split("T")[0];
  document.getElementById("datePicker").value = selectedDate;
  loadAppointments();
});

// Date picker event listener
const datePicker = document.getElementById("datePicker");
datePicker.addEventListener("change", (event) => {
  if(event.target.value === "") {
    selectedDate = null;
  } else {
  selectedDate = new Date(event.target.value).toISOString().split("T")[0];
  loadAppointments();
}});

// Function to load appointments based on selected date and patient name
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);
    tableBody.innerHTML = ""; // Clear existing rows

    if (appointments.length === 0) {
      const noDataRow = document.createElement("tr");
      noDataRow.innerHTML = `<td colspan="5">No Appointments found for today.</td>`;
      tableBody.appendChild(noDataRow);
      return;
    }

    appointments.forEach((appointment) => {
      const patient = {
        id: appointment.patient.patientId,
        name: appointment.patient.last_name,
        phone: appointment.patient.phone,
        email: appointment.patient.email,
      };
      const row = createPatientRow(patient,appointment.id,appointment.doctor.id);
      tableBody.appendChild(row);
    });
  } catch (error) {
    const errorRow = document.createElement("tr");
    errorRow.innerHTML = `<td colspan="5">Error loading appointments. Try again later.</td>`;
    tableBody.appendChild(errorRow);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  renderContent(); // Assuming this function sets up the UI layout
  loadAppointments(); // Load today's appointments on page load
});

