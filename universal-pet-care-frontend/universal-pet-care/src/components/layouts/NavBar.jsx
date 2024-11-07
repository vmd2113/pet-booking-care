/* eslint-disable no-unused-vars */
import React from "react";
import { Navbar, Container, Nav, NavDropdown } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";

import { logout } from "../auth/AuthService";

const NavBar = () => {
    const isLoggedIn = localStorage.getItem("authToken");
    const userRoles = localStorage.getItem("userRoles") || [];
    const userId = localStorage.getItem("userId") || "";

    const handleLogout = () => {
        logout();
    };

    return (
        <Navbar expand="lg" sticky="top" className="nav-bg">
            <Container>
                <Navbar.Brand to={"/"} as={Link} className="nav-home">
                    uniPetcare
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link to={"/doctors"} as={Link}>
                            Meet Our Veterinarians
                        </Nav.Link>
                    </Nav>
                    <Nav>
                        <NavDropdown title="Account" id="basic-nav-dropdown">
                            {!isLoggedIn ? (
                                <React.Fragment>
                                    <NavDropdown.Item
                                        to={"/register-user"}
                                        as={Link}
                                    >
                                        Register
                                    </NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item to={"/login"} as={Link}>
                                        Login
                                    </NavDropdown.Item>
                                </React.Fragment>
                            ) : (
                                <React.Fragment>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item
                                        to={`/user-dashboard/${userId}/my-dashboard`}
                                        as={Link}
                                    >
                                        My Dashboard
                                    </NavDropdown.Item>

                                    {userRoles.includes("ROLE_ADMIN") && (
                                        <React.Fragment>
                                            <NavDropdown.Divider />
                                            <NavDropdown.Item
                                                to={`/admin-dashboard/${userId}/admin-dashboard`}
                                                as={Link}
                                            >
                                                Admin Dashboard
                                            </NavDropdown.Item>
                                        </React.Fragment>
                                    )}

                                    <NavDropdown.Divider />
                                    <NavDropdown.Item
                                        to={"#"}
                                        onClick={handleLogout}
                                    >
                                        Logout
                                    </NavDropdown.Item>
                                </React.Fragment>
                            )}
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default NavBar;
