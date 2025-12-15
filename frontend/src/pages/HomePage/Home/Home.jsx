import React, { useState } from "react";
import Navbar from "../../../components/HomeComponents/Navbar";
import Hero from "../../../components/HomeComponents/Hero";
import Footer from "../../../components/HomeComponents/Footer";
import Card from "../../../components/HomeComponents/Card";
import AuthPage from "../../LoginPage/LoginPage"; // adjust path if needed
import {  useAuth } from "../../../auth/AuthContext";
import { motion } from "framer-motion";
import { FaCode } from "react-icons/fa";
import { LuBookOpen } from "react-icons/lu";
import "./Home.css";
import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();
  const [showAuthModal, setShowAuthModal] = useState(false);
  const {isAuthenticated} = useAuth();

  return (
    <div className="relative h-screen w-full flex flex-col bg-gray-900 text-blue-600 overflow-hidden">
      <Navbar setShowAuthModal={setShowAuthModal} />
      <Hero />

      {/* Main Cards Section */}
      <div className="flex justify-center mt-20 gap-12 px-4 flex-wrap">
        <motion.div
          initial={{ opacity: 0, y: 500 }}
          animate={{ opacity: 1, y: 0, transition: { duration: 0.6 } }}
          whileHover={{ scale: 1.05 }}
          className="card-container bg-gray-800 p-6 rounded-xl shadow-lg cursor-pointer w-80"
          onClick={() => navigate("/visualizer")}
        >
          <FaCode className="text-yellow-600 w-10 h-10" />
          <Card
            title="Interactive Code Visualizer"
            description="Experiment with code and watch it transform into animated logic flows."
          />
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 500 }}
          animate={{ opacity: 1, y: 0, transition: { duration: 0.6 } }}
          whileHover={{ scale: 1.05 }}
          className="card-container bg-gray-800 p-6 rounded-xl shadow-lg cursor-pointer w-80"
          // onClick={() => navigate("/topics")}
          onClick={() => {
            if (!isAuthenticated) {
              setShowAuthModal(true);
              return;
            }
            navigate("/topics");
          }}

        >
          <LuBookOpen className="text-yellow-600 w-10 h-10" />
          <Card
            title="Learning Hub"
            description="Explore tutorials, resources, and guides to master coding concepts."
          />
        </motion.div>
      </div>

      <Footer />

      {/* Popup Modal for Login/Register */}
      {showAuthModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black/50 backdrop-blur-sm z-500">
          <div className="relative bg-transparent">
            <button
              onClick={() => setShowAuthModal(false)}
              className="absolute top-2 right-4 text-white text-xl bg-gray-700/70 hover:bg-gray-700 px-2.5 py-2.5 rounded-full z-[999] rounted-full"
            >
              ✕
            </button>
            <div className="scale-95 md:scale-100 transition-all">
              <AuthPage closeAuthModal={() => setShowAuthModal(false)}/>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Home;



// import React from "react";
// import Navbar from "../../../components/HomeComponents/Navbar";
// import Hero from "../../../components/HomeComponents/Hero";
// import Footer from "../../../components/HomeComponents/Footer";
// import Card from "../../../components/HomeComponents/Card";

// import { motion } from "framer-motion";
// import { FaCode } from "react-icons/fa";
// import { LuBookOpen } from "react-icons/lu";

// import "./Home.css"
// import { useNavigate } from "react-router-dom";
// function Home() {

//   const navigate = useNavigate()
//   return (
//     <div className="h-screen w-[100%] flex flex-col bg-gray-900 text-white">

//       <Navbar />
//       <Hero />
      
//       {/* Cards Section */}
//       <div className="flex justify-center mt-20 gap-12 px-4 flex-wrap">
//         <motion.div 
//           initial={{ opacity: 0, y: 500 }}
//           animate={{ opacity: 1, y: 0 , transition: { duration: 0.6 } }}
//           whileHover={{ scale: 1.05, transition: { duration: 0 } }}
          
//           className="card-container bg-gray-800 p-6 rounded-xl shadow-lg cursor-pointer w-80"
//           onClick={() => {navigate("/visualizer");}}
//         >
//           <FaCode className="text-yellow-600 w-10 h-10" />
//           <Card
//             title="Interactive Code Visualizer"
//             description="Experiment with code and watch it transform into animated logic flows."
//           />
//         </motion.div>
//         <motion.div 
//           initial={{ opacity: 0, y: 500 }}
//           animate={{ opacity: 1, y: 0 , transition: { duration: 0.6 } }}
//           whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//           className=" card-container bg-gray-800 p-6 rounded-xl shadow-lg cursor-pointer w-80"
//           onClick={() => {navigate("/topics");}}
//         >
//           <LuBookOpen className="text-yellow-600 w-10 h-10" />
//           <Card
//           title="Learning Hub"
//           description="Explore tutorials, resources, and guides to master coding concepts."
//         />
//         </motion.div>
        
      
//       </div>
      
//       <Footer />
//     </div>
//   );
// }

// export default Home;
