import React from "react";
import PropTypes from "prop-types";

const UserInformation = ({ userType, appointment }) => {
    return (
        <div className="mt-2 mb-2" style={{ backgroundColor: "whiteSmoke" }}>
            <h5>
                {userType === "VET" ? "Patient " : "Veterinarian "} Information
            </h5>
            {userType === "VET" ? (
                <React.Fragment>
                    <p className="text-info">
                        Appointment No: {appointment.appointmentNo}
                    </p>
                    <p>
                        Name: {appointment.patient.firstName}{" "}
                        {appointment.patient.lastName}
                    </p>
                    <p>Email: {appointment.patient.email}</p>
                    <p className="text-info">
                        Phone Number: {appointment.patient.phoneNumber}
                    </p>
                </React.Fragment>
            ) : (
                <React.Fragment>
                    <p className="text-info">
                        Appointment No: {appointment.appointmentNo}
                    </p>
                    <p>
                        Name: Dr. {appointment.veterinarian.firstName}{" "}
                        {appointment.veterinarian.lastName}
                    </p>
                    <p className="text-info">
                        Specialization:{" "}
                        {appointment.veterinarian.specialization}
                    </p>
                    <p>Email: {appointment.veterinarian.email}</p>
                    <p className="text-info">
                        Phone Number: {appointment.veterinarian.phoneNumber}
                    </p>
                </React.Fragment>
            )}
        </div>
    );
};

UserInformation.propTypes = {
    userType: PropTypes.string.isRequired, // Kiểu string và bắt buộc
    appointment: PropTypes.object.isRequired, // Kiểu string và bắt buộc
};

export default UserInformation;
