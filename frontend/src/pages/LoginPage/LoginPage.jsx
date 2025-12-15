import React, { useState } from "react";
import Login from "../../components/LoginComponents/Login";
import Togle from "../../components/LoginComponents/Togle";
import Register from "../../components/LoginComponents/Register";
import "./Auth.css";


export default function AuthPage({closeAuthModal}) {
  const [isActive, setIsActive] = useState(false);

  return (
    <div className={isActive ? "container active" : "container"} id="container">
      <Register/>
      <Login closeAuthModal={closeAuthModal}/>
      <Togle setIsActive = {setIsActive}/>
    </div>
  );
}