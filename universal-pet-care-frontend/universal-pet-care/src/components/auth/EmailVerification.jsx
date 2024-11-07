/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { verifyEmail, resendVerificationToken } from "./AuthService";
import ProcessSpinner from "../common/ProcessSpinner";

const EmailVerification = () => {
    const [verificationMessage, setVerificationMessage] = useState(
        "Verifying your email, please wait...."
    );
    const [alertType, setAlertType] = useState("alert-info");
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        const queryParams = new URLSearchParams(window.location.search);
        const token = queryParams.get("token");
        if (token) {
            verifyEmailToken(token);
        } else if (!token) {
            setVerificationMessage("No token provided.");
            setAlertType("alert-danger");
        }
    }, []);

    const verifyEmailToken = async (token) => {
        setIsProcessing(true);
        try {
            const response = await verifyEmail(token);
            switch (response.message) {
                case "VALID":
                    setVerificationMessage(
                        "Your email has been successfully verified, you can proceed to login."
                    );
                    setAlertType("alert-success");
                    break;
                case "VERIFIED":
                    setVerificationMessage(
                        "This email has already been verified, please proceed to login."
                    );
                    setAlertType("alert-info");
                    break;
                default:
                    setVerificationMessage("An unexpected error occurred.");
                    setAlertType("alert-danger");
            }
        } catch (error) {
            if (error.response) {
                const { message } = error.response.data;

                if (message && message === "INVALID") {
                    setVerificationMessage(
                        "This verification link is invalid."
                    );
                    setAlertType("alert-danger");
                } else {
                    setVerificationMessage(
                        "This verification link has expired, please try again."
                    );
                    setAlertType("alert-warning");
                }
            } else {
                setVerificationMessage("Failed to connect to the server.");
                setAlertType("alert-danger");
            }
        } finally {
            setIsProcessing(false); // Stop loading regardless of the outcome
        }
    };

    //Resend verification to user if the initial one has expired.
    const handleResendToken = async () => {
        setIsProcessing(true);
        const queryParams = new URLSearchParams(location.search);
        const oldToken = queryParams.get("token");
        try {
            if (!oldToken) {
                return;
            }

            const response = await resendVerificationToken(oldToken);
            setVerificationMessage(response.message);
            setAlertType("alert-success");
        } catch (error) {
            console.log("The error : " + error);
            let message = "Failed to resend verification token.";
            if (
                error.response &&
                error.response.data &&
                error.response.data.message
            ) {
                message = error.response.data.message;
            } else if (error.message) {
                message = error.message;
            }
            setVerificationMessage(message);
            setAlertType("alert-danger");
        } finally {
            setIsProcessing(false); // Stop loading regardless of the outcome
        }
    };

    return (
        <div className="d-flex justify-content-center  mt-lg-5">
            {isProcessing ? (
                <ProcessSpinner message="Processing your request, please wait..." />
            ) : (
                <div className="col-12 col-md-6">
                    <div className={`alert ${alertType}`} role="alert">
                        {verificationMessage}

                        {alertType === "alert-warning" && (
                            <button
                                onClick={handleResendToken}
                                className="btn btn-link"
                            >
                                Resend Verification Link
                            </button>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default EmailVerification;
