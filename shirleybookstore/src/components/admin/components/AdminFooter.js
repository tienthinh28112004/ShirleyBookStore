import React from "react";
import {CFooter} from "@coreui/react";

const AdminFooter = () =>{
    return (
        <CFooter className="px-4">
            <div>
                <a
                    href="https://www.facebook.com/tien.thinh.573993"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    SERLEY BOOK
                </a>
                <span className="ms-1">&copy; 2024 creativeLabs.</span>
            </div>
            <div className="ms-auto">
                <span className="me-1">Powered by</span>
                <a
                    href="https://www.facebook.com/tien.thinh.573993"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Tiến Thịnh
                </a>
            </div>
        </CFooter>
    );
};

export default React.memo(AdminFooter);