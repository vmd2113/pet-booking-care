/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import ActionButtons from "./ActionButtons";
import AppointmentUpdateModal from "../modals/AppointmentUpdateModal";
import PropTypes from "prop-types";

const PatientActions = ({ onCancel, onUpdate, isDisabled, appointment }) => {
    const [isProcessing, setIsProcessing] = useState(false);
    const [showUpdateModal, setShowUpdateModal] = useState(false);

    const handleActionClick = (actionType) => {
        setIsProcessing(true);
        try {
            if (actionType === "Update") {
                setShowUpdateModal(true);
            } else {
                onCancel(appointment.id);
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleUpdateAppointment = async (updatedAppointment) => {
        setIsProcessing(false);
        try {
            await onUpdate(updatedAppointment);
            handleCloseModal();
        } catch (error) {
            console.error(error);
        }
    };

    const handleCloseModal = () => {
        setShowUpdateModal(false);
    };

    return (
        <React.Fragment>
            <section className="d-flex justify-content-end gap-2 mt-2 mb-2">
                <ActionButtons
                    title={"Update Appointment"}
                    variant={"warning"}
                    onClick={() => handleActionClick("Update")}
                    disabled={isDisabled}
                />
                <ActionButtons
                    title={"Cancel Appointment"}
                    variant={"danger"}
                    onClick={() => onCancel(appointment.id)}
                    disabled={isDisabled}
                />
            </section>

            {showUpdateModal && (
                <AppointmentUpdateModal
                    show={showUpdateModal}
                    appointment={appointment}
                    handleClose={handleCloseModal}
                    handleUpdate={handleUpdateAppointment}
                />
            )}
        </React.Fragment>
    );
};

PatientActions.propTypes = {
    onCancel: PropTypes.func.isRequired,
    onUpdate: PropTypes.func.isRequired,
    isDisabled: PropTypes.bool.isRequired,
    appointment: PropTypes.object.isRequired,
};

export default PatientActions;
