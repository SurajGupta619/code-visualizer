import React, { createContext, useContext, useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);
  const [userName, setUserName] = useState("");
  const [name,setName] = useState("");
  const [showAuthModal, setShowAuthModal] = useState(false);

  const navigate = useNavigate();

  // Restore token & username on refresh
  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedUserName = localStorage.getItem("userName");
    const storedName = localStorage.getItem("name")

    if (storedToken) setToken(storedToken);
    if (storedUserName) setUserName(storedUserName);
    if (storedName) setName(storedName)

    setLoading(false);
  }, []);

  // Save username manually
  const saveUserName = (userName) => {
    localStorage.setItem("userName", userName);
    setUserName(userName);
  };
  const getUserName = () => {
    return localStorage.getItem('userName')
  }
  const register = (userName) => {
    localStorage.setItem("userName", userName);
    setUserName(userName);
  };

  //name save
  const saveName = (name) => {
    localStorage.setItem("name",name);
    setName(name);
  }

  // Login and store token + login time
  const login = (jwtToken, userName, name) => {
    localStorage.setItem("token", jwtToken);
    const now = Date.now();
    localStorage.setItem("name",name);
    localStorage.setItem("loginTime", now.toString());
    localStorage.setItem("lastActivityTime", now.toString());
    setToken(jwtToken);
    setUserName(userName);
    setName(name);
  };


  // Logout
  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("userName");
    localStorage.removeItem("name");
    localStorage.removeItem("loginTime");
    localStorage.removeItem("lastActivityTime");
    setUserName("");
    setToken(null);

    toast.error("You have been logged out.", { duration: 3000 });

    navigate("/");
  }, [navigate]);

  //  Logout only when the user closes the tab/browser, not on refresh
useEffect(() => {
  const handleBeforeUnload = () => {
    // Mark unloading so next load can detect if it's a refresh
    localStorage.setItem("pageUnloading", "true");
    localStorage.setItem("unloadTime", Date.now().toString());
  };

  window.addEventListener("beforeunload", handleBeforeUnload);

  return () => window.removeEventListener("beforeunload", handleBeforeUnload);
}, []);

useEffect(() => {
  const unloadFlag = localStorage.getItem("pageUnloading");
  const unloadTime = localStorage.getItem("unloadTime");

  // Clear the flag immediately (since page loaded)
  localStorage.removeItem("pageUnloading");

  if (unloadFlag && unloadTime) {
    const timeDiff = Date.now() - parseInt(unloadTime, 10);

    // If page reloads within 2 seconds → it was a refresh
    if (timeDiff > 3000) {
      //  tab/browser close → logout
      logout();
    }
  }
}, [logout]);

  // Auto logout on inactivity with toast 10s before
  useEffect(() => {
    if (!token) return;

    const sessionDuration = 30 * 60 * 1000; // 30 min
    // const warningBefore = 5 * 1000; // 10s before logout

    const resetActivity = () => {
      localStorage.setItem("lastActivityTime", Date.now().toString());
    };

    const checkSession = () => {
      const lastActivity = localStorage.getItem("lastActivityTime");
      const loginTime = localStorage.getItem("loginTime");
      if (!lastActivity || !loginTime) return;

      const now = Date.now();
      const elapsed = now - parseInt(lastActivity, 10);
   

      if (elapsed >= sessionDuration) {
        logout();
      }
    };

    // Listen to user activity
    window.addEventListener("mousemove", resetActivity);
    window.addEventListener("keydown", resetActivity);
    window.addEventListener("click", resetActivity);
    window.addEventListener("scroll", resetActivity);

    // Check session every second
    const interval = setInterval(checkSession, 1000);

    // Initialize activity
    resetActivity();

    return () => {
      clearInterval(interval);
      window.removeEventListener("mousemove", resetActivity);
      window.removeEventListener("keydown", resetActivity);
      window.removeEventListener("click", resetActivity);
      window.removeEventListener("scroll", resetActivity);
    };
  }, [token, logout]);

  if (loading) return <div>Loading...</div>;

  return (
    <AuthContext.Provider
      value={{
        token,
        isAuthenticated: !!token,
        login,
        logout,
        register,
        saveUserName,
        getUserName,
        loading,
        userName,
        saveName,
        name,
        showAuthModal,
        setShowAuthModal,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);