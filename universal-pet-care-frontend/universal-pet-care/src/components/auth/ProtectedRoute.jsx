/* eslint-disable no-unused-vars */
import React from "react";
import { useLocation, Outlet, Navigate } from "react-router-dom";

import PropTypes from "prop-types";

const ProtectedRoute = ({ children, allowedRoles = [], useOutlet = false }) => {
    const isAuthenticated = localStorage.getItem("authToken");
    const userRoles = JSON.parse(localStorage.getItem("userRoles")) || [];
    const location = useLocation();

    if (!isAuthenticated) {
        // Redirect to login and remember the last location
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    const userRolesLower = userRoles.map((role) => role.toLowerCase());
    const allowedRolesLower = allowedRoles.map((role) => role.toLowerCase());

    const isAuthorized = userRolesLower.some((userRole) =>
        allowedRolesLower.includes(userRole)
    );

    if (isAuthorized) {
        
        return useOutlet ? <Outlet /> : children;
    } else {
        // Redirect to a default or unauthorized access page if the user doesn't have an allowed role
        return (
            <Navigate to="/unauthorized" state={{ from: location }} replace />
        );
    }
};

ProtectedRoute.propTypes = {
    children: PropTypes.node.isRequired,
    allowedRoles: PropTypes.arrayOf(PropTypes.string),
    useOutlet: PropTypes.bool,
};

export default ProtectedRoute;
