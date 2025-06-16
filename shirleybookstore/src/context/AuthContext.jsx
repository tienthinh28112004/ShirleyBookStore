import { createContext, useEffect, useState } from "react";
import {introspect} from "../service/AuthenticationService";

const AuthContext = createContext({});


export const AuthProvider =({children}) => {
    const [authenticated,setAuthenticated] = useState(false);
    // const [loggedOut,setLoggedOut] = useState(true);

    const refresh = async()=>{
        const accessToken = sessionStorage.getItem("accessToken");
        console.log(accessToken);
        if(!accessToken){
            // setLoggedOut(true);
            setAuthenticated(false);
            return ;
        }
        try{
            const result = await introspect();
            console.log(result);
            setAuthenticated(result.valid);
            console.log(result.valid);
            // setLoggedOut(!result.valid);
        }catch(error){
            console.error("Error introspecting token:",error);
            setAuthenticated(false);
            // setLoggedOut(true);
        }
    }
    
    useEffect(()=>{
        refresh();
    },[]);
    return (//đánh dấu là có thể mang để xác thực khắp nới
        <AuthContext.Provider value={{authenticated,refresh}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;