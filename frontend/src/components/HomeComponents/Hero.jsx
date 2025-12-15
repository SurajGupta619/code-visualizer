import React from "react";
import { motion } from "framer-motion";
import { TypeAnimation } from 'react-type-animation';

const Hero = () => {

       
       return (
       <motion.div
              className="flex flex-col items-center justify-center text-center mt-24 px-4"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
       >
              <h1 className="text-5xl font-bold text-blue-400 mb-6">
                     <TypeAnimation
                            sequence={[
                            'Reimagine Code.',
                            500,
                            '',
                            100,
                           'Visualize Logic.',
                           500,
                           '',
                           100,
                           'Learn Faster.',
                           500,
                           '',
                           100

                            ]}
                            speed={50}
                            style={{ fontSize: '3.2rem' }}
                            repeat={Infinity}
                     />
                     
              </h1>
              <p className="mt-4 text-lg font-medium text-yellow-600 max-w-2xl">
                     An interactive platform that transforms code into dynamic visual flows
                     while gamifying learning.
              </p>
       </motion.div>
       );
}
export default Hero;