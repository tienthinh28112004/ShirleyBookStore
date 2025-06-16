import { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router-dom";
import { getUserById } from "../../../../service/UserService";
import "../../css/UserDetail.css";


const UserDetail = () =>{
    const [userDetails,setUserDetails] = useState(null);
    const {id} = useParams();
    const navigate = useNavigate();

    useEffect(() =>{
        const fetchDetails = async () =>{
            try{
                const data = await getUserById(id);
                setUserDetails(data.result);
            }catch(error){
                console.log(error);
            }
        };
        fetchDetails();
    },[id]);

    if(!userDetails){
        return <div>Loading...</div>
    }

    return (
        <div className="user-detail">
            <div className="header-section">
                <img 
                    src={userDetails.avatarUrl}
                    alt="Avatar"
                    className="user-avatar"
                />
                <div className="user-basic-info">
                    <div className="name-and-points">
                        <h2 className="user-name">{userDetails.fullName}</h2>
                    </div>
                    <p className="user-email">
                        {userDetails.email || "N/A"}
                    </p>
                    <p>
                        {userDetails.phoneNumber || "N/A"}
                    </p>
                </div>
            </div>
            <div className="detail-section">
                <div className="info-card">
                    <h3>Date of Birth</h3>
                    <p>{userDetails.birthday
                        ?new Date(userDetails.birthday).toLocaleDateString()
                        :"N/A"}
                    </p>
                </div>
                <div className="info-card">
                    <h3>Role</h3>
                    <p>
                        {userDetails.role || "N/A"}
                    </p>
                </div>
                <div className="info-card">
                    <h3>Created At</h3>
                    <p>
                        {userDetails.createdAt
                            ?new Date(userDetails.createdAt).toLocaleDateString()
                            :"N/A"}
                    </p>
                </div>
            </div>
        </div>
    )
}

export default UserDetail;