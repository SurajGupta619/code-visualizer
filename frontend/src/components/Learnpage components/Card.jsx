export function Card({ children, className = "" }) {
    return (
      <div
        className={`rounded-lg border border-gray-700 shadow-md bg-gray-800 ${className}`}
      >
        {children}
      </div>
    );
  }
  
  export function CardContent({ children, className = "" }) {
    return (
      <div className={`p-4 rounded-lg ${className}`}>
        {children}
      </div>
    );
  }
  