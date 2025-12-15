import React, { useEffect, useState } from "react";
import { Button } from "../../components/Learnpage components/Button";
import { Card, CardContent } from "../../components/Learnpage components/Card";
import "../../index.css"
import EditorPanel from "../../components/visualizer components/EditorPanel";
import { useNavigate, useParams } from "react-router-dom";
import { Loader2 } from "lucide-react";
import Modal from "../../components/visualizer components/Modal";
import http from "../../auth/HttpClient";
import axios from 'axios'
import Visualize2 from "../../components/visualizer components/Visualize2";

export default function LearnPage() {
  const [code, setCode] = useState("");
  const [input, setInput] = useState("");
  const [output, setOutput] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [error, setError] = useState("");
  const [isError, setIsError] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submissionResult, setSubmissionResult] = useState(null);
  const [showSubmissionModal, setShowSubmissionModal] = useState(false);
  const navigate = useNavigate();
  const [io, setIo] = useState(false);
  const levels = ["Incomplete", "Debug", "Problem Statement"];
  const questions = ["1", "2", "3"];
  const [currentLevel, setCurrentLevel] = useState(0);
  const [progress, setProgress] = useState([0, 0, 0]);
  const [selectedQuestion, setSelectedQuestion] = useState(0);
  const [editorHeight, setEditorHeight] = useState("460px");
  const [visualizationData, setVisualizationData] = useState(null);
  const [isLoading1, setIsLoading1] = useState(false);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const [questionData, setQuestionData] = useState([]);

  const {topicId} = useParams()
  const questionTypeMap = {
    0: 1, // Incomplete
    1: 2, // Debug
    2: 4  // Problem Statement
  };
  const questionTypeId = questionTypeMap[currentLevel];

  useEffect(() => {
    const fetchData = async () => {
      try {
        // const topicId = 36;
  
        const response = await http.post("http://192.168.0.110:8081/questions/uncomplete", {
          topicId,
          questionTypeId
        });
  
        console.log("Fetched questions:", response.data);
        
        // ✅ Flatten all nested arrays into a single array
        const flattenedQuestions = response.data.flat();
        
        console.log("Flattened questions:", flattenedQuestions);
        setQuestionData(flattenedQuestions);
        
      } catch (err) {
        console.error("Error fetching questions:", err);
      }
    };
    
    fetchData();
  }, [currentLevel]);

  // ✅ Sync code when question changes
  useEffect(() => {
    if (questionData.length > 0 && questionData[selectedQuestion]?.question[0]?.display_code) {
      setCode(questionData[selectedQuestion]?.question[0]?.display_code);
    }
    if (questionData.length > 0 && questionData[selectedQuestion]?.question[0]?.display_code == null) {
      setCode("// write your code here...");
    }
  }, [selectedQuestion, questionData]);

  // ✅ Reset submission result when question or level changes
  useEffect(() => {
    setSubmissionResult(null);
  }, [selectedQuestion, currentLevel]);

  const runCode = async () => {
    setIo(true);
    setEditorHeight("300px");
    setError("")
    setIsError(false)
    setOutput("")
    try {
      setIsLoading(true)
      const res = await http.post("/api/code/execute", {
        "code": code,
        "input": input
      });
      console.log(res.data);
      
      if (res?.data?.errors || res?.data?.error || res?.data?.error?.message) {
        setIsError(true);
        setError(res?.data?.errors || res?.data?.error || res?.data?.error?.message);
      }
      else {
        setOutput(res?.data?.output)
      }
    } catch (error) {
      // alert(error);
      if (error?.response?.data?.message) {
        setIsError(true);
        setError(error?.response?.data?.message);
      }
    }
    finally {
      setIsLoading(false);
    }
  }

  const handleSubmit = async () => {
    if (!questionData[selectedQuestion]?.question[0]?.id) {
      alert("Question data not loaded yet. Please wait.");
      return;
    }

    setIsSubmitting(true);
    setSubmissionResult(null);

    try {
      const questionTypeMap = {
        0: 1, // Incomplete
        1: 2, // Debug
        2: 4  // Problem Statement
      };

      const submissionPayload = {
        question_id: questionData[selectedQuestion].question[0].id,
        question_type: questionTypeMap[currentLevel],
        code: code,
        topic: 36 // You can make this dynamic if needed
      };

      console.log("Submitting:", submissionPayload);

      const response = await http.post("http://192.168.0.110:8081/api/code/submission", submissionPayload);

      console.log("Submission response:", response.data);
      setSubmissionResult(response.data);
      setShowSubmissionModal(true);

      // If submission is successful and all tests passed, update progress
      if (response.data.completion_status === true) {
        handleComplete();
      }

    } catch (error) {
      console.error("Submission error:", error);
      
      let errorMessage = "Submission failed. Please try again.";
      
      if (error?.response?.data) {
        if (error.response.data.status === "COMPILATION_ERROR") {
          errorMessage = "Compilation Error:\n" + (error.response.data.errors || "").toString();
        } else if (error.response.data.status === "RUNTIME_ERROR") {
          errorMessage = "Runtime Error:\n" + (error.response.data.errors || "");
        } else if (error.response.data.message) {
          errorMessage = error.response.data.message;
        }
      }

      setSubmissionResult({
        error: true,
        message: errorMessage
      });
      setShowSubmissionModal(true);
    } finally {
      setIsSubmitting(false);
    }
  };

  const fetchVisualization = async () => {
    setIsLoading1(true);
    try {
      const response = await axios.post('http://192.168.0.110:8081/api/visualize/execute', {
        "input": input,
        "code": code
      });
      const data = await response.data;
      setVisualizationData(data);
      openModal();
    } catch (error) {
      console.error('Error fetching visualization:', error);
    }
    finally{
      setIsLoading1(false);
    }
  };

  // Code Save
  
