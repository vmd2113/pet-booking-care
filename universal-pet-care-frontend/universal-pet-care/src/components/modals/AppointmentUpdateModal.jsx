/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import DatePicker from "react-datepicker";
import PropTypes from "prop-types";
const AppointmentUpdateModal = ({
    show,
    handleClose,
    appointment,
    handleUpdate,
}) => {
    const [appointmentDate, setAppointmentDate] = useState(
        new Date(appointment.appointmentDate)
    );
    const [appointmentTime, setAppointmentTime] = useState(
        new Date(
            `${appointment.appointmentDate}T${appointment.appointmentTime}`
        )
    );
    const [reason, setReason] = useState(appointment.reason);

    const handleSubmit = () => {
        const updatedAppointment = {
            ...appointment,
            appointmentDate: appointmentDate.toISOString().split("T")[0],
            appointmentTime: appointmentTime
                .toTimeString()
                .split(" ")[0]
                .substring(0, 5),
            reason,
        };
        handleUpdate(updatedAppointment);
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Update Appointment</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Form>
                    <Form.Group controlId="appointmentDate">
                        <Form.Label className=" me-2">Date</Form.Label>
                        <DatePicker
                            selected={appointmentDate}
                            onChange={(date) => setAppointmentDate(date)}
                            dateFormat="yyyy-MM-dd"
                            className="form-control"
                        />
                    </Form.Group>

                    <Form.Group controlId="appointmentTime" className="mt-4">
                        <Form.Label className=" me-2">Time</Form.Label>
                        <DatePicker
                            selected={appointmentTime}
                            onChange={(time) => setAppointmentTime(time)}
                            showTimeSelect
                            showTimeSelectOnly
                            timeIntervals={30}
                            timeCaption="Time"
                            dateFormat="HH:mm"
                            className="form-control"
                            placeholderText="Select time"
                            required
                        />
                    </Form.Group>

                    <Form.Group controlId="reason" className="mt-2">
                        <Form.Label>Reason</Form.Label>
                        <Form.Control
                            as={"textarea"}
                            rows={3}
                            placeholder="Enter reason"
                            value={reason}
                            onChange={(e) => setReason(e.target.value)}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="info" onClick={handleSubmit}>
                    Save Update
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

AppointmentUpdateModal.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    appointment: PropTypes.object.isRequired,
    handleUpdate: PropTypes.func.isRequired,
};

export default AppointmentUpdateModal;
