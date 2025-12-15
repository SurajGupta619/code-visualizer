import React, { useEffect, useState } from 'react'
// import { IoMdCode } from "react-icons/io";
import LinearProgress from '@mui/material/LinearProgress'
import toast from 'react-hot-toast';


import { useNavigate } from "react-router-dom";

import './TopicCard.css';
import http from '../../../auth/HttpClient';

const TopicCard = () => {
    const [topic, setTopic] = useState([])
    const navigate = useNavigate()
    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await http.get("/questions/fetchTopic")
                setTopic(res.data)
                

            } catch (error) {
                toast.error(error)

                // alert(error)
            }
        }
        fetchData();
    }, [])



    return (
        <div className='flex justify-center '>
            <div className='flex flex-col gap-3 min-w-full px-10'>
                <div className="grid grid-cols-3 bg-gray-900 max-h-[calc(100vh-90px)] overflow-y-hidden">
                    {topic.map((card) => (
                        <div key={card[0]} className="card1 bg-gray-500 p-5 ml-5 mt-5 rounded-md w-100 flex flex-col gap-3 hover:cursor-pointer" onClick={() => { navigate(`/learn/${card[0]}`); }}>
                            <div className='flex items-center gap-2 mb-2'>
                                <h3 className='text-md font-medium'>{card[1]}</h3>
                            </div>
                            {/* <p className='text-md  pl-4'>{card.desc}</p> */}
                            <LinearProgress sx={{ height: 6, borderRadius: 2 }} variant='determinate' value={Math.round(Number(card[2]))} className='' />
                            <p className="ml-[8rem] text-sm font-medium text-gray-900 self-end">{Math.round(Number(card[2]))}% Completed</p>
                            <div className="go-corner">
                                <div className="go-arrow">→</div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default TopicCard


