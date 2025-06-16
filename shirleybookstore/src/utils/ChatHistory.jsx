import ReactMarkdown from "react-markdown";

const ChatHistory = ({ chatHistory }) => {
  return (
    <>
      {chatHistory.map((message, index) => (
        <div
          key={index}
          className={`d-flex align-items-start p-2 mb-2 rounded ${
            message.type === "user"
              ? "user-message"
              : "bot-message"
          }`}
          style={{
            backgroundColor: message.type === "user" ? "#f3f4f6" : "#dbeafe",
            color: message.type === "user" ? "#1f2937" : "#1e40af",
            padding: "0.5rem 1rem"
          }}
        >
          {message.type === "user" && (
            <span className="me-2 fw-bold" style={{ color: "#4b5563" }}></span>
          )}
          <div>
            <ReactMarkdown>{message.message}</ReactMarkdown>
          </div>
        </div>
      ))}
    </>
  );
};

export default ChatHistory;