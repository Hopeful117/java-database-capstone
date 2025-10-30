import { getDoctors, filterDoctors, saveDoctor } from '../service/doctorService.js';
import { createDoctorCard } from '../components/doctorCard.js';
import { openModal } from '../components/modals.js';
document.getElementById('addDocBtn').addEventListener('click', () => {
  openModal('addDoctor');
});
document.addEventListener('DOMContentLoaded', () => {
  loadDoctorCards();
});
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';
    doctors.forEach(doctor => {
      const doctorCard = createDoctorCard(doctor);
      contentDiv.appendChild(doctorCard);
    });
  } catch (error) {
    console.error('Error loading doctors:', error);
  }
}
document.getElementById('searchBar').addEventListener('input', filterDoctorsOnChange);
document.getElementById('timeFilter').addEventListener('change', filterDoctorsOnChange);
document.getElementById('specialtyFilter').addEventListener('change', filterDoctorsOnChange);
async function filterDoctorsOnChange() {
  const name = document.getElementById('searchBar').value || null;
  const time = document.getElementById('timeFilter').value || null;
  const specialty = document.getElementById('specialtyFilter').value || null;
  try {
    const filteredDoctors = await filterDoctors(name, time, specialty);
    if (filteredDoctors.length > 0) {
      renderDoctorCards(filteredDoctors);
    } else {
      const contentDiv = document.getElementById('content');
      contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
    }
  } catch (error) {
    alert('Error filtering doctors: ' + error.message);
  }
}
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  contentDiv.innerHTML = '';
  doctors.forEach(doctor => {
    const doctorCard = createDoctorCard(doctor);
    contentDiv.appendChild(doctorCard);
  });
}
export async function adminAddDoctor() {
  const name = document.getElementById('doctorName').value;
  const email = document.getElementById('doctorEmail').value;
  const phone = document.getElementById('doctorPhone').value;
  const password = document.getElementById('doctorPassword').value;
  const specialty = document.getElementById('doctorSpecialty').value;
  const availabilityElements = document.querySelectorAll('.availability-time:checked');
  const availability = Array.from(availabilityElements).map(elem => elem.value);
  const token = localStorage.getItem('token');
  if (!token) {
    alert('Authentication token not found. Please log in again.');
    return;
  }
  const doctor = {
    name,
    email,
    phone,
    password,
    specialization: specialty,
    availability
  };
  try {
    const response = await saveDoctor(doctor, token);
    if (response.success) {
      alert('Doctor added successfully.');
      document.getElementById('closeModal').click();
      window.location.reload();
    } else {
      alert('Error adding doctor: ' + response.message);
    }
  } catch (error) {
    alert('An error occurred while adding the doctor: ' + error.message);
  }
}
