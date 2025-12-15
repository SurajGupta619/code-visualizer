import React from "react";
function Card({ title, description, link }) {

  return (
    <div
      
    >
      <h2 className="text-2xl font-semibold text-blue-400">{title}</h2>
      <p className="mt-2 text-gray-300">{description}</p>
    </div>
  );
}

export default Card;
