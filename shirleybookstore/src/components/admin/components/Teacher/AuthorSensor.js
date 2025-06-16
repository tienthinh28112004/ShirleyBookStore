import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import TablePagination from "@mui/material/TablePagination";
import CIcon from "@coreui/icons-react";
import {
  cilSortAlphaDown,
  cilUser,
  cilEnvelopeClosed,
  cilCalendar,
  cilClipboard,
  cilCheckCircle,
  cilBan,
} from "@coreui/icons";
import "../../css/UserManage.css"; // Common CSS
// import "../../css/AuthorCensor.css"; // Specific CSS
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { banAuthor, updateRoleAuthor } from "../../../../service/UserService";
import { registerList } from "../../../../service/RegisterAuthorService";

const AuthorCensor = () => {
  const [applications, setApplications] = useState([]);
//   const [rowsPerPage, setRowsPerPage] = useState(7);

  const navigate = useNavigate();
  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    try {
      const response = await registerList();
      setApplications(response.result);
      console.log(response.result);
    } catch (error) {
      console.error("Error fetching Author applications:", error);
      toast.error("Failed to load Author applications.");
    }
  };

  const handleCertificationClick = (id) => {
    navigate(`/admin/Authors/detail/${id}`);
  };

  const handleApproval = async (id, approved) => {
    try {
      if (approved) {
        await updateRoleAuthor(id);
        toast.success(`Author application approved.`);
      } else {
        await banAuthor(id);
        toast.error(`Author application rejected.`, {
          className: "toast-error",
          icon: "❌",
        });
      }
      fetchApplications();
    } catch (error) {
      console.error("Error updating Author application status:", error);
      toast.error("Failed to update application status.");
    }
  };

  return (
    <div className="user-manage">
      <h2 className="user-manage-title">Author Application Censorship</h2>
      <div className="user-manage-table">
        <table>
          <thead>
            <tr>
              <th>STT</th>
              <th>
                <CIcon icon={cilUser} className="table-icon" /> Name
              </th>
              <th>
                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Email
              </th>
              <th>
                <CIcon icon={cilCalendar} className="table-icon" /> Description
              </th>
              <th>
                <CIcon icon={cilClipboard} className="table-icon" /> Detail
              </th>
              <th>
                <CIcon icon={cilCheckCircle} className="table-icon" /> Action
              </th>
            </tr>
          </thead>
          <tbody>
            {applications.map((app, index) => (
              <tr key={app.id} onClick={()=>handleCertificationClick(app.id)}>
                <td>{ index + 1}</td>
                <td>{app.name}</td>
                <td>{app.email}</td>
                <td>{app.description}</td>
                <td
                  className="certification-link"
                  onClick={() => handleCertificationClick(app.id)}
                >
                  CV
                </td>
                <td>
                  <button
                    className="approve-btn"
                    onClick={() => handleApproval(app.id, true)}
                  >
                    <CIcon icon={cilCheckCircle} /> Approve
                  </button>
                  <button
                    className="reject-btn"
                    onClick={() => handleApproval(app.id, false)}
                  >
                    <CIcon icon={cilBan} /> Reject
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
};

export default AuthorCensor;