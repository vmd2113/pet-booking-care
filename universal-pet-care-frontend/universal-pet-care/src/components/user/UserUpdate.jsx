/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getUserById, updateUser } from "./UserService";
import { Container, Form, Button, Card, Col } from "react-bootstrap";
import VetSpecializationSelector from "../veterinarian/VetSpecializationSelector";
import ProcessSpinner from "../common/ProcessSpinner";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import AlertMessage from "../common/AlertMessage";

const UserUpdate = () => {
    const [isProcessing, setIsProcessing] = useState(false);
    const { userId } = useParams();
    const navigate = useNavigate();

    const [userData, setUserData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        userType: "",
        gender: "",
        phoneNumber: "",
        specialization: "",
    });
    const {
        successMessage,
        setSuccessMessage,
        errorMessage,
        setErrorMessage,
        showSuccessAlert,
        setShowSuccessAlert,
        showErrorAlert,
        setShowErrorAlert,
    } = UseMessageAlerts();

    useEffect(() => {
        const getUserData = async () => {
            try {
                const data = await getUserById(userId);
                setUserData(data.data);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        getUserData();
    }, [userId]);

    const handleUserInputChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleUserUpdate = async (e) => {
        e.preventDefault();
        const updatedUserData = {
            firstName: userData.firstName,
            lastName: userData.lastName,
            gender: userData.gender,
            phoneNumber: userData.phoneNumber,
            email: userData.email,
            userType: userData.userType,
            specialization: userData.specialization,
        };
        try {
            setIsProcessing(true);
            const response = await updateUser(updatedUserData, userId);
            console.log("The response from the update: ", response);
            setSuccessMessage(response.message);
            setShowSuccessAlert(true);
        } catch (error) {
            setErrorMessage(error.message);
            setShowErrorAlert(true);
        } finally {
            setIsProcessing(false);
        }
    };

    const handleCancelEdit = () => {
        navigate(`/user-dashboard/${userId}/my-dashboard`);
    };

    return (
        <Container md={6} className="d-flex justify-content-center mt-5">
            <Col md={6}>
                <Form onSubmit={(e) => handleUserUpdate(e)}>
                    <Card className="shadow mb-5">
                        <Card.Header className="text-center mb-2">
                            Update User Information
                        </Card.Header>
                        <Card.Body>
                            <fieldset className="field-set">
                                <legend>Full Name</legend>
                                <Form.Group
                                    as={Col}
                                    controlId="nameFields"
                                    className="mb-2 d-flex"
                                >
                                    <Form.Control
                                        type="text"
                                        name="firstName"
                                        value={userData.firstName}
                                        onChange={handleUserInputChange}
                                        style={{ marginRight: "10px" }}
                                    />
                                    <Form.Control
                                        type="text"
                                        name="lastName"
                                        value={userData.lastName}
                                        onChange={handleUserInputChange}
                                    />
                                </Form.Group>
                            </fieldset>

                            <Form.Group
                                as={Col}
                                controlId="gender"
                                className="mb-2"
                            >
                                <Form.Label className="legend">
                                    Gender
                                </Form.Label>
                                <Form.Control
                                    as="select"
                                    name="gender"
                                    value={userData.gender}
                                    onChange={handleUserInputChange}
                                >
                                    <option value="">Select Gender</option>
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Others">Others</option>
                                </Form.Control>
                            </Form.Group>

                            <Form.Group
                                as={Col}
                                controlId="gender"
                                className="mb-2"
                            >
                                <Form.Label className="legend">
                                    User Type
                                </Form.Label>
                                <Form.Control
                                    type="text"
                                    name="userType"
                                    value={userData.userType}
                                    onChange={handleUserInputChange}
                                    disabled
                                />
                            </Form.Group>

                            <fieldset className="field-set mb-2 mt-2">
                                <legend>Contact Information</legend>
                                <Form.Group
                                    as={Col}
                                    controlId="emailPhoneFields"
                                    className="mb-2 d-flex"
                                >
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        value={userData.email}
                                        onChange={handleUserInputChange}
                                        style={{ marginRight: "10px" }}
                                        disabled
                                    />
                                    <Form.Control
                                        type="text"
                                        name="phoneNumber"
                                        placeholder="Mobile Contact"
                                        value={userData.phoneNumber}
                                        onChange={handleUserInputChange}
                                    />
                                </Form.Group>
                            </fieldset>

                            {userData.userType === "VET" && (
                                <Form.Group
                                    controlId="specialization"
                                    className="mb-4"
                                >
                                    <Form.Label className="legend">
                                        Specialization
                                    </Form.Label>
                                    <VetSpecializationSelector
                                        handleAddSpecialization={
                                            handleUserInputChange
                                        }
                                        userData={userData}
                                        setUserData={setUserData}
                                    />
                                </Form.Group>
                            )}
                            {showErrorAlert && (
                                <AlertMessage
                                    type="danger"
                                    message={errorMessage}
                                />
                            )}
                            {showSuccessAlert && (
                                <AlertMessage
                                    type="success"
                                    message={successMessage}
                                />
                            )}

                            <div className="d-flex justify-content-center">
                                <div className="mx-2">
                                    <Button
                                        type="submit"
                                        variant="outline-warning"
                                        size="sm"
                                        disabled={isProcessing}
                                    >
                                        {isProcessing ? (
                                            <ProcessSpinner message="Processing update..." />
                                        ) : (
                                            "Update"
                                        )}
                                    </Button>
                                </div>
                                <div className="mx-2">
                                    <Button
                                        variant="outline-info"
                                        size="sm"
                                        onClick={handleCancelEdit}
                                    >
                                        Back to profile
                                    </Button>
                                </div>
                            </div>
                        </Card.Body>
                    </Card>
                </Form>
            </Col>
        </Container>
    );
};
export default UserUpdate;
