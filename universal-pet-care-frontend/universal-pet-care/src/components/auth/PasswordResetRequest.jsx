/* eslint-disable react/no-unescaped-entities */
/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { Container, Form, Button, Card } from "react-bootstrap";
import AlertMessage from "../common/AlertMessage";
import ProcessSpinner from "../common/ProcessSpinner";
import { requestPasswordReset } from "./AuthService";

const PasswordResetRequest = () => {
    const [email, setEmail] = useState("");
    const [isProcessing, setIsProcessing] = useState(false);

    const {
        errorMessage,
        successMessage,
        setShowSuccessAlert,
        setSuccessMessage,
        setErrorMessage,
        showErrorAlert,
        setShowErrorAlert,
    } = UseMessageAlerts();

    const handleSubmit = async (event) => {
        event.preventDefault();
        setIsProcessing(true);
        try {
            const data = await requestPasswordReset(email);
            setSuccessMessage(data.message);
            setShowSuccessAlert(true);
            setEmail("");
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
                {setShowSuccessAlert && (
                    <AlertMessage type={"success"} message={successMessage} />
                )}

                <Card.Body>
                    <Card.Title>Password Reset Request</Card.Title>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="email">
                            <Form.Label>Enter your email address</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Enter email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />

                            <Form.Text className="text-muted">
                                <p>
                                    We'll send a password reset link to your
                                    email.
                                </p>
                            </Form.Text>
                        </Form.Group>

                        <Button
                            variant="outline-info"
                            type="submit"
                            className="mt-2"
                        >
                            {isProcessing ? (
                                <ProcessSpinner message="Sending Verification link, please, wait..." />
                            ) : (
                                "Send Link"
                            )}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default PasswordResetRequest;
