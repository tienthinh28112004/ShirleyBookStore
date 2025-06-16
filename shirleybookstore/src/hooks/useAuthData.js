import { useContext, useEffect, useState } from "react";
import { introspect } from "../service/AuthenticationService";
import AuthContext from "../context/AuthContext";

export const useAuthData = () => {
    const authContext = useContext(AuthContext)
    const [role, setRole] = useState([]);
    const [loading, setLoading] = useState(true);
  
    useEffect(() => {
      if (!authContext.authenticated) {
        console.log("User not authenticated");
        setRole([]);
        setLoading(false);
        return;
      }
  
      introspect()
        .then((data) => {
          if (data.valid) {
            setRole(data.scope);
            console.log("Fetched roles:", data.scope);
          } else {
            setRole([]);
          }
          setLoading(false);
        })
        .catch(() => {
          setRole([]);
          setLoading(false);
        });
    }, [authContext]);
  
    return { role, loading };
  };
  