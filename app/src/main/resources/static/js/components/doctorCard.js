import { getPatientData } from "/js/services/patientServices.js";
import { deleteDoctor } from "/js/services/doctorServices.js";

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.className = "doctor-card";

  const userRole = localStorage.getItem("userRole");

  const infoContainer = document.createElement("div");
  infoContainer.className = "doctor-info";

  const nameElem = document.createElement("h3");
  nameElem.textContent = `Dr. ${doctor.last_name}`;

  const specializationElem = document.createElement("p");
  specializationElem.textContent = `Specialization: ${doctor.specialty}`;

  const emailElem = document.createElement("p");
  emailElem.textContent = `Email: ${doctor.email}`;

  const availabilityElem = document.createElement("p");
  availabilityElem.textContent = `Available Slots: ${doctor.availableTimes.join(", ")}`;

  infoContainer.appendChild(nameElem);
  infoContainer.appendChild(specializationElem);
  infoContainer.appendChild(emailElem);
  infoContainer.appendChild(availabilityElem);

  const actionContainer = document.createElement("div");
  actionContainer.className = "card-actions";

  if (userRole === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete Doctor";
    deleteBtn.className = "delete-btn";
    deleteBtn.onclick = async () => {
      const token = localStorage.getItem("token");
      const response = await deleteDoctor(doctor.id, token);
      if (response.success) {
        alert("Doctor deleted successfully.");
        card.remove();
      } else {
        alert(`Error deleting doctor: ${response.message}`);
      }
    };
    actionContainer.appendChild(deleteBtn);
  } else if (userRole === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";
    bookBtn.onclick = () => {
      alert("Please log in to book an appointment.");
    };
    actionContainer.appendChild(bookBtn);
  } else if (userRole === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";
    bookBtn.onclick = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }
      const patientResponse = await getPatientData(token);
      if (patientResponse.success) {
        const patient = patientResponse.data;
        openBookingOverlay(doctor, patient);
      } else {
        alert(`Error fetching patient details: ${patientResponse.message}`);
      }
    };
    actionContainer.appendChild(bookBtn);
  }

  card.appendChild(infoContainer);
  card.appendChild(actionContainer);

  return card;
}

