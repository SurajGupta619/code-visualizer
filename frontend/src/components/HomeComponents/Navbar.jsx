import React from "react";
import { motion } from "framer-motion";
import { useAuth } from "../../auth/AuthContext";

function Navbar({ setShowAuthModal }) {
  const { isAuthenticated, logout, name } = useAuth();

  return (
    <motion.div
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0, transition: { duration: 0.6 } }}
    >
      <nav className="w-full py-2 px-10 flex justify-between items-center bg-gray-800/70 backdrop-blur-md fixed top-0 z-50">
        {/* Logo Section */}
        <motion.div
          initial={{ opacity: 0, x: 400 }}
          animate={{ opacity: 1, x: 0, transition: { duration: 0.8 } }}
          className="flex items-center gap-2"
        >
          <div className="border-8 rounded-full border-blue-700 p-2"></div>
          <div className="text-2xl font-bold text-blue-400">CodeLens.</div>
        </motion.div>

        {/* Buttons Section */}
        <motion.div
          initial={{ opacity: 0, x: -500 }}
          animate={{ opacity: 1, x: 0, transition: { duration: 0.6 } }}
          className="flex flex-row gap-2 items-center"
        >
          {isAuthenticated ? (
            <>
              <motion.h1
                whileHover={{ scale: 1.05 }}
                className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
              >
                {name}
              </motion.h1>
              <motion.button
                whileHover={{ scale: 1.05 }}
                className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
                onClick={logout}
              >
                Logout
              </motion.button>
            </>
          ) : (
            <motion.button
              whileHover={{ scale: 1.05 }}
              className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
              onClick={() => setShowAuthModal(true)}
            >
              Sign In
            </motion.button>
          )}
        </motion.div>
      </nav>
    </motion.div>
  );
}

export default Navbar;

// import React from "react";
// import { motion } from "framer-motion";
// import { useAuth } from "../../auth/AuthContext";
// import { useNavigate } from "react-router-dom";

// function Navbar() {
//   const { isAuthenticated, logout, userName } = useAuth();
//   const navigate = useNavigate();
//   // const userName = getUserName();

//   return (
//     <motion.div
//       initial={{ opacity: 0, y: -20 }}
//       animate={{ opacity: 1, y: 0, transition: { duration: 0.6 } }}
//     >
//       <nav className="w-full py-2 px-10 flex justify-between items-center bg-gray-800/70 backdrop-blur-md fixed top-0 z-50">
//         {/* Logo Section */}
//         <motion.div
//           initial={{ opacity: 0, x: 400 }}
//           animate={{ opacity: 1, x: 0, transition: { duration: 0.8 } }}
//           className="flex items-center gap-2"
//         >
//           <div className="border-8 rounded-full border-blue-700 p-2"></div>
//           <div className="text-2xl font-bold text-blue-400">CodeLens.</div>
//         </motion.div>

//         {/* Buttons Section */}
//         <motion.div
//           initial={{ opacity: 0, x: -500 }}
//           animate={{ opacity: 1, x: 0, transition: { duration: 0.6 } }}
//           className="flex flex-row gap-2 items-center"
//         >
//           {isAuthenticated ? (
//             <>
//               <motion.h1
//                 whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//                 className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
//               >
//                 {userName}
//               </motion.h1>
//               <motion.button
//                 whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//                 className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
//                 onClick={logout}
//               >
//                 Logout
//               </motion.button>
//             </>
//           ) : (
//             <motion.button
//               whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//               className="px-3 py-1 rounded-full border border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
//               onClick={() => navigate("/login")}
//             >
//               Login
//             </motion.button>
//           )}
//         </motion.div>
//       </nav>
//     </motion.div>
//   );
// }

// export default Navbar;
// import React from "react";
// import { motion } from "framer-motion";
// import { useAuth } from "../../auth/AuthContext";

// function Navbar() {
//   const {getUserName,logout} = useAuth();
  
//   const userName = getUserName();
//   return (
//     <motion.div
//       initial={{ opacity: 0, y: -20 }}
//       animate={{ opacity: 1, y: 0 , transition: { duration: 0.6 } }}
//     >
//       <nav className="w-full py-2 px-10 flex justify-between items-center bg-gray-800/70 backdrop-blur-md fixed top-0 z-50">
//         <motion.div
//           initial={{ opacity: 0, x: 400 }}
//           animate={{ opacity: 1, x: 0 , transition: { duration: 0.8 } }}
//           className="flex items-center gap-2">
//           <div className="border-8 rounded-full border-blue-700 p-2"></div>
//           <div className="text-2xl font-bold text-blue-400">CodeLens.</div>
//         </motion.div>
        
//         <motion.div  
//           initial={{ opacity: 0, x: -500 }}
//           animate={{ opacity: 1, x: 0 , transition: { duration: 0.6 } }}
          
//           className="flex flex-row gap-2 items-center ">
          
//           <motion.h1 
//             whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//             className="px-3 py-1 rounded-full border-1 border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
//           >
//             {userName}
//           </motion.h1>
//           <motion.button
//             whileHover={{ scale: 1.05, transition: { duration: 0 } }}
//             className="px-3 py-1 rounded-full border-1 border-blue-300 hover:text-blue-400 hover:border-blue-600 hover:shadow-blue-700 hover:shadow-md"
//             onClick={logout}
//           >
//             Logout
//           </motion.button>
          
          
//         </motion.div >
//       </nav>
//     </motion.div>
//   );
// }

// export default Navbar;
