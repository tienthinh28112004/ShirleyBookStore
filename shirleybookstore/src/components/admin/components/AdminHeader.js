import CIcon from "@coreui/icons-react";
import { CContainer, CHeader, CHeaderNav, CHeaderToggler, CNavItem, CNavLink } from "@coreui/react";
import { useDispatch, useSelector } from "react-redux"
import {
    cilBell,
    cilContrast,
    cilEnvelopeOpen,
    cilList,
    cilMenu,
    cilMoon,
    cilSun,
  } from "@coreui/icons";
import { NavLink } from "react-router-dom";
import AppHeaderDropdown from "./AppHeaderDropdown";
const AdminHeader = () =>{
    const dispath = useDispatch();
    const sidebarShow = useSelector((state) => state.sidebarShow);

    return (
        <CHeader position="sticky" className="mb-4 p-0">
            <CContainer className="border-bottom px-4" fluid>
                <CHeaderToggler
                    onClick={() => dispath({type:"set",sidebarShow:!sidebarShow})}
                    style={{marginInlineStart:"-14px"}}
                >
                    <CIcon icon={cilMenu} size="lg"/>
                </CHeaderToggler>
                <CHeaderNav className="d-none d-md-flex">
                    <CNavItem>
                        <CNavLink to="/dashboard" as={NavLink}>
                            Dashboard
                        </CNavLink>
                    </CNavItem>
                    <CNavItem>
                        <CNavLink href="#">Users</CNavLink>
                    </CNavItem>
                    <CNavItem>
                        <CNavLink href="#">Settings</CNavLink>
                    </CNavItem>
                </CHeaderNav>
                <CHeaderNav className="ms-auto">
                    <CNavItem>
                        <CNavLink href="#">
                            <CIcon icon={cilBell} size="lg" customClassName="nav-icon"/>
                        </CNavLink>
                    </CNavItem>
                    <CNavItem>
                        <CNavLink href="#">
                            <CIcon icon={cilList} size="lg" customClassName="nav-icon"/>
                        </CNavLink>
                    </CNavItem>
                    <CNavItem>
                        <CNavLink>
                            <CIcon icon={cilEnvelopeOpen} size="lg" customClassName="nav-icon"/>
                        </CNavLink>
                    </CNavItem>
                </CHeaderNav>
                <CHeaderNav>
                    <li className="nav-item py-1">
                        <div className="vr h-100 mx-2 text-body text-opacity-75"></div>
                    </li>
                    <li className="nav-item py-1">
                        <div className="vr h-100 mx-2 text-body text-opacity-75"></div>
                    </li>
                    <AppHeaderDropdown />
                </CHeaderNav>
            </CContainer>
            <CContainer className="px-4" fluid>

            </CContainer>
        </CHeader>
    );
};

export default AdminHeader;