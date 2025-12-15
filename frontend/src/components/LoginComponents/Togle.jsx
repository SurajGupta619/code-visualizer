import React from 'react'
// import log from '../../image/login.gif'

const Togle = ({setIsActive}) => {
  return (
    <div className="toggle-container">
        <div className="toggle">
          <div className="toggle-panel toggle-left">
            <h1>Hello, Friend!</h1>
            <p>Already have an account? Please Sign In</p>
            
            <button
              className="button1"
              id="login"
              type="button"
              onClick={() => setIsActive(false)}
            >
              Sign In
            </button>
          </div>
          <div className="toggle-panel toggle-right">
            <h1>Welcome Back!</h1>
            <p>New to our Visualizer? Please Sign up.</p>
            {/* <img src={log} alt="logicon" width={100}  /> */}
            <button
              className="button2"
              id="register"
              type="button"
              onClick={() => setIsActive(true)}
            >
              Sign Up
            </button>
          </div>
        </div>
      </div>
  )
}

export default Togle
