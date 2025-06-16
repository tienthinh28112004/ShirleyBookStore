import { NavLink } from 'react-router-dom';

export const NavigationMenu = ({isActive,underlineRef}) =>{
    return (
        <div className="navbar-nav mx-auto py-0 position-relative">
            <NavLink to="/" className={`nav-item nav-link rounded ${isActive('/home') ? 'active' : ''}`}>Home</NavLink>
            <NavLink to="/about" className={`nav-item nav-link rounded ${isActive('/about') ? 'active' : ''}`}>About</NavLink>
            <NavLink to="https://www.fahasa.com/" className={`nav-item nav-link rounded ${isActive('/courses') ? 'active' : ''}`}>Books</NavLink>
            <NavLink to="https://www.facebook.com/groups/congdongdocsachvietnam/?locale=vi_VN" className={`nav-item nav-link rounded ${isActive('/comunity') ? 'active' : ''}`}>Community</NavLink>
            <NavLink to="/register-teacher" className={`nav-item nav-link rounded ${isActive('/contact') ? 'active' : ''}`}>Contact</NavLink>
            <div className="underline" ref={underlineRef}></div>
        </div>
    )
}