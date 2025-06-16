//dùng để phân quyền đảm bảo người dùng hợp lệ mới được vào

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import LoadingSpinner from "../../utils/LoadingSpinner";

export const Authorization = ({children,requiredRole}) =>{
    const navigate = useNavigate();

    const [isLoading,setIsLoading] = useState(true);
    const [isAuthorized,setIsAuthorized] = useState(false);//kiểm soát quá trình authorization

    const accessToken = sessionStorage.getItem('accessToken');

    useEffect(()=>{
        if(!accessToken){
            navigate('/login');//nếu chưa có token thì chuyển hướng đến trang login
            return ;
        }

        const roles = Array.isArray(requiredRole)?requiredRole:[requiredRole];
        //kiểm tra xem requiredRole có là 1 mảng hay không,nếu có thì gán trực tiếp cho role còn nếu không thì tạo mảng chứ phần tử đó rồi gán cho role

        fetch("http://localhost:8080/ApiWebManga/auth/introspect",{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                    'Authorization': `Bearer ${accessToken}`,
                },
                body: JSON.stringify({accessToken}),
            })
            .then(response =>{
                if(!response.ok){
                    return response.json().then(errorData =>{
                        throw new Error(errorData.message);
                    });
                }
                return response.json();
            })
            .then(data =>{
                if(data.result && data.result.valid){
                    const userRoles =Array.isArray(data.result.scope)? data.result.scope:[ data.result.scope];
                    //backend chắc chắn trả ra 1 mảng rồi nhưng làm như anyf để chắc chắn
                    if(roles.some(role =>userRoles.includes(role))){
                        //kiểm tra xem role yêu cầu có trùng với role 1 role trong đây không
                        setIsAuthorized(true);//đánh dấu có quyền
                    }else{
                        navigate('/accessdenied');//nếu không có quyền chuyển về trang thông báo quyền
                    }
                }else{
                    throw new Error('Invalid token');
                }
            })
            .catch(error =>{
                console.error('Error during introspect:',error);
                navigate('/login');//nếu như có lỗi thì chuyển về trang login
            })
            .finally(()=>{
                setIsLoading(false);//kết thúc trạng thái kiểm tra
            });
        },[navigate,accessToken,requiredRole]);

        if(isLoading){
            return <LoadingSpinner />
        }
        if(!isAuthorized){
            return null;
        }
        return children;
    };