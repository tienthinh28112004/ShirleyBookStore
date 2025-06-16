import { useContext, useEffect, useState } from "react";
import AuthContext from "../context/AuthContext";
import {getProfileInfo} from "../service/UserService"

export const useUserProfile = () => {
  const authContext = useContext(AuthContext);
  const [lengthCart,setLengthCart]=useState(0);
  const [avatar, setAvatar] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!authContext.authenticated) {
      setLengthCart(0);
      setAvatar(null);
      setLoading(false);
      return;
    }

    const fetchUserProfile = async () => {
      try {
        const response = await getProfileInfo();
        setAvatar(response.result.avatarUrl);
        setLengthCart(response.result.cart.totalElements)
        console.log(response);
        console.log(avatar);
        console.log(30);
      } catch (error) {
        console.log(error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, [authContext]);

  return { avatar, loading,lengthCart };
};