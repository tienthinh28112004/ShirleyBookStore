import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { introspect } from "../../service/AuthenticationService";

const LoadingSpinner = () => (
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <div className="spinner-border text-primary" role="status">
      <span className="sr-only">Loading...</span>
    </div>
  </div>
);

export const PrivateRoute = ({ children }) => {
  const [isValidToken, setIsValidToken] = useState(null);
  const accessToken = sessionStorage.getItem("accessToken");

  useEffect(() => {
    if (!accessToken) {
      setIsValidToken(false);
      return;
    }
    introspect()
      .then((data) => {
        if (data?.valid) {
          setIsValidToken(true);
        } else {
          setIsValidToken(false);
        }
      })
      .catch(() => {
        setIsValidToken(false);
      });
  }, [accessToken]);

  if (isValidToken === null) {
    return <LoadingSpinner />;
  }

  return isValidToken ? children : <Navigate to="/login" />;
};