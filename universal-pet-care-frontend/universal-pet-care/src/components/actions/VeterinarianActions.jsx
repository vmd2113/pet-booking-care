/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import ActionButtons from "./ActionButtons";
import PropTypes from "prop-types";
import ProcessSpinner from "../common/ProcessSpinner";

const VeterinarianActions = ({
    onApprove,
    onDecline,
    isDisabled,
    appointment,
}) => {
    const [isProcessing, setIsProcessing] = useState(false);
    const [processingAction, setProcessingAction] = useState(null);

    const handleActionClick = (actionType) => {
        setIsProcessing(true);
        setProcessingAction(actionType);
        if (actionType === "Approve") {
            onApprove(appointment.id)
                .then(() => {
                    setIsProcessing(false);
                    setProcessingAction(null);
                })
                .catch(() => {
                    setIsProcessing(false);
                    setProcessingAction(null);
                });
        } else {
            onDecline(appointment.id)
                .then(() => {
                    setIsProcessing(false);
                    setProcessingAction(null);
                })
                .catch(() => {
                    setIsProcessing(false);
                    setProcessingAction(null);
                });
        }
    };
    return (
        <section className="d-flex justify-content-end gap-2 mt-2 mb-2">
            <ActionButtons
                title={
                    isProcessing && processingAction === "Approve" ? (
                        <ProcessSpinner message="Approving appointment..." />
                    ) : (
                        "Approve Appointment"
                    )
                }
                variant={"success"}
                onClick={() => handleActionClick("Approve")}
                disabled={isDisabled}
                isProcessing={isProcessing}
            />
            <ActionButtons
                title={
                    isProcessing && processingAction === "Decline" ? (
                        <ProcessSpinner message="Declining appointment..." />
                    ) : (
                        "Decline Appointment"
                    )
                }
                variant={"secondary"}
                onClick={() => handleActionClick("Decline")}
                disabled={isDisabled}
                isProcessing={isProcessing}
            />
        </section>
    );
};

VeterinarianActions.propTypes = {
    onApprove: PropTypes.func,
    onDecline: PropTypes.func,
    isDisabled: PropTypes.bool,
    appointment: PropTypes.object,
};

export default VeterinarianActions;
