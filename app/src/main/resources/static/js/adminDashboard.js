import {getDoctors,filterDoctors,saveDoctor } from '../js/services/doctorServices.js';
import {createDoctorCard} from '../js/components/doctorCard.js';
import {openModal} from '../js/components/modals.js';

document.getElementById('addDocBtn').addEventListener('click', () => {
  openModal('addDoctor');
});
document.addEventListener('DOMContentLoaded', () => {
  loadDoctorCards();
});


async function loadDoctorCards() {
  const specialtySet=new Set();
  const timeSet=new Set();
  
  try {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';
    doctors.forEach(doctor => {
      const doctorCard = createDoctorCard(doctor);
      contentDiv.appendChild(doctorCard);
      doctor.specialty && specialtySet.add(doctor.specialty);
      doctor.availableTimes && doctor.availableTimes.forEach(time => timeSet.add(time));
     
    });
     timeSet.forEach(time => {
      const option = document.createElement('option');
      option.value = time;
      option.textContent = time;
      document.getElementById('timeFilter').appendChild(option);
        
    });
      specialtySet.forEach(specialty => {
        const option = document.createElement('option');
        option.value = specialty;
        option.textContent = specialty;
        document.getElementById('specialtyFilter').appendChild(option);
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
    const response = await filterDoctors(name, time, specialty);
    const filteredDoctors = response.doctors || [];
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
  const first_name = document.getElementById('doctorFirstName').value;
  const last_name = document.getElementById('doctorLastName').value;
  const email = document.getElementById('doctorEmail').value;
  const phone = document.getElementById('doctorPhone').value;
  const password = document.getElementById('doctorPassword').value;
  const specialty = document.getElementById('specialization').value;
  const availabilityElements = document.querySelectorAll('.availability-time:checked');
  const availability = Array.from(availabilityElements).map(elem => elem.value);
  const token = localStorage.getItem('token');
  if (!token) {
    alert('Authentication token not found. Please log in again.');
    return;
  }
  const doctor = {
    first_name:first_name,
    last_name:last_name,
    email:email,
    phone:phone,
    password:password,
    specialty:specialty,
    availability:availability
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
window.adminAddDoctor = adminAddDoctor;
