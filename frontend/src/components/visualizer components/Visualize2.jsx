import React, { useState } from "react";
import EditorPanel from "./EditorPanel";
import { Button } from "../Learnpage components/Button";
import OutputPanel from "./OutputPanel";

const Visualize2 = ({ setIsModalOpen, data }) => {
  const [visualizationData, setVisualizationData] = useState(data);
  const [currentStep, setCurrentStep] = useState(0);
  const code = visualizationData.in.code;

  const handleStart = () => {
    setCurrentStep(0);
  };

  const handlePrevStep = () => {
    setCurrentStep(Math.max(0, currentStep - 1));
  };

  const handleNextStep = () => {
    if (visualizationData && currentStep < visualizationData.out.length - 1) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handleEnd = () => {
    if (visualizationData) {
      setCurrentStep(visualizationData.out.length - 1);
    }
  };

  // Get active heap objects referenced by current frame
  const getActiveHeapObjects = (frame) => {
    if (!frame) return new Set();

    const activeRefs = new Set();

    // Collect all references from local variables
    frame.frameList.forEach((f) => {
      Object.values(f.locals).forEach((val) => {
        if (Array.isArray(val) && val[0] === "REF") {
          activeRefs.add(val[1]);
        }
      });
    });

    // Recursively collect references from heap objects
    const collectReferences = (objId) => {
      if (!frame.heap[objId] || activeRefs.has(objId)) return;
      activeRefs.add(objId);

      const obj = frame.heap[objId];
      if (obj.elements) {
        obj.elements.forEach((el) => {
          if (Array.isArray(el) && el[0] === "REF") {
            collectReferences(el[1]);
          }
        });
      }
      if (obj.fields) {
        Object.values(obj.fields).forEach((val) => {
          if (Array.isArray(val) && val[0] === "REF") {
            collectReferences(val[1]);
          }
        });
      }
    };

    // Start collecting from initial references
    Array.from(activeRefs).forEach((ref) => collectReferences(ref));

    return activeRefs;
  };

  const renderValue = (value) => {
    if (Array.isArray(value)) {
      if (value[0] === "REF") {
        return <span className="text-purple-400">→ ref:{value[1]}</span>;
      } else if (value[0] === "VALUE") {
        return <span className="text-blue-400 font-semibold">{value[1]}</span>;
      }
    }
    return <span className="text-gray-400">{JSON.stringify(value)}</span>;
  };

  // Collection type configurations
  const collectionConfig = {
    ARRAY: { icon: "📦", color: "purple", label: "Array" },
    MAP: { icon: "🗺️", color: "orange", label: "Map" },
    LINKEDHASHMAP: { icon: "🔗", color: "orange", label: "LinkedHashMap" },
    HASHMAP: { icon: "🗂️", color: "amber", label: "HashMap" },
    TREEMAP: { icon: "🌲", color: "lime", label: "TreeMap" },
    SET: { icon: "📋", color: "cyan", label: "Set" },
    HASHSET: { icon: "⚡", color: "cyan", label: "HashSet" },
    LINKEDHASHSET: { icon: "🔗", color: "teal", label: "LinkedHashSet" },
    TREESET: { icon: "🌳", color: "emerald", label: "TreeSet" },
    LIST: { icon: "📝", color: "blue", label: "List" },
    ARRAYLIST: { icon: "📊", color: "indigo", label: "ArrayList" },
    LINKEDLIST: { icon: "⛓️", color: "violet", label: "LinkedList" },
    VECTOR: { icon: "➡️", color: "fuchsia", label: "Vector" },
    STACK: { icon: "📚", color: "pink", label: "Stack" },
    QUEUE: { icon: "🎫", color: "rose", label: "Queue" },
    DEQUE: { icon: "↔️", color: "red", label: "Deque" },
    PRIORITYQUEUE: { icon: "🎯", color: "yellow", label: "PriorityQueue" },
    OBJECT: { icon: "🔷", color: "green", label: "Object" },
  };

  const getColorClasses = (color) => {
    const colorMap = {
      purple: {
        border: "border-purple-600",
        text: "text-purple-400",
        bg: "bg-gray-700 border-purple-500",
      },
      orange: {
        border: "border-orange-600",
        text: "text-orange-400",
        bg: "bg-gray-700 border-orange-500",
      },
      amber: {
        border: "border-amber-600",
        text: "text-amber-400",
        bg: "bg-gray-700 border-amber-500",
      },
      lime: {
        border: "border-lime-600",
        text: "text-lime-400",
        bg: "bg-gray-700 border-lime-500",
      },
      cyan: {
        border: "border-cyan-600",
        text: "text-cyan-400",
        bg: "bg-gray-700 border-cyan-500",
      },
      teal: {
        border: "border-teal-600",
        text: "text-teal-400",
        bg: "bg-gray-700 border-teal-500",
      },
      emerald: {
        border: "border-emerald-600",
        text: "text-emerald-400",
        bg: "bg-gray-700 border-emerald-500",
      },
      blue: {
        border: "border-blue-600",
        text: "text-blue-400",
        bg: "bg-gray-700 border-blue-500",
      },
      indigo: {
        border: "border-indigo-600",
        text: "text-indigo-400",
        bg: "bg-gray-700 border-indigo-500",
      },
      violet: {
        border: "border-violet-600",
        text: "text-violet-400",
        bg: "bg-gray-700 border-violet-500",
      },
      fuchsia: {
        border: "border-fuchsia-600",
        text: "text-fuchsia-400",
        bg: "bg-gray-700 border-fuchsia-500",
      },
      pink: {
        border: "border-pink-600",
        text: "text-pink-400",
        bg: "bg-gray-700 border-pink-500",
      },
      rose: {
        border: "border-rose-600",
        text: "text-rose-400",
        bg: "bg-gray-700 border-rose-500",
      },
      red: {
        border: "border-red-600",
        text: "text-red-400",
        bg: "bg-gray-700 border-red-500",
      },
      yellow: {
        border: "border-yellow-600",
        text: "text-yellow-400",
        bg: "bg-gray-700 border-yellow-500",
      },
      green: {
        border: "border-green-600",
        text: "text-green-400",
        bg: "bg-gray-700 border-green-500",
      },
    };
    return colorMap[color] || colorMap["green"];
  };

  const renderHeapObject = (objId, obj) => {
    const objType = obj.type?.toUpperCase() || "OBJECT";
    const config = collectionConfig[objType] || collectionConfig["OBJECT"];
    const colors = getColorClasses(config.color);

    // Check if it's a map-like structure (has key-value pairs in fields)
    const isMapLike =
      obj.fields &&
      Object.keys(obj.fields).length > 0 &&
      (objType.includes("MAP") ||
        (objType === "OBJECT" && obj.elements === null));

    // Check if it's a collection with elements (array-like or set-like)
    const hasElements = obj.elements && obj.elements.length > 0;

    return (
      <div className={`bg-gray-800 border ${colors.border} rounded p-3 mb-2`}>
        <div className={`font-semibold ${colors.text} mb-2 flex items-center`}>
          <span className="mr-2">{config.icon}</span>
          {config.label} #{objId}
          <span className="ml-2 text-xs text-gray-400">
            (
            {obj.length !== null && obj.length !== undefined
              ? `size: ${obj.length}`
              : "empty"}
            )
          </span>
        </div>

        {/* Render Map-like structures (key-value pairs) */}
        {isMapLike ? (
          <div className="space-y-1">
            {Object.entries(obj.fields).map(([key, val], idx) => (
              <div
                key={idx}
                className={`${colors.bg} px-3 py-2 rounded border flex items-center gap-2 text-sm`}
              >
                <span className={`${colors.text} font-semibold`}>{key}</span>
                <span className="text-gray-400">→</span>
                {renderValue(val)}
              </div>
            ))}
          </div>
        ) : hasElements ? (
          /* Render Array-like or Set-like structures (indexed or unindexed elements) */
          <div className="flex flex-wrap gap-2">
            {obj.elements.map((el, idx) => (
              <div
                key={idx}
                className={`${colors.bg} px-2 py-1 rounded border text-sm`}
              >
                {objType.includes("ARRAY") ||
                objType.includes("LIST") ||
                objType.includes("VECTOR") ? (
                  <>
                    <span className="text-gray-400">[{idx}]:</span>{" "}
                    {renderValue(el)}
                  </>
                ) : (
                  renderValue(el)
                )}
              </div>
            ))}
          </div>
        ) : obj.fields && Object.keys(obj.fields).length > 0 ? (
          /* Render regular objects with fields */
          <div className="space-y-1">
            {Object.entries(obj.fields).map(([key, val], idx) => (
              <div key={idx} className="flex items-center gap-2 text-sm">
                <span className="text-gray-300">{key}:</span>
                {renderValue(val)}
              </div>
            ))}
          </div>
        ) : (
          <div className="text-gray-500 text-sm">
            Empty {config.label.toLowerCase()}
          </div>
        )}
      </div>
    );
  };

  const currentFrame = visualizationData?.out[currentStep];
  const activeHeapRefs = currentFrame
    ? getActiveHeapObjects(currentFrame)
    : new Set();

  // Get code with line highlighting
  const getHighlightedCode = () => {
    if (!visualizationData || !currentFrame) return code;

    // const lines = visualizationData.in.code.split('\n');
    const lastLine = parseInt(currentFrame.lastLineNo);
    const nextLine = parseInt(currentFrame.nextLineNo);

    return {
      code: visualizationData.in.code,
      lastLine,
      nextLine,
    };
  };

  const highlightInfo = getHighlightedCode();

  return (
    <>
      <div className="h-[90%] w-[90%] absolute flex z-34 bg-[#111827] p-3">
        <div id="left" className="w-[47%] h-full overflow-y-auto">
          <EditorPanel
            setCode={visualizationData.in.code}
            code={highlightInfo.code}
            readOnly={true}
            currentLine={highlightInfo.nextLine}
            previousLine={highlightInfo.lastLine}
          />
          <div className="bg-[#1e1e1e] p-4 mt-2 mb-2">
            <span className="text-sm text-gray-400">
              Step {currentStep + 1} <span className="text-gray-400">of</span>{" "}
              {visualizationData.out.length}
            </span>
            {/* Progress Bar */}
            {visualizationData && (
              <div className="mb-4">
                <div className="w-full bg-gray-700 rounded-full h-3 overflow-hidden">
                  <div
                    className="bg-gradient-to-r from-blue-500 to-indigo-500 h-full rounded-full transition-all duration-300 ease-out relative"
                    style={{
                      width: `${
                        ((currentStep + 1) / visualizationData.out.length) * 100
                      }%`,
                    }}
                  >
                    <div className="absolute inset-0 bg-white opacity-20 animate-pulse"></div>
                  </div>
                </div>
                <div className="flex justify-between text-xs text-gray-500 mt-1">
                  <span>Start</span>
                  <span>End</span>
                </div>
              </div>
            )}

            <div className="flex justify-evenly gap-2">
              <Button
                className="bg-gray-800 text-slate-300 hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed flex-1 transition-all"
                onClick={handleStart}
                disabled={currentStep === 0}
              >
                ⏮ Start
              </Button>
              <Button
                className="bg-gray-800 text-slate-300 hover:bg-gray-700 disabled:opacity-50 disabled:cursor-not-allowed flex-1 transition-all"
                onClick={handlePrevStep}
                disabled={currentStep === 0}
              >
                ◀ Prev
              </Button>
              <Button
                className="bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex-1 transition-all"
                onClick={handleNextStep}
                disabled={
                  !visualizationData ||
                  currentStep === visualizationData.out.length - 1
                }
              >
                Next ▶
              </Button>
              <Button
                className="bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex-1 transition-all"
                onClick={handleEnd}
                disabled={
                  !visualizationData ||
                  currentStep === visualizationData.out.length - 1
                }
              >
                End ⏭
              </Button>
            </div>
            {currentFrame && (
              <div className="mt-3 text-sm text-gray-400 flex justify-between">
                <span>Step: {currentFrame.stepNo}</span>
                {/* <span>
                  Step {currentStep + 1}{" "}
                  <span className="text-gray-400">of</span>{" "}
                  {visualizationData.out.length}
                </span> */}
                <span>
                  Line: {currentFrame.lastLineNo} → {currentFrame.nextLineNo}
                </span>
              </div>
            )}
          </div>
          <OutputPanel output={currentFrame?.printedString || ""} />
        </div>

        {/* RIGHT PANEL - VISUALIZATION */}
        <div id="right" className="w-[53%] h-full overflow-y-auto px-4">
          {!visualizationData ? (
            <div className="flex items-center justify-center h-full">
              <div className="text-gray-400 text-center">
                <div className="text-xl mb-2">Loading visualization...</div>
                <div className="text-sm">Please wait</div>
              </div>
            </div>
          ) : currentFrame ? (
            <div className="space-y-4">
              {/* Call Stack Section */}
              <div className="bg-[#1e1e1e] rounded-lg p-4">
                <h2 className="text-lg font-bold text-blue-400 mb-3 flex items-center">
                  <span className="mr-2">📚</span> Call Stack
                </h2>
                <div className="space-y-3">
                  {currentFrame.frameList.map((frame, idx) => (
                    <div
                      key={idx}
                      className="bg-gray-800 border-l-4 border-blue-500 p-3 rounded"
                    >
                      <div className="font-semibold text-blue-300 mb-2 text-sm">
                        {frame.objectName}
                      </div>
                      <div className="text-xs text-gray-400 mb-2">
                        Class:{" "}
                        <span className="font-mono text-blue-400">
                          {frame.className}
                        </span>{" "}
                        | Line: {frame.lineno}
                      </div>

                      {/* Local Variables */}
                      <div className="mt-2">
                        <div className="text-xs font-semibold text-gray-300 mb-2">
                          Local Variables:
                        </div>
                        <div className="space-y-1 bg-gray-900 rounded p-2">
                          {Object.keys(frame.locals).length > 0 ? (
                            Object.entries(frame.locals).map(([key, value]) => (
                              <div
                                key={key}
                                className="flex items-center gap-2 text-sm"
                              >
                                <span className="font-mono text-gray-300 min-w-[80px]">
                                  {key}:
                                </span>
                                <span className="flex-1">
                                  {renderValue(value)}
                                </span>
                              </div>
                            ))
                          ) : (
                            <div className="text-gray-500 text-xs">
                              No local variables
                            </div>
                          )}
                        </div>
                      </div>

                      {frame._return !== null && (
                        <div className="mt-2 text-sm bg-gray-900 rounded p-2">
                          <span className="text-gray-300">Return value: </span>
                          {renderValue(frame._return)}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </div>

              {/* Heap Memory Section - Only Active Objects */}
              {activeHeapRefs.size > 0 && (
                <div className="bg-[#1e1e1e] rounded-lg p-4">
                  <h2 className="text-lg font-bold text-purple-400 mb-3 flex items-center">
                    <span className="mr-2">🗄️</span> Heap Memory
                    <span className="ml-2 text-xs text-gray-500">
                      ({activeHeapRefs.size} active objects)
                    </span>
                  </h2>
                  <div className="space-y-2">
                    {Array.from(activeHeapRefs).map((objId) => {
                      const obj = currentFrame.heap[objId];
                      if (!obj) return null;
                      return (
                        <div key={objId}>{renderHeapObject(objId, obj)}</div>
                      );
                    })}
                  </div>
                </div>
              )}

              {/* Current Execution Info */}
              <div className="bg-[#1e1e1e] rounded-lg p-4">
                <h2 className="text-lg font-bold text-green-400 mb-3 flex items-center">
                  <span className="mr-2">ℹ️</span> Execution Info
                </h2>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-gray-400">Current Method:</span>
                    <span className="text-blue-400 font-mono text-xs">
                      {currentFrame.currentObjectName}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-400">Event Type:</span>
                    <span
                      className={`font-semibold ${
                        currentFrame.event === "call"
                          ? "text-green-400"
                          : currentFrame.event === "return"
                          ? "text-red-400"
                          : "text-yellow-400"
                      }`}
                    >
                      {currentFrame.event.toUpperCase()}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-400">Executing Line:</span>
                    <span className="text-blue-400">
                      {currentFrame.nextLineNo}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-400">Previous Line:</span>
                    <span className="text-gray-400">
                      {currentFrame.lastLineNo}
                    </span>
                  </div>
                </div>
              </div>

              {/* Error Display */}
              {currentFrame.errorMessage && (
                <div className="bg-red-900 bg-opacity-30 border border-red-600 rounded-lg p-4">
                  <h2 className="text-lg font-bold text-red-400 mb-2 flex items-center">
                    <span className="mr-2">⚠️</span> Error
                  </h2>
                  <div className="text-red-300 text-sm font-mono">
                    {currentFrame.errorMessage}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="flex items-center justify-center h-full">
              <div className="text-gray-400 text-center">
                <div className="text-xl mb-2">No visualization data</div>
                <div className="text-sm">Run the code to see visualization</div>
              </div>
            </div>
          )}
        </div>

        <button
          className="absolute top-0 right-0 m-3 px-2 py-0.2 text-gray-400 rounded hover:text-gray-300"
          onClick={() => {
            setIsModalOpen(false);
          }}
        >
          <i className="ri-close-fill text-2xl"></i>
        </button>
      </div>
    </>
  );
};

export default Visualize2;
