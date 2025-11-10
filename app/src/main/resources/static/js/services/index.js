import {openModal} from '../components/modals.js';
import {API_BASE_URL} from '../config/config.js';
import { patientLogin } from './patientServices.js';



const ADMIN_API = `${API_BASE_URL}/admin`;
const DOCTOR_API = `${API_BASE_URL}/doctor`;
const PATIENT_API = `${API_BASE_URL}/patient`;
window.onload = function() {
  const adminLoginBtn = document.getElementById('adminLogin');
  const doctorLoginBtn = document.getElementById('doctorLogin');
  const patientSelectBtn = document.getElementById('patientSelect');
  const patientLoginBtn = document.getElementById('patientLoginBtn');
  const patientSignupBtn = document.getElementById('patientSignupBtn');
  

  if (adminLoginBtn) {
    adminLoginBtn.addEventListener('click', () => {
      openModal('adminLogin');
    });
  }

  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener('click', () => {
      openModal('doctorLogin');
    });
  }
  if (patientSelectBtn) {
    patientSelectBtn.addEventListener('click', () => {
      openModal('patientSelect');
    });
  
  }
}

export async function adminLoginHandler() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  const admin = { username, password };

  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(admin)
    });

    const data = await response.json(); // ✅ On parse JSON à tous les coups

    if (response.ok && data.token) {
      localStorage.setItem('token', data.token);
      selectRole('admin');
    } else {
      alert(data.message || 'Invalid credentials');
    }

  } catch (error) {
    console.error('Login error:', error);
    alert('An error occurred during admin login. Please try again later.');
  }
}

window.adminLoginHandler = adminLoginHandler;


 export  async function doctorLoginHandler() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  const doctor = {
    email: email,
    password: password
  };

  try {
    const response = await fetch(DOCTOR_API+"/login", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(doctor)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('doctor');
    } else {
      alert('Invalid doctor credentials. Please try again.');
    }
  } catch (error) {
    console.error('Error during doctor login:', error);
    alert('An error occurred during doctor login. Please try again later.');
  }
}
window.doctorLoginHandler = doctorLoginHandler;

export async function patientLoginHandler() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  const patient = {
    email: email,
    password: password
  };
try{
  const response = await patientLogin(patient);
  if (response.ok) {
    const data = await response.json();
    localStorage.setItem('token', data.token);
    selectRole('loggedPatient');
  } else {
    alert('Invalid patient credentials. Please try again.');
  } 
}
catch (error) {
    console.error('Error during patient login:', error);
    alert('An error occurred during patient login. Please try again later.');
  }
}
window.patientLoginHandler = patientLoginHandler;
