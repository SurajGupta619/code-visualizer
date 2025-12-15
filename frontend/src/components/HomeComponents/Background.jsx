import React from "react";

function Background() {
  return (
    <div className="absolute top-0 left-0 w-full h-full -z-10 overflow-hidden">
      <div className="absolute w-[200%] h-[200%] bg-gradient-to-r from-blue-900 via-blue-900 to-blue-900 opacity-20 animate-gradient"></div>
    </div>
  );
}

export default Background;
