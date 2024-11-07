/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Container, Form, Card, Button } from "react-bootstrap";
import AlertMessage from "../common/AlertMessage";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { validateToken, resetPassword } from "./AuthService";
import ProcessSpinner from "../common/ProcessSpinner";

const ResetPassword = () => {
    const [newPassword, setNewPassword] = useState("");
    const [isProcessing, setIsProcessing] = useState(false);
    const [tokenStatus, setTokenStatus] = useState("PENDING");

    const queryParams = new URLSearchParams(window.location.search);
    const token = queryParams.get("token");

    const {
        errorMessage,
        setErrorMessage,
        successMessage,
        setSuccessMessage,
        setShowSuccessAlert,
        showSuccessAlert,
        showErrorAlert,
        setShowErrorAlert,
    } = UseMessageAlerts();

    useEffect(() => {
        if (token) {
            validateToken(token)
                .then((response) => {
                    setTokenStatus(response.message);
                })
                .catch((error) => {
                    setErrorMessage(error.response.data.message);
                    setShowErrorAlert(true);
                });
        }
    }, [token]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setIsProcessing(true);
        try {
            const data = await resetPassword(token, newPassword);
            setSuccessMessage(data.message);
            setShowSuccessAlert(true);
        } catch (error) {
            setErrorMessage(error.response.data.message);
            setShowErrorAlert(true);
        }
        setIsProcessing(false);
    };

    return (
        <Container
            className="d-flex align-items-center justify-content-center"
            style={{ marginTop: "100px" }}
        >
            <Card style={{ maxWidth: "600px" }} className="w-100">
                {showErrorAlert && (
                    <AlertMessage type={"danger"} message={errorMessage} />
                )}
                {showSuccessAlert && (
                    <AlertMessage type={"success"} message={successMessage} />
                )}

                {tokenStatus === "VALID" ? (
                    <Card.Body>
                        <Card.Title>Reset Your Password</Card.Title>

                        <Form onSubmit={handleSubmit}>
                            <Form.Group className="mb-3" controlId="emailInput">
                                <Form.Label>Set a new password</Form.Label>
                                <Form.Control
                                    type="password"
                                    value={newPassword}
                                    onChange={(e) =>
                                        setNewPassword(e.target.value)
                                    }
                                    placeholder="choose a new password"
                                />
                            </Form.Group>

                            <Button variant="outline-info" type="submit">
                                {isProcessing ? (
                                    <ProcessSpinner message="Resetting your password, please wait..." />
                                ) : (
                                    "Reset Password"
                                )}
                            </Button>
                        </Form>
                    </Card.Body>
                ) : tokenStatus === "PENDING" ? (
                    <Card.Body>
                        <ProcessSpinner message="Validating token, please wait..." />
                    </Card.Body>
                ) : (
                    <Card.Body>
                        <AlertMessage
                            type={"danger"}
                            message={
                                "Invalid or expired link, process aborted. please try again!"
                            }
                        />
                    </Card.Body>
                )}
            </Card>
        </Container>
    );
};

export default ResetPassword;