useEffect(() => {
  if (questionData.length === 0) return;

  const question = questionData[selectedQuestion]?.question?.[0];
  if (!question || !code) return;

  // Track the start time per question
  const startTimeKey = `startTime_${question.id}`;
  let startTime = sessionStorage.getItem(startTimeKey);

  if (!startTime) {
    startTime = new Date().toISOString();
    sessionStorage.setItem(startTimeKey, startTime);
  }

  const saveCode = async () => {
    const now = new Date();
    const firstSub = new Date(startTime);
    const diffMs = now - firstSub;

    // convert ms → hh:mm:ss
    const timeTaken = new Date(diffMs).toISOString().substring(11, 8);

    try {
      const payload = {
        question_id: question.id,
        // completion_status: false,
        // first_sub: firstSub.toISOString().slice(0, 19).replace("T", " "),
        // last_sub: now.toISOString().slice(0, 19).replace("T", " "),
        // time_taken: timeTaken,
        question_type_id: question.question_type_id || 4,
        latest_code: code,
        // latest_code_save_time: now.toISOString().slice(0, 19).replace("T", " "),
        topic_id: topicId,
      };

      await http.post("http://192.168.0.110:8081/questions/uct/storeCode", payload);
      console.log("Auto-saved:", payload);
    } catch (err) {
      console.error("Auto-save failed:", err);
    }
  };

  // Save every 3s
  const interval = setInterval(saveCode, 3000);

  //Final save before leaving or refreshing
  const handleBeforeUnload = (e) => {
    e.preventDefault();
    saveCode();
  };
  window.addEventListener("beforeunload", handleBeforeUnload);

  // Cleanup
  return () => {
    clearInterval(interval);
    window.removeEventListener("beforeunload", handleBeforeUnload);
  };
}, [code, selectedQuestion, questionData, topicId]);



  const handleComplete = () => {
    let newProgress = [...progress];
    if (newProgress[currentLevel] < 3) newProgress[currentLevel] += 1;
    setProgress(newProgress);
    if (newProgress[currentLevel] === 3 && currentLevel < 2) {
      setCurrentLevel(currentLevel + 1);
      setSelectedQuestion(0);
    }
  };

  const handleIOPanel = () => {
    setIo(!io);
    if (io) {
      setEditorHeight("460px");
    }
    else {
      setEditorHeight("300px");
    }

  };

  // Function to render stars at specific positions
  const renderStarsAtPositions = (progressCount) => {
    const starPositions = [25, 50, 75];

    return starPositions.map((position, index) => {
      const isVisible = index < progressCount;

      return (
        <div
          key={index}
          className="absolute top-1/2 -translate-y-1/2 z-10"
          style={{ left: `${position}%`, transform: 'translate(-50%, -50%)' }}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            className={`w-4 h-4 transition-all duration-500 ${isVisible ? "text-yellow-400 scale-100" : "text-transparent scale-0"
              }`}
          >
            <path
              fillRule="evenodd"
              d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z"
              clipRule="evenodd"
            />
          </svg>
        </div>
      );
    });
  };

  // Function to render text with progressive coloring
  const renderProgressiveText = (text, progressCount, isCompleted, isActive) => {
    const totalLetters = text.length;
    const percentComplete = progressCount / 3;
    const lettersToColor = Math.ceil(percentComplete * totalLetters);

    return (
      <span className="text-center font-semibold mb-1 text-sm sm:text-base">
        {text.split('').map((letter, idx) => {
          let color = 'text-gray-400'; // default for inactive levels

          if (isActive) {
            // Active level: completed part is green, rest is blue
            if (idx < lettersToColor) {
              color = 'text-green-600'; // Completed portion
            } else {
              color = 'text-blue-500'; // Remaining portion (active)
            }
          } else if (isCompleted) {
            // Fully completed level: all green
            color = 'text-green-600';
          } else if (progressCount > 0) {
            // Not active but has progress: show completed portion
            if (idx < lettersToColor) {
              color = 'text-green-600';
            } else {
              color = 'text-gray-400';
            }
          }

          return (
            <span key={idx} className={`${color} transition-colors duration-300`}>
              {letter}
            </span>
          );
        })}
      </span>
    );
  };

  return (
    <div className="p-3 sm:p-6 bg-gray-900 text-gray-100 min-h-screen lg:w-[100%] overflow-hidden">
      {/* Back Button */}
      <div className="flex flex-col sm:flex-row space-y-3 sm:space-y-0 sm:space-x-6 mb-4 sm:mb-6">
        <button className=" text-center w-20 rounded-3xl h-9 relative text-white  font-semibold group mb-3 ease-in-out hover:scale-105" type="button" onClick={() => { navigate("/topics"); }}>
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

        {/* Progress Bar */}
        {levels.map((level, index) => {
          const percent = (progress[index] / 3) * 100;
          const isCompleted = progress[index] === 3;
          const isActive = index === currentLevel;

          return (
            <div
              key={index}
              className="flex-1 cursor-pointer"
              onClick={() => {
                setCurrentLevel(index);
                setSelectedQuestion(0);
              }}
            >
              <p className="text-center mb-1">
                {renderProgressiveText(level, progress[index], isCompleted, isActive)}
              </p>
              <div className="w-full h-4 bg-gray-700 rounded-xl overflow-hidden relative">
                <div
                  className={`h-full transition-all duration-500 ${isCompleted
                    ? "bg-green-600"
                    : isActive
                      ? "bg-blue-600"
                      : "bg-blue-500/50"
                    }`}
                  style={{ width: `${percent}%` }}
                />

                {renderStarsAtPositions(progress[index])}
              </div>
            </div>
          );
        })}
      </div>

      {/* Main Layout */}
      <div className="flex flex-col lg:grid lg:grid-cols-12 gap-4 lg:h-[84vh]">
        {/* Left Panel */}
        <Card className="lg:col-span-1 bg-gray-800 border-gray-700 lg:w-auto">
          <CardContent className="p-2 sm:p-4 h-full">
            <div className="flex flex-row lg:flex-col gap-2 lg:space-y-2">
              {questions.map((q, index) => (
                <Button
                  key={index}
                  onClick={() => {
                    setSelectedQuestion(index);
                  }}
                  className={`flex-1 lg:w-full justify-center lg:justify-start text-sm ${selectedQuestion === index
                    ? "bg-blue-600 text-white border border-blue-600"
                    : "bg-gray-800 text-blue-600 border border-blue-400 hover:bg-blue-600 hover:text-white"
                    }`}
                >
                  {q}
                </Button>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Center Panel */}
        <Card className="lg:col-span-4 bg-gray-800 border-gray-700 h-auto lg:h-full lg:w-[100%] overflow-hidden">
          <CardContent className="p-3 sm:p-4 h-full flex flex-col overflow-hidden">
            <h2 className="font-bold text-lg sm:text-xl mb-2 text-blue-700 flex-shrink-0">
              {"Question " + questions[selectedQuestion]}
            </h2>

            <div className="flex-grow overflow-y-auto overscroll-contain pr-2 mb-3">
              {questionData.length > 0 ? (
                <div className="space-y-2">
                  <h3 className="text-blue-400 font-semibold text-base mb-1">
                    {questionData[selectedQuestion]?.question[0]?.title || "No Title available"}
                  </h3>
                  <p dangerouslySetInnerHTML={{ __html: questionData[selectedQuestion]?.question[0]?.description || "No statement available" }} className="text-gray-200 text-sm sm:text-base leading-relaxed mb-2">
                  </p>

                  {questionData[selectedQuestion]?.question[0]?.difficulty && (
                    <p className="text-gray-400 text-xs italic">
                      Difficulty: {questionData[selectedQuestion]?.question[0].difficulty}
                    </p>
                  )}
                </div>
              ) : (
                <p className="text-gray-400 text-sm">Loading questions...</p>
              )}
            </div>


            {/* Test Cases Section */}
            <div className="bg-gray-900 border border-gray-700 rounded-lg p-2 mb-3 text-center">
              <h3 className="text-blue-400 font-semibold mb-2 text-sm uppercase tracking-wide">
                Test Cases - {submissionResult?.passed || 0}/{questionData[selectedQuestion]?.testcases || 0}
              </h3>
            </div>

            {/* Buttons */}
            <div className="flex flex-wrap sm:flex-nowrap justify-between mt-auto gap-2 sm:space-x-2 flex-shrink-0">
              <Button className="flex-1 sm:flex-initial bg-blue-500 text-white hover:bg-blue-600 text-m" onClick={runCode} disabled={isLoading || isSubmitting}>
                {isLoading ? <Loader2 className='animate-spin' /> : "Run"}
              </Button>
              <Button
                onClick={handleSubmit}
                className="flex-1 sm:flex-initial bg-green-500 text-white hover:bg-green-600 text-sm"
                disabled={isSubmitting || isLoading}
              >
                {isSubmitting ? <Loader2 className='animate-spin' /> : "Submit"}
              </Button>
              <Button className="flex-1 sm:flex-initial bg-gray-500 text-white hover:bg-gray-600 text-sm" onClick={fetchVisualization} disabled={isLoading || isSubmitting}>
                {isLoading1 ? <Loader2 className='animate-spin' /> : "Visualize"}
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Right Panel / Coding Area */}
        <Card className="lg:col-span-7 bg-gray-800 border-gray-700 shadow-md h-full lg:w-[100%]">
          <CardContent className="p-3 sm:p-4 h-full flex flex-1 flex-col">

            <EditorPanel editorHeight={editorHeight} setCode={setCode} code={code} readOnly={false}/>

            <button
              className="flex-1 sm:flex-initial bg-gray-500 text-white hover:bg-gray-600 text-sm py-1 rounded-lg"
              onClick={handleIOPanel}
            >
              {io ? (
                <i className="ri-arrow-down-double-line text-xl"></i>
              ) : (
                <i className="ri-arrow-up-double-line text-xl"></i>
              )}
            </button>
            {io && (
              <div className="flex-1 flex gap-2 w-full mt-2">
                <div className="flex-1 bg-gray-900 rounded-lg">
                  <textarea
                    placeholder="Enter input here..."
                    className="flex-grow w-full p-2 sm:p-3 rounded-lg h-full font-mono resize-none focus:ring-2 focus:ring-blue-400 bg-gray-900 text-white min-h-[300px] lg:min-h-0 text-sm"
                    onChange={(e) => { setInput(e.target.value) }}
                    value={input}
                  ></textarea>
                </div>
                <div className="flex-1 bg-gray-900 rounded-lg">
                  <textarea
                    style={{ color: isError ? "red" : "white" }}
                    placeholder="Output will display here..."
                    className="flex-grow w-full p-2 sm:p-3 rounded-lg h-full font-mono resize-none focus:ring-2 focus:ring-blue-400 bg-gray-900 text-white min-h-[300px] lg:min-h-0 text-sm"
                    readOnly={true}
                    value={isError ? error : output}
                  ></textarea>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Visualize Modal */}
      <Modal isOpen={isModalOpen} onClose={closeModal}>
        <Visualize2 setIsModalOpen={setIsModalOpen} data={visualizationData}/>
      </Modal>

      {/* Submission Result Modal */}
      {showSubmissionModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-gray-800 rounded-lg p-6 max-w-md w-full border border-gray-700">
            <h2 className="text-2xl font-bold mb-4 text-center">
              {submissionResult?.error ? (
                <span className="text-red-500">Submission Failed</span>
              ) : submissionResult?.completion_status ? (
                <span className="text-green-500">✓ All Tests Passed!</span>
              ) : (
                <span className="text-yellow-500">Some Tests Failed</span>
              )}
            </h2>

            {submissionResult?.error ? (
              <div className="bg-gray-900 p-4 rounded-lg mb-4">
                <pre className="text-red-400 text-sm whitespace-pre-wrap">{submissionResult.message}</pre>
              </div>
            ) : (
              <div className="space-y-3">
                <div className="bg-gray-900 p-4 rounded-lg">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-gray-300">Total Test Cases:</span>
                    <span className="text-white font-semibold">{submissionResult?.total}</span>
                  </div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-green-400">Passed:</span>
                    <span className="text-green-400 font-semibold">{submissionResult?.passed}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-red-400">Failed:</span>
                    <span className="text-red-400 font-semibold">{submissionResult?.failed}</span>
                  </div>
                </div>
                {submissionResult?.completion_status && (
                  <div className="bg-green-900 bg-opacity-20 border border-green-500 rounded-lg p-3 text-center">
                    <p className="text-green-400 font-semibold">Question Completed! 🎉</p>
                  </div>
                )}
              </div>
            )}

            <button
              onClick={() => setShowSubmissionModal(false)}
              className="w-full mt-4 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div >
  );
}