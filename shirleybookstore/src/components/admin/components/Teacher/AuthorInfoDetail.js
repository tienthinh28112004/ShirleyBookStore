import React, { useEffect, useState } from "react";
// import "../../css/AuthorInforDetail.css";s
import { CIcon } from "@coreui/icons-react";
import {
  cilEnvelopeClosed,
  cilUser,
  cilPhone,
  cilDescription,
  cilFile,
  cilStar,
  cilCalendar,
} from "@coreui/icons";
import { Button } from "react-bootstrap";
import { FiFileText, FiAward } from "react-icons/fi";
import { FaFacebook } from "react-icons/fa";
import { useParams, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { banAuthor, getAuthorDetails, updateRoleAuthor } from "../../../../service/UserService";


const AuthorInforDetail = () => {
  const [AuthorDetails, setAuthorDetails] = useState(null);
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const data = await getAuthorDetails(id);
        setAuthorDetails(data.result);
      } catch (error) {}
    };

    fetchDetails();
  }, [id]);

  const handleOpenInNewTab = (url) => {
    if (url) {
      const fullUrl = `http://localhost:8080${url}`;
      window.open(fullUrl, "_blank", "noopener,noreferrer");
    } else {
      toast.error("File not found.");
    }
  };

  const handleApprove = async () => {
    try {
      await updateRoleAuthor(id);
      toast.success("Author approved successfully.");
      setTimeout(() => navigate("/admin/Authors/censor"), 3000);
    } catch (error) {
      console.error("Error approving Author:", error);
      toast.error("Failed to approve Author.");
    }
  };

  const handleReject = async () => {
    try {
      await banAuthor(id);
      toast.warn("Author rejected successfully.");
      setTimeout(() => navigate("/admin/Authors/censor"), 3000);
    } catch (error) {
      console.error("Error rejecting Author:", error);
      toast.error("Failed to reject Author.");
    }
  };

  if (!AuthorDetails) {
    return <div>Loading...</div>;
  }

  return (
    <div className="Author-info-detail">
      <ToastContainer position="top-right" autoClose={3000} />
      <div className="header-section">
        <img
          src={AuthorDetails.avatar}
          alt="Avatar"
          className="Author-avatar"
        />
        <div className="Author-basic-info">
          <div className="name-and-points">
            <div className="Author-name"><strong>Name: </strong>{AuthorDetails.name}</div>
          </div>
          <p className="Author-email">
            <CIcon icon={cilEnvelopeClosed} /> <strong>Email: </strong>{AuthorDetails.email}
          </p>
          <p className="Author-phone">
            <CIcon icon={cilPhone} /> <strong>Phone number: </strong>{AuthorDetails.phone || "N/A"}
          </p>
        </div>
      </div>

      <div className="detail-section">
        <div className="info-card">
          <h3>CV</h3>
          <Button
            variant="outline-primary"
            onClick={() => handleOpenInNewTab(AuthorDetails.cvUrl)}
          >
            <FiFileText /> View CV
          </Button>
        </div>
        <div className="info-card">
          <h3>Certificate</h3>
          <Button
            variant="outline-secondary"
            onClick={() => handleOpenInNewTab(AuthorDetails.certificate)}
          >
            <FiAward /> View Certificate
          </Button>
        </div>
        <div className="info-card">
          <h3>Facebook</h3>
          <p>
            <a
              href={AuthorDetails.facebookLink}
              target="_blank"
              rel="noopener noreferrer"
            >
              <FaFacebook style={{ color: "#3b5998" }} /> View Facebook Profile
            </a>
          </p>
        </div>
        <div className="info-card">
          <h3>Description</h3>
          <p>
            <CIcon icon={cilDescription} />{" "}
            {AuthorDetails.description || "N/A"}
          </p>
        </div>
        <div className="info-card">
          <h3>Years of Experience</h3>
          <p>
            <CIcon icon={cilCalendar} />{" "}
            {AuthorDetails.yearsOfExperience || "N/A"} years
          </p>
        </div>
      </div>

      <div className="action-section">
        <button className="approve-button" onClick={handleApprove}>
          Phê Duyệt
        </button>
        <button className="reject-button" onClick={handleReject}>
          Từ Chối
        </button>
      </div>
    </div>
  );
};

export default AuthorInforDetail;