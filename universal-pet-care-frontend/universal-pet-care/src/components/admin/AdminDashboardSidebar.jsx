/* eslint-disable no-unused-vars */
import React from "react";
import {
    BsFillHospitalFill,
    BsGrid1X2Fill,
    BsX,
    BsPeopleFill,
} from "react-icons/bs";
import PropTypes from "prop-types";

const AdminDashboardSidebar = ({
    openSidebarToggle,
    OpenSidebar,
    onNavigate,
    activeTab,
}) => {
    return (
        <aside
            id="sidebar"
            className={openSidebarToggle ? "sidebar-responsive" : ""}
        >
            <div className="sidebar-title">
                <div className="sidebar-brand">
                    <BsFillHospitalFill className="icon-header" /> UniPet Care
                </div>
                <span className="icon icon-close" onClick={OpenSidebar}>
                    <BsX />
                </span>
            </div>

            <ul className="sidebar-list">
                <li
                    className={`sidebar-list-item ${
                        activeTab === "overview" ? "active" : ""
                    }`}
                    onClick={() => onNavigate("overview")}
                >
                    <a href="#">
                        <BsGrid1X2Fill className="icon" /> Dashboard Overview
                    </a>
                </li>

                <li
                    className={`sidebar-list-item ${
                        activeTab === "veterinarians" ? "active" : ""
                    }`}
                    onClick={() => onNavigate("veterinarians")}
                >
                    <a href="#">
                        <BsPeopleFill className="icon" /> Veterinarians
                    </a>
                </li>

                <li
                    className={`sidebar-list-item ${
                        activeTab === "patients" ? "active" : ""
                    }`}
                    onClick={() => onNavigate("patients")}
                >
                    <a href="#">
                        <BsPeopleFill className="icon" /> Patients
                    </a>
                </li>
            </ul>
        </aside>
    );
};

AdminDashboardSidebar.propTypes = {
    openSidebarToggle: PropTypes.bool.isRequired,
    OpenSidebar: PropTypes.func.isRequired,
    onNavigate: PropTypes.func.isRequired,
    activeTab: PropTypes.string.isRequired,
};

export default AdminDashboardSidebar;
