import { useEffect, useState } from "react"
import { Link,useNavigate } from "react-router-dom";
import { register, verifyEmail } from "../../../service/AuthenticationService";
import { motion } from "framer-motion";
import { RegisterForm } from "./components/RegisterForm";
export const Register =() =>{
    useEffect(() => {
        document.title = "Register Page";
    });

    const [formData,setformData] = useState({
        email:"",
        password:"",
        fullName:"",
        otp:""
    });

    const [formErrors,setFormErrors] = useState({
        email:"",
        password:"",
        fullName:"",
        otp:""
    });

    const [isOtpSent,setIsOtpSent] = useState(false); //Quản lý trạng thái OTP
    const [errorMessage,setErrorMessage] = useState("");//Hiển thị thông báo lỗi
    const navigate = useNavigate();

    //Xử lý thay đổi giá trị các input
    const handleInputChange = (e) =>{
        const { name,value } = e.target;

        setformData({
            ...formData,
            [name]:value,
        });

        setFormErrors({
            ...formErrors,
            [name]:value ? "":formErrors[name],
        });
    }

    //Xử lý lỗi khi để trống
    const handleInputBlur = (event) =>{
        const {name,value} = event.target;
        if(!value){
            setFormErrors({
                ...formErrors,
                [name]:"This field cannot be left blank"
            });
        }
    };

    //Kiểm tra email và gửi OTP nếu chưa tồn tại
    const handleRegisterSubmit = async(e) =>{
        e.preventDefault();

        if(formData.password.length <6){
            setErrorMessage("Password must contain 1 uppercase letter, 1 lowercase letter, 1 special character and no spaces")
            return ;
        }
        try{
            const response=await register(formData.email,formData.password,formData.fullName);
            if(response.code === 1000){
                setIsOtpSent(true);
                setErrorMessage("");
            }else{
                setErrorMessage("Error sending OTP, please try again.");
            }
        }catch(error){
            console.log(error);
            setErrorMessage("An error occurred while checking email.");
        }
    };

    const handleOtpSubmit = async (e) =>{
        e.preventDefault();
        try{
            const data=await verifyEmail(formData.otp);
            if (data.result) {
                console.log("User registered successfully");
                navigate("/login");
              } else {
                setErrorMessage("OTP is invalid or expired");
                console.error("Error during registration:", data.message);
              }
        }catch(error){
            console.error(error);
            setErrorMessage("OTP is invalid or expired");
        }
    }
    return (
        <motion.div
            initial={{opacity:0, x:100}}//hiệu ứng ban đầu ẩn và dịch phải
            animate={{opacity: 1,x:0}} //Hiệu ứng khi hiển thị: hiện và dịch về phía vị trí gốc
            exit={{opacity: 0,x: -100}} //Hiệu ứng khi thoát: ẩn và dịch trái
            transition={{duration: 0.5}} //thời gian chuyển động
            className="content-page"
        >
            <section className="py-3 py-md-5 py-xl-8">
                <div className="container">
                    <div className="row">
                        <div className="col-12">
                            <div className="mb-5">
                                <h2 className="display-5 fw-bold text-center">Register</h2>
                                <p className="text-center m-0">
                                    Already have an account? <Link to="/login">Sign in</Link>
                                </p>
                            </div>  
                        </div>
                    </div>
                    <RegisterForm 
                        handleRegisterSubmit={handleRegisterSubmit}
                        errorMessage={errorMessage}
                        handleOtpSubmit={handleOtpSubmit}
                        formData={formData}
                        handleInputChange={handleInputChange}
                        handleInputBlur={handleInputBlur}
                        formErrors={formErrors}
                        isOtpSent={isOtpSent}
                    />
                </div>
            </section>
        </motion.div>
    )
};