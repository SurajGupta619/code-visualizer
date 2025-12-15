import React from 'react'

const OutputPanel = ({ output, isError, error }) => {
  return (
    <div id='output' className='w-full h-[50%] flex flex-col rounded-[10px]'>
      <label style={{ color: "#22d3ee" }} htmlFor="output" className='text-gray-300 mb-3'>Output</label>
      <textarea style={{color: isError ? "red" :"white"}} className='p-3  h-full w-[100%] bg-gray-800 resize-none text-white rounded-[10px] border-none overflow-y-scroll [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]' name="output" id="io-textarea" disabled placeholder='Output' value={isError ? error : output}></textarea>
    </div>
  )
}

export default OutputPanel
