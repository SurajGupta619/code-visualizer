import React from 'react'
import { BiBookReader } from "react-icons/bi";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate()
  return (
    <div className='flex  h-[80px] justify-between items-center px-6'>
        <button
        className="text-center w-20 flex justify-center items-center rounded-xl h-10 relative text-white text-md font-semibold group"
        type="button"
        onClick={() => {navigate("/");}}>
            <div className="bg-blue-400 rounded-xl h-8 w-1/4 flex items-center justify-center absolute left-1 top-[4px] group-hover:w-[70px] z-10 duration-500">
        
            <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 1024 1024"
            height="25px"
            width="25px"
          >
            <path
              d="M224 480h640a32 32 0 1 1 0 64H224a32 32 0 0 1 0-64z"
              fill=""
            ></path>
            <path
              d="m237.248 512 265.408 265.344a32 32 0 0 1-45.312 45.312l-288-288a32 32 0 0 1 0-45.312l288-288a32 32 0 1 1 45.312 45.312L237.248 512z"
              fill=""
            ></path>
          </svg>
            </div>
            <p className="text-blue-400 translate-x-2 ">Back</p>
          </button>


      <div className='flex justify-center items-center gap-4'>
        <BiBookReader className='text-yellow-600 text-4xl'/>
        <h1 className='text-blue-400 text-4xl font-bold'>Java Learning Topics</h1>
      </div>
      <h1></h1>
    </div>
  )
}

export default Header
