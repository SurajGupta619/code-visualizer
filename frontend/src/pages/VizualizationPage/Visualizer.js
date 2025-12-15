import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import EditorPanel from "../../components/visualizer components/EditorPanel"
import InputPanel from "../../components/visualizer components/InputPanel"
import OutputPanel from "../../components/visualizer components/OutputPanel"
import { Button } from '../../components/Learnpage components/Button'
import Visualize2 from '../../components/visualizer components/Visualize2'
import Modal from '../../components/visualizer components/Modal'
import { Loader2 } from 'lucide-react'
import http from '../../auth/HttpClient'
import axios from 'axios'
import toast from 'react-hot-toast';

const Visualizer = () => {
  const initialCode = `import java.util.*;
public class Solution {

    public static void main(String[] args) {
      System.out.print("Hello World");
    }
}`;
  const [code, setCode] = useState(initialCode);
  const [input, setInput] = useState("");
  const [output, setOutput] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [error, setError] = useState("");
  const [isError, setIsError] = useState(false);
  const [isLoading1, setIsLoading1] = useState(false);
  const [isLoading2, setIsLoading2] = useState(false);
  const navigate = useNavigate();
  const [visualizationData, setVisualizationData] = useState(null);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);


  const runCode = async () => {
    setError("")
    setIsError(false)
    setOutput("")
    try {
      setIsLoading2(true)
      const res = await http.post("/api/code/execute", {
        "code": code,
        "input": input
      });
      toast.success('Code executed Successfully')
      if (res?.data?.errors || res?.data?.error) {
        setIsError(true);
        setError(res?.data?.errors || res?.data?.error);
      }
      else {
        setOutput(res.data.output)
      }
    } catch (error) {
      toast.error("Something went wrong.")
      if (error?.response?.data?.message) {
        setIsError(true);
        setError(error?.response?.data?.message);
      }
    }
    finally {
      setIsLoading2(false);
    }
  }


  const fetchVisualization = async () => {
    setIsLoading1(true);
    try {
      const response = await axios.post('http://192.168.0.110:8081/api/visualize/execute', {
        "input": input,
        "code": code
      });
      const data = await response.data;
      setVisualizationData(data);
      toast.success("Visualized successfully.")
      openModal();
    } catch (error) {
      toast.error('Error fetching visualization');
    }
    finally{
      setIsLoading1(false);
    }
  };


  return (
    <>
      <div id='main' className='w-full h-full flex p-4 bg-gray-300 items-center justify-center gap-[1%]'>
        <div id='left' className='h-full w-[49.5%] p-6 bg-[#111827] rounded-2xl'>
          <button className=" text-center w-20 rounded-3xl h-9 relative text-white  font-semibold group mb-3 ease-in-out hover:scale-105" type="button" onClick={() => { navigate("/"); }}>
            <div className="bg-blue-500 rounded-xl h-7 w-1/4 flex items-center justify-center absolute left-1 top-[4px] group-hover:w-[65px] z-10 duration-500">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 1024 1024"
                height="25px"
                width="25px"
              >
                <path
                  d="M224 480h640a32 32 0 1 1 0 64H224a32 32 0 0 1 0-64z"
                  fill="#000000"
                ></path>
                <path
                  d="m237.248 512 265.408 265.344a32 32 0 0 1-45.312 45.312l-288-288a32 32 0 0 1 0-45.312l288-288a32 32 0 1 1 45.312 45.312L237.248 512z"
                  fill="#000000"
                ></path>
              </svg>
            </div>
            <p className="translate-x-2">Back</p>
          </button>
          {/* <h1 style={{ color: "#22d3ee" }} className='text-3xl border-b-[3px] border-gray-500 pb-[10px] mb-[10px]'>Code Editor</h1> */}
          <div className='w-full rounded-3xl border-4 border-gray-600 overflow-hidden'>
            <EditorPanel setCode={setCode} code={code} readOnly={false} />
          </div>
          <div id='btn-wrapper' className='w-[100%] p-2 flex items-center justify-between h-[10%] mt-3'>
            <Button className='bg-blue-500 text-gray-200 text-[17px] px-4 py-2' onClick={fetchVisualization}>{isLoading1 ? <Loader2 className='animate-spin' /> : "Visualize"}</Button>
            <Button className='bg-green-600 text-gray-100 text-[17px] px-4 py-2' onClick={runCode}>{isLoading2 ? <Loader2 className='animate-spin' /> : "Run"}</Button>
          </div>
        </div>
        <div id='right' className='h-full w-[49.5%] p-6 bg-[#111827] rounded-2xl'>
          <div id='io-wrapper' className='w-full flex flex-col h-[100%]'>
            <InputPanel input={input} setInput={setInput} readOnly={false} />
            <OutputPanel output={output} isError={isError} error={error} />
            {/* <OutputPanel output={output} /> */}
          </div>
        </div>
      </div>
      <Modal isOpen={isModalOpen} onClose={closeModal}>
        <Visualize2 setIsModalOpen={setIsModalOpen} data={visualizationData} />
      </Modal>
    </>
  )
}

export default Visualizer
