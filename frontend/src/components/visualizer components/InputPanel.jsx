import React from 'react'

const InputPanel = ({input, setInput, readOnly}) => {
  const handleChange = (e) => {
    setInput(e.target.value);
  }
  return (
    <div id='input' className='w-full h-[50%] flex flex-col rounded-[10px]'>
      <label style={{color:"#22d3ee"}} htmlFor="input" className='text-gray-300 mb-3'>Sample Input</label>
      <textarea className='p-3 border-none h-full w-[100%] bg-gray-800 resize-none text-white rounded-[10px] overflow-y-scroll [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]' name="input" id="io-textarea" placeholder='Enter Input here.. ' value={input} onChange={handleChange} readOnly={readOnly} spellCheck={false}></textarea>
    </div>
  )
}

export default InputPanel
