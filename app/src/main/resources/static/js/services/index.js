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

window.signupPatient = async function () {
  try {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const address = document.getElementById("address").value;

    const data = { name, email, password, phone, address };
    const { success, message } = await patientSignup(data);
    if (success) {
      alert(message);
      document.getElementById("modal").style.display = "none";
      window.location.reload();
    }
    else alert(message);
  } catch (error) {
    console.error("Signup failed:", error);
    alert("❌ An error occurred while signing up.");
  }
};

window.loginPatient = async function () {
  try {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const data = {
      email,
      password
    }
    console.log("loginPatient :: ", data)
    const response = await patientLogin(data);
    console.log("Status Code:", response.status);
    console.log("Response OK:", response.ok);
    if (response.ok) {
      const result = await response.json();
      console.log(result);
      selectRole('loggedPatient');
      localStorage.setItem('token', result.token)
      window.location.href = '/pages/patientDashboard.html';
    } else {
      alert('❌ Invalid credentials!');
    }
  }
  catch (error) {
    alert("❌ Failed to Login : ", error);
    console.log("Error :: loginPatient :: ", error)
  }


}
