import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProfileInfo, updateProfile } from "../../../service/UserService";
import { removeAvatar, updateAvatar } from "../../../service/ProfileService";
import { FormUpdateProfile } from "./components/FromUpdateFrofile";
import AuthContext from "../../../context/AuthContext";
import { getOrderByUser } from "../../../service/OrderService";
import { addItemCart } from "../../../service/CartService";


export const Profile = () =>{
    const authContext = useContext(AuthContext);
    const accessToken = sessionStorage.getItem("accessToken");
    const [isUpdatingAvatar,setIsUpdatingAvartar] = useState(false);
    const [isRemovingAvatar,setIsRemovingAvatar] = useState(false);
    const [selectedImage,setSelectedImage] = useState(null);
    const [orders,setOrders]=useState([]);
    const [profileData,setProfileData] = useState({
        fullName:"",
        phoneNumber:"",
        dob:"",
    });
    useEffect(() => {
        document.title="Profile";
    });

    useEffect(() =>{
        getProfileInfo()
        .then((data) =>{
            if(data.result){
                setProfileData({
                    role:data.result.role||[],
                    createdAt:data.result.createdAt||"",
                    email:data.result.email||"",
                    avatarUrl: data.result.avatarUrl || "",
                    fullName: data.result.fullName||"",
                    phoneNumber:data.result.phoneNumber||"",
                    dob:data.result.birthday||"" 
                });
                setSelectedImage(
                    data.result.avatarUrl ||
                     "https://t4.ftcdn.net/jpg/05/49/98/39/360_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg"
                )
            }
        })
        .catch((error)=>{
            console.log("Get info user fail ",error);
        });
    },[]);

    useEffect(() => {
        orderByUser();
    },[]);
    console.log(orders);
    const orderByUser =async() =>{
        try{
            const response = await getOrderByUser();
            setOrders(response.result);
        }catch(error){
            console.log("lỗi:)))")
        }
    }

    const handleUpdateAvatar = (event) => {
        event.preventDefault();

        const fileInput = document.getElementById("url-update-avatar");
        const file = fileInput.files[0];

        if(!file){
            console.log("Please select an image first")
            return;
        }
        
        const formData = new FormData();
        formData.append("file",file);

        setIsUpdatingAvartar(true);

        updateAvatar(formData)
        .then((response) =>{
            if(response && response.message === "Profile updated successfully"){
                authContext.refresh();
                console.log(response.message);
            }else{
                console.log("Avatar update failed")
            }
        })
        .catch((error) =>{
            console.log(error);
        })
        .finally(() => {
            setIsUpdatingAvartar(false);
        });
    };

    const handleRemoveAvatar = () =>{
        setIsRemovingAvatar(true);

        removeAvatar()
        .then(() =>{
            setSelectedImage(
                "https://t4.ftcdn.net/jpg/05/49/98/39/360_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg"
            );
            console.log("Avatar removed successfully!");
        })
        .catch((error) =>{
            console.log("Failed to removed successfully!")
        })
        .finally(() =>{
            setIsRemovingAvatar(false);
        })
    }
     const addItemCarts = async (id,quantity) =>{
            try{
                const response = await addItemCart(id,quantity);
                if(response){
                    console.log("Sản phẩm đã được thêm vào giỏ hàng");
                }
            }catch(error){
                console.log("thêm sản phẩm vào giỏ hàng thất bại")
            }
        };
    const handleUpdateProfile = () =>{
        const filteredData = {};
        Object.keys(profileData).forEach((key) =>{
            if(profileData[key]!==null && profileData[key] !== ""){
                filteredData[key] = profileData[key];
            }
        });
        console.log(filteredData);
        updateProfile(filteredData)
        .then(() => {
            console.log("Profile updated successfully")
        })
        .catch((error) =>{
            console.log("Error updating profile");
        });
    };

    const handleInputChange = (e) =>{
        const {name,value} = e.target;
        setProfileData({
            ...profileData,
            [name]:value,
        });
    };

    const handleOnChangeAvatar = (event) =>{
        // Đọc ảnh người dùng chọn và hiển thị ngay ảnh preview trong UI mà chưa cần upload lên server.
        const file = event.target.files[0];
        if(file){
            const reader = new FileReader();
            reader.onloadend = () =>{
                setSelectedImage(reader.result)
            };
            reader.readAsDataURL(file);//readAsDataURL để đọc file và trả về chuỗi base64.
        }
    }
    return (
        <div>
            <div className="page-section-title" style={{marginTop:"200px",marginBottom:"50px",textAlign:"center",justifyContent:"center",display:"flex"}}>
                <h1>Profile User</h1>
            </div>
            {/* <div className="content-profile"> */}
                <FormUpdateProfile 
                    handleUpdateProfile={handleUpdateProfile}
                    selectedImage={selectedImage}
                    handleOnChangeAvatar={handleOnChangeAvatar}
                    handleUpdateAvatar={handleUpdateAvatar}
                    isUpdatingAvatar={isUpdatingAvatar}
                    isRemovingAvatar={isRemovingAvatar}
                    handleRemoveAvatar={handleRemoveAvatar}
                    handleInputChange={handleInputChange}
                    profileData={profileData}
                    orders={orders}
                    addItemCarts={addItemCarts}
                />
            {/* </div> */}
        </div>
        
    )
}