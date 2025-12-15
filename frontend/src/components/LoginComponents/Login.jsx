import React, {useState} from 'react'
import toast from 'react-hot-toast';
import http from "../../auth/HttpClient"
import { useNavigate } from 'react-router-dom';
import log from '../../image/login.gif'

import { useAuth } from '../../auth/AuthContext';
const LoginForm = ({setToken,closeAuthModal}) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate()
    const {login} =useAuth()


    const handleLogin = async (e) => {
      e.preventDefault()
        try {
          const data = { "username": email, "password": password }
          const res = await http.post("/api/auth/login", data);
          toast.success("Login Successfull")
          if(res.data.message === "Login successful" ){
            const {name,token,username} = res.data
            login(token,username,name)
            navigate("/");
            closeAuthModal();
          } 
        } catch (err) {
          toast.error(err.response.data.error)
        }
      };
    
  return (
      <div className="form-container sign-in">
      <form>
        <img src={log} alt="logicon" width={100}  />
        <h1>Sign In</h1><br/>
        <br/>
        <input type="email" placeholder="EmpId@tcs.com" value={email} onChange={(e) => {setEmail(e.target.value)}} />
        <input type="password" placeholder="Password" value={password} onChange={(e) => {setPassword(e.target.value)}} />
        
        <button type="submit" className='btn2' onClick={handleLogin}>Sign In</button>
      </form>
    </div>
    )
  }


export default LoginForm
