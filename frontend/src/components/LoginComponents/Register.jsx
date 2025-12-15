import React, { useState } from 'react';
import toast from 'react-hot-toast';
import http from "../../auth/HttpClient";
import { useNavigate } from 'react-router-dom';
// import log from '../../image/registered.gif'
import zxcvbn from 'zxcvbn';

const Register = () => {
  const [name, setName] = useState("");
  const [username, setUsername] = useState(""); // Emp ID
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [passwordMatch, setPasswordMatch] = useState(false);
  const navigate=useNavigate();


  const checkPasswordStrength = (pass) => {
    const minLength = /.{8,}/;
    const upperCase = /[A-Z]/;
    const lowerCase = /[a-z]/;
    const number = /[0-9]/;
    const specialChar = /[!@#$%^&*(),.?":{}|<>]/;

    let strength = 0;
    if (minLength.test(pass)) strength++;
    if (upperCase.test(pass)) strength++;
    if (lowerCase.test(pass)) strength++;
    if (number.test(pass)) strength++;
    if (specialChar.test(pass)) strength++;

    return strength;
  };

  //  Handlers
  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);

    const evaluation = zxcvbn(value);
    setPasswordStrength(evaluation.score);
    // setPasswordStrength(checkPasswordStrength(value));
     setPasswordMatch(value === confirmPassword);
  };

  const handleConfirmPasswordChange = (e) => {
    const value = e.target.value;
    setConfirmPassword(value);
    setPasswordMatch(value === password);
  };

  

  const register = async () => {
    if (!name || !username || !password || !confirmPassword) {
      toast.error("Please fill all fields!");
      return;
    }

    if (!passwordMatch) {
      toast.error("Passwords do not match!");
      return;
    }

    if (passwordStrength < 4) {
      toast.error("Password is too weak!");
      return;
    }

    try {
      await http.post("/api/auth/register", {
        name,
        username,
        password,
      });

      toast.success(`${name} registered successfully 🎉`);
      setTimeout(() => {
        navigate("/");
      }, 100);
      

      // Reset fields
      setName("");
      setUsername("");
      setPassword("");
      setConfirmPassword("");
      setPasswordStrength(0);
      setPasswordMatch(false);
    } catch (err) {
      toast.error(err.response?.data?.error || "Registration failed!");
      // console.error(err);
    }
  };

  //  Get progress bar color based on strength
  const getStrengthColor = () => {
    switch (passwordStrength) {
      case 0:
      case 1:
      case 2:
        return "red";
      case 3:
      case 4:
        return "#f7b731"; // yellow
      case 5:
        return "green";
      default:
        return "transparent";
    }

  
  };

  return (
    <div className="form-container sign-up">
      {/* Global toast renderer */}
      {/* <Toaster position="top-right" /> */}

      <form>
       {/* <img src={log} alt="logicon" width={50} /> */}
        <h1>Sign up</h1>
        <br />

        <input
          type="text"
          placeholder="User name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <input
          type="email"
          placeholder="EmpId@tcs.com"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <div style={{ position: "relative", width: "100%" }}>
                    <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={handlePasswordChange}
              style={{
                borderColor:
                  passwordStrength === 1 ? "red" :
                  passwordStrength === 1 ? "orange" :
                  passwordStrength === 2 ? "#f7b731" :
                  passwordStrength === 3 ? "green" :
                  "#ccc"
              }}
            />
          {/* <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={handlePasswordChange}
            style={{
              borderColor:
                passwordStrength <= 2 && password
                  ? "red"
                  : passwordStrength === 3 || passwordStrength === 4
                  ? "#f7b731"
                  : passwordStrength === 5
                  ? "green"
                  : "#ccc",
            }}
          /> */}
          {/* Password strength bar */}
          {password && (
            <div
              style={{
                height: "4px",
                borderRadius: "4px",
                background: "#ddd",
                width: "100%",
                marginTop: "4px",
                overflow: "hidden",
              }}
            >
              <div
                style={{
                  height: "100%",
                  width: `${(passwordStrength / 5) * 100}%`,
                  background: getStrengthColor(),
                  transition: "width 0.3s ease",
                }}
              ></div>
            </div>
          )}
        </div>

        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={handleConfirmPasswordChange}
          style={{
            borderColor:
              confirmPassword && !passwordMatch ? "red" : passwordMatch ? "green" : "#ccc",
          }}
        />

        {/* Password match message */}
        {confirmPassword && (
          <p
            style={{
              fontSize: "0.85rem",
              marginTop: "-6px",
              color: passwordMatch ? "green" : "red",
            }}
          >
            {passwordMatch ? "✅ Passwords match" : "❌ Passwords do not match"}
          </p>
        )}
        <br />

        <button type="submit" className="btn1" onClick={register}>
          Sign Up
        </button>
      </form>
    </div>
  );
};

export default Register;