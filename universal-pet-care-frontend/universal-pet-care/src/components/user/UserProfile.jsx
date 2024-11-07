/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import UserImage from "../common/UserImages";
import { Link } from "react-router-dom";
import ImageUploaderModal from "../modals/ImageUploaderModal";
import ChangePasswordModal from "../modals/ChangePasswordModal";
import { Col, Row, Card, ListGroup, Button, Container } from "react-bootstrap";
import DeleteConfirmationModal from "../modals/DeleteConfirmationModal";
import style from "../../components/user/UserProfile.module.css";

import PropTypes from "prop-types";

const UserProfile = ({ user, handleRemovePhoto, handleDeleteAccount }) => {
    const [showImageUploaderModal, setShowImageUploaderModal] = useState(false);
    const [showChangePasswordModal, setShowChangePasswordModal] =
        useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);

    const currentUserId = localStorage.getItem("userId");

    const isCurrentUser = user.id == currentUserId;

    const handleShowImageUploaderModal = () => {
        setShowImageUploaderModal(true);
    };
    const handleCloseImageUploaderModal = () => {
        setShowImageUploaderModal(false);
    };

    const handleShowChangePasswordModal = () => {
        setShowChangePasswordModal(true);
    };
    const handleCloseChangePasswordModal = () => {
        setShowChangePasswordModal(false);
    };

    const handleCloseDeleteModal = () => {
        setShowDeleteModal(false);
    };

    const handleShowDeleteModal = (userId) => {
        setUserToDelete(userId);
        setShowDeleteModal(true);
    };

    const handleDeleteAndCloseModal = async () => {
        try {
            await handleDeleteAccount();
            setShowDeleteModal(false);
        } catch (error) {
            console.error(error.message);
        }
    };

    return (
        <Container>
            <DeleteConfirmationModal
                show={showDeleteModal}
                onHide={handleCloseDeleteModal}
                onConfirm={handleDeleteAndCloseModal}
                itemToDelete={"user account"}
            />
            <React.Fragment>
                <Row>
                    <Col md={3}>
                        <Card className="text-center mb-3 shadow">
                            <Card.Body>
                                <UserImage
                                    userId={user.id}
                                    userPhoto={user.photo}
                                />
                            </Card.Body>

                            {isCurrentUser && (
                                <div className="text-center">
                                    <p>
                                        {" "}
                                        <Link
                                            to={"#"}
                                            onClick={
                                                handleShowImageUploaderModal
                                            }
                                        >
                                            Update Photo
                                        </Link>
                                    </p>

                                    <ImageUploaderModal
                                        userId={user.id}
                                        show={showImageUploaderModal}
                                        handleClose={
                                            handleCloseImageUploaderModal
                                        }
                                    />
                                    <p>
                                        {" "}
                                        <Link
                                            to={"#"}
                                            onClick={handleRemovePhoto}
                                        >
                                            Remove Photo
                                        </Link>
                                    </p>

                                    <p>
                                        {" "}
                                        <Link
                                            to={"#"}
                                            onClick={
                                                handleShowChangePasswordModal
                                            }
                                        >
                                            Change Password
                                        </Link>
                                    </p>

                                    <ChangePasswordModal
                                        userId={user.id}
                                        show={showChangePasswordModal}
                                        handleClose={
                                            handleCloseChangePasswordModal
                                        }
                                    />
                                </div>
                            )}
                        </Card>

                        {isCurrentUser && (
                            <Card.Body>
                                <div className="d-flex justify-content-center mb-4">
                                    <div className="mx-2">
                                        <Link
                                            to={`/update-user/${user.id}/update`}
                                            className="btn btn-warning btn-sm"
                                        >
                                            Edit profile
                                        </Link>
                                    </div>
                                    {/* <div className="mx-2">
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={handleShowDeleteModal}
                                        >
                                            Close account
                                        </Button>
                                    </div> */}
                                </div>
                            </Card.Body>
                        )}
                    </Col>

                    <Col md={8}>
                        <Card className="mb-3 shadow">
                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>First Name :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.firstName}</Card.Text>
                                </Col>
                            </Card.Body>

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>Last name :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.lastName}</Card.Text>
                                </Col>
                            </Card.Body>

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>Gender :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.gender}</Card.Text>
                                </Col>
                            </Card.Body>

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>Email :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.email}</Card.Text>
                                </Col>
                            </Card.Body>

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>Mobile :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.phoneNumber}</Card.Text>
                                </Col>
                            </Card.Body>

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>User Type :</Col>
                                <Col md={4}>
                                    <Card.Text>{user.userType}</Card.Text>
                                </Col>
                            </Card.Body>

                            {user.userType === "VET" && (
                                <Card.Body className="d-flex align-items-center">
                                    <Col md={4}>Specialization : </Col>
                                    <Col md={4}>
                                        <Card.Text>
                                            {user.specialization}
                                        </Card.Text>
                                    </Col>
                                </Card.Body>
                            )}

                            <Card.Body className="d-flex align-items-center">
                                <Col md={4}>Account status : </Col>
                                <Col md={4}>
                                    <Card.Text
                                        className={
                                            user.enabled
                                                ? style.active
                                                : style.inactive
                                        }
                                    >
                                        {user.enabled ? "Active" : "Inactive"}
                                    </Card.Text>
                                </Col>
                            </Card.Body>
                        </Card>

                        {/* <Card className="mb-3 shadow">
                            <Card.Body className="d-flex align-items-center">
                                <Col md={2}>Role(s) :</Col>
                                <Col md={4}>
                                    <ListGroup variant="flush">
                                        {user.roles &&
                                            user.roles.map((role, index) => (
                                                <ListGroup.Item
                                                    key={index}
                                                    className="text-success"
                                                >
                                                    {role
                                                        ? role.replace(
                                                              "ROLE_",
                                                              ""
                                                          )
                                                        : ""}
                                                </ListGroup.Item>
                                            ))}
                                    </ListGroup>
                                </Col>
                            </Card.Body>
                        </Card> */}
                    </Col>
                </Row>
            </React.Fragment>
        </Container>
    );
};

UserProfile.propTypes = {
    user: PropTypes.object.isRequired,
    handleRemovePhoto: PropTypes.func.isRequired,
    handleDeleteAccount: PropTypes.func.isRequired,
};
export default UserProfile;
