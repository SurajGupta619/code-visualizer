export function Button({ children, onClick, className = "", type = "button" }) {
    return (
      <button
        type={type}
        onClick={onClick}
        className={`px-4 py-2 rounded-lg transition-colors duration-200 ${className}`}
      >
        {children}
      </button>
    );
  }  