import { useState, useEffect } from "react";
import { GoogleGenerativeAI } from "@google/generative-ai";
import ChatHistory from "../../utils/ChatHistory";
import Loading from "../../utils/Loading";

const ChatBox = () => {
  const [userInput, setUserInput] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);

  const genAI = new GoogleGenerativeAI("AIzaSyB-qBS57oBLOqe-SsFcRscKaGeNutYQ9rM");
  const model = genAI.getGenerativeModel({ model: "gemini-2.0-flash" });

  const handleUserInput = (e) => {
    setUserInput(e.target.value);
  };

  const sendMessage = async () => {
    if (userInput.trim() === "") return;
    setIsLoading(true);

    try {
      const result = await model.generateContent(userInput);
      const response = await result.response;

      setChatHistory([
        ...chatHistory,
        { type: "user", message: userInput },
        { type: "bot", message: response.text() },
      ]);
    } catch {
      console.error("Error sending message");
    } finally {
      setUserInput("");
      setIsLoading(false);
    }
  };

  const clearChat = () => {
    setChatHistory([]);
  };

  useEffect(() => {
    const toggle = () => setIsChatOpen((prev) => !prev);
    window.addEventListener("toggleChatPopup", toggle);
    return () => window.removeEventListener("toggleChatPopup", toggle);
  }, []);

  return (
    <>
      {isChatOpen && (
        <div
          className="chat-popup shadow"
          style={{
            position: "fixed",
            bottom: "0px",
            right: "62px",
            width: "300px",
            height: "400px", // Tăng chiều cao tổng thể
            backgroundColor: "#fff",
            borderRadius: "12px",
            overflow: "hidden",
            boxShadow: "0 8px 16px rgba(0,0,0,0.2)",
            display: "flex",
            flexDirection: "column",
            zIndex: 999,
          }}
        >
          <div style={{ padding: "12px", borderBottom: "1px solid #eee" }}>
            <h4 className="text-center mb-0 fw-bold">ChatBot</h4>
          </div>

          <div
            style={{
              flex: 1,
              overflowY: "auto",
              padding: "12px",
              display: "flex",
              flexDirection: "column-reverse", // Tin nhắn mới sẽ đẩy lên trên
            }}
          >
            <div>
              <ChatHistory chatHistory={chatHistory} />
              <Loading isLoading={isLoading} />
            </div>
          </div>

          <div style={{ padding: "12px", borderTop: "1px solid #eee" }}>
            <div className="d-flex" style={{ marginBottom: "8px" }}>
              <input
                type="text"
                className="form-control me-2"
                placeholder="Type your message..."
                value={userInput}
                onChange={handleUserInput}
                onKeyPress={(e) => e.key === "Enter" && sendMessage()}
              />
              <button
                className="btn btn-primary"
                onClick={sendMessage}
                disabled={isLoading}
              >
                Send
              </button>
            </div>
            <button
              className="btn btn-secondary btn-sm w-100"
              onClick={clearChat}
            >
              Clear Chat
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatBox;