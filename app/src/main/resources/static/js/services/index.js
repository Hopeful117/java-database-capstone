import { openModal } from '../util/modal.js';
import { BASE_API_URL } from '../config/config.js';

const ADMIN_API = `${BASE_API_URL}/api/admin`;
const DOCTOR_API = `${BASE_API_URL}/api/doctor`;
window.onload = function() {
  const adminLoginBtn = document.getElementById('adminLogin');
  const doctorLoginBtn = document.getElementById('doctorLogin');

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
}
window.adminLoginHandler = async function() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  const admin = {
    username: username,
    password: password
  };

  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(admin)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('admin');
    } else {
      alert('Invalid admin credentials. Please try again.');
    }
  } catch (error) {
    alert('An error occurred during admin login. Please try again later.');
  }
}

window.doctorLoginHandler = async function() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  const doctor = {
    email: email,
    password: password
  };

  try {
    const response = await fetch(DOCTOR_API, {
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
