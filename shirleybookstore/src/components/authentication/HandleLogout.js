import { useContext } from "react";
import { useNavigate } from "react-router-dom"
import AuthContext from "../../context/AuthContext";

export const HandleLogout = () =>{//truyền set vào để cập nhật trạng thái
    const navigate = useNavigate();
    const authContext = useContext(AuthContext);
    // const { setLoggedOut } = useContext(AuthContext);

    const handleLogout = async ()=>{
        const accessToken = sessionStorage.getItem('accessToken');

        //Trường hơp không có token redirect đến trang login luôn
        if(!accessToken){
            console.log('No token found');
            //setLoggedOut(true);//Cập nhật trạng thái logout
            navigate('/login');//không có token thì chuyển vè trang login
            return ;
        }

        //trường hợp có token thì xử lý cả fontend,bakend
        sessionStorage.clear();
        //setLoggedOut(true);//nếu có token thì xóa và setLoggout=true(đã đăng nhập)

        try{
            const response = await fetch("http://localhost:8080/ApiWebManga/auth/logout",{
                method: 'POST',
                credentials: 'include', // Thêm dòng này để gửi kèm HttpOnly cookies
                headers:{
                    'Content-Type':'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({accessToken})
            });

            if(response.ok){
                sessionStorage.clear();
                authContext.refresh();
                navigate('/login');//gọi hàm logout thành công thì chuyển nó đến trang login luôn
            }else{
                const errorData = await response.json();//neeud mà axios thì mới cần .data nữa
                console.log('Logout Failed: ',errorData.message || response.statusText);
            }
        }catch(error){
            console.log('Logout error: ',error);
        }
    };
    return {handleLogout};
}