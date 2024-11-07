/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { dateTimeFormatter } from "../utils/utilities";
import { useParams } from "react-router-dom";
import {
    Container,
    Row,
    Col,
    Form,
    Card,
    OverlayTrigger,
    Tooltip,
    Button,
} from "react-bootstrap";
import { FaPlus } from "react-icons/fa";
import DatePicker from "react-datepicker";
import PetEntry from "../pet/PetEntry";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { bookAppointment } from "./AppointmentService";
import AlertMessage from "../common/AlertMessage";
import ProcessSpinner from "../common/ProcessSpinner";
import { useNavigate } from "react-router-dom";

const BookAppointment = () => {
    const [isProcessing, setIsProcessing] = useState(false);
    const [formData, setFormData] = useState({
        appointmentDate: "",
        appointmentTime: "",
        reason: "",
        pets: [
            {
                petName: "",
                petType: "",
                petColor: "",
                petBreed: "",
                petAge: "",
            },
        ],
    });

    const {
        successMessage,
        setSuccessMessage,
        showSuccessAlert,
        setShowSuccessAlert,
        errorMessage,
        setErrorMessage,
        showErrorAlert,
        setShowErrorAlert,
    } = UseMessageAlerts();

    const { recipientId } = useParams();
    const senderId = localStorage.getItem("userId");

    const handleDateChange = (date) => {
        setFormData((prevState) => ({
            ...prevState,
            appointmentDate: date,
        }));
    };

    const handleTimeChange = (time) => {
        setFormData((prevState) => ({
            ...prevState,
            appointmentTime: time,
        }));
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handlePetChange = (index, e) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            pets: prevState.pets.map((pet, idx) =>
                idx === index ? { ...pet, [name]: value } : pet
            ),
        }));
    };

    const addPet = () => {
        const newPet = {
            petName: "",
            petType: "",
            petColor: "",
            petBreed: "",
            petAge: "",
        };
        setFormData((prevState) => ({
            ...prevState,
            pets: [...prevState.pets, newPet],
        }));
    };

    const removePet = (index, e) => {
        const filteredPets = formData.pets.filter((_, idx) => idx !== index);
        setFormData((prevState) => ({
            ...prevState,
            pets: filteredPets,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Extract appointmentDate and appointmentTime from formData
        const { appointmentDate, appointmentTime } = formData;
        // Use dateTimeFormatter to format the date and time
        const { formattedDate, formattedTime } = dateTimeFormatter(
            appointmentDate,
            appointmentTime
        );
        // Constructing an array of pet objects from formData.pets
        const pets = formData.pets.map((pet) => ({
            name: pet.petName,
            type: pet.petType,
            breed: pet.petBreed,
            color: pet.petColor,
            age: pet.petAge,
        }));

        const request = {
            appointment: {
                appointmentDate: formattedDate,
                appointmentTime: formattedTime,
                reason: formData.reason,
            },
            pets: pets,
        };

        setIsProcessing(true);
        try {
            const response = await bookAppointment(
                senderId,
                recipientId,
                request
            );
            setSuccessMessage(response.message);
            handleReset();
            setShowSuccessAlert(true);
        } catch (error) {
            if (error.response.data.status === 401) {
                setErrorMessage("Please, login to book appointment");
                setShowErrorAlert(true);
            } else {
                setErrorMessage(error.response.data.message);
                setShowErrorAlert(true);
            }
        } finally {
            setIsProcessing(false);
        }
    };

    const handleReset = () => {
        setFormData({
            appointmentDate: "",
            appointmentTime: "",
            reason: "",
            pets: [
                {
                    petName: "",
                    petType: "",
                    petColor: "",
                    petBreed: "",
                    petAge: "",
                },
            ],
        });
        setShowSuccessAlert(false);
        setShowErrorAlert(false);
    };

    return (
        <Container className="mt-5">
            <Row className="justify-content-center">
                <Col lg={6} md={10} sm={12}>
                    <Form onSubmit={handleSubmit}>
                        <Card className="shadow mb-5">
                            <Card.Header as="h5" className="text-center">
                                {" "}
                                Appointment Booking Form
                            </Card.Header>
                            <Card.Body>
                                <fieldset className="field-set mb-4">
                                    <legend className="text-center">
                                        Appointment Date and Time
                                    </legend>
                                    <Form.Group as={Row} className="mb-4">
                                        <Col>
                                            <DatePicker
                                                selected={
                                                    formData.appointmentDate
                                                }
                                                onChange={handleDateChange}
                                                dateFormat="yyyy-MM-dd"
                                                className="form-control"
                                                minDate={new Date()}
                                                placeholderText="Choose a date"
                                                required
                                            />
                                        </Col>

                                        <Col>
                                            <DatePicker
                                                selected={
                                                    formData.appointmentTime
                                                }
                                                onChange={handleTimeChange}
                                                showTimeSelect
                                                showTimeSelectOnly
                                                timeIntervals={30}
                                                dateFormat="HH:mm"
                                                className="form-control"
                                                placeholderText="Select time"
                                                required
                                            />
                                        </Col>
                                    </Form.Group>
                                </fieldset>

                                <Form.Group className="mb-4">
                                    <Form.Label>
                                        Reason for appointment
                                    </Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        name="reason"
                                        onChange={handleInputChange}
                                        value={formData.reason}
                                        required
                                    />
                                </Form.Group>
                                <h5 className="text-center">
                                    Appointment Pet Information
                                </h5>
                                {formData.pets.map((pet, index) => (
                                    <PetEntry
                                        key={index}
                                        pet={pet}
                                        index={index}
                                        handleInputChange={(e) =>
                                            handlePetChange(index, e)
                                        }
                                        removePet={removePet}
                                        canRemove={formData.pets.length > 1}
                                    />
                                ))}

                                {showErrorAlert && (
                                    <AlertMessage
                                        type={"danger"}
                                        message={errorMessage}
                                    />
                                )}

                                {showSuccessAlert && (
                                    <AlertMessage
                                        type={"success"}
                                        message={successMessage}
                                    />
                                )}

                                <div className="d-flex justify-content-center mb-3">
                                    <OverlayTrigger
                                        overlay={<Tooltip>Add pets</Tooltip>}
                                    >
                                        <Button
                                            size="sm"
                                            onClick={addPet}
                                            className="me-2"
                                        >
                                            <FaPlus />
                                        </Button>
                                    </OverlayTrigger>

                                    <Button
                                        type="submit"
                                        variant="outline-primary"
                                        size="sm"
                                        className="me-2"
                                        disabled={isProcessing}
                                    >
                                        {isProcessing ? (
                                            <ProcessSpinner message="Booking appointment, please wait..." />
                                        ) : (
                                            "  Book Appointment"
                                        )}
                                    </Button>

                                    <Button
                                        variant="outline-info"
                                        size="sm"
                                        onClick={handleReset}
                                    >
                                        Reset
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
};

export default BookAppointment;
