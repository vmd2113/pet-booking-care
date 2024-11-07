/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { Form, Row, Col, Button } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format } from "date-fns";
import AlertMessage from "../common/AlertMessage";
import {
    findAvailableVeterinarians,
    getAllSpecializations,
} from "./VeterinarianService";
import { dateTimeFormatter } from "../utils/utilities";
import PropTypes from "prop-types";

const VeterinarianSearch = ({ onSearchResult }) => {
    const [specializations, setSpecializations] = useState([]);
    const [searchQuery, setSearchQuery] = useState({
        date: null,
        time: null,
        specialization: "",
    });
    const [showDateTime, setShowDateTime] = useState(false);
    const { errorMessage, setErrorMessage, showErrorAlert, setShowErrorAlert } =
        UseMessageAlerts();

    const handleInputchange = (e) => {
        setSearchQuery({ ...searchQuery, [e.target.name]: e.target.value });
    };

    const handleDateChange = (date) => {
        setSearchQuery((prevState) => ({
            ...prevState,
            date: date,
        }));
    };

    const handleTimeChange = (time) => {
        setSearchQuery((prevState) => ({
            ...prevState,
            time: time,
        }));
    };

    const handleDateTimeToggle = (e) => {
        const isChecked = e.target.checked;
        setShowDateTime(isChecked);
        if (isChecked) {
            setSearchQuery({ ...searchQuery, date: null, time: null });
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();

        const { date, time, specialization } = searchQuery;

        let searchParams = { specialization };

        if (date && time) {
            const { formattedDate, formattedTime } = dateTimeFormatter(
                date,
                time
            );
            searchParams.date = formattedDate;
            searchParams.time = formattedTime;
        }

        try {
            const response = await findAvailableVeterinarians(searchParams);
            onSearchResult(response.data);
            setShowErrorAlert(false);
        } catch (error) {
            console.log("The search query : " + error);
            setErrorMessage(error.response.data.message);
            setShowErrorAlert(true);
        }
    };

    useEffect(() => {
        getAllSpecializations()
            .then((data) => {
                setSpecializations(data.data || data);
            })
            .catch((error) => {
                setErrorMessage(error.message);
            });
    }, []);

    const handleClearSearch = () => {
        setSearchQuery({
            date: null,
            time: null,
            specialization: "",
        });
        setShowDateTime(false);
        onSearchResult(null);
    };

    return (
        <section className="stickyFormContainer">
            <h3>Find a Veterinarian</h3>
            <Form onSubmit={handleSearch}>
                <Form.Group>
                    <Form.Label>Specialization</Form.Label>
                    <Form.Control
                        as="select"
                        name="specialization"
                        value={searchQuery.specialization}
                        onChange={handleInputchange}
                    >
                        <option value="">Select Specialization</option>
                        {specializations.map((specialization) => (
                            <option key={specialization} value={specialization}>
                                {specialization}
                            </option>
                        ))}
                    </Form.Control>
                </Form.Group>

                <fieldset>
                    <Row className="mb-3">
                        <Col>
                            <Form.Group className="mb-3 mt-3">
                                <Form.Check
                                    type="checkbox"
                                    label="Include Date and Time Availabilty"
                                    checked={showDateTime}
                                    onChange={handleDateTimeToggle}
                                />
                            </Form.Group>
                            {showDateTime && (
                                <React.Fragment>
                                    <legend>Include Date and Time</legend>
                                    <Form.Group className="mb-3">
                                        <Form.Label className="searchText">
                                            Date
                                        </Form.Label>
                                        <DatePicker
                                            selected={searchQuery.date}
                                            onChange={handleDateChange}
                                            dateFormat="yyyy-MM-dd"
                                            minDate={new Date()}
                                            className="form-control"
                                            placeholderText="Select date"
                                        />
                                    </Form.Group>
                                    <Form.Group className="mb-3">
                                        <Form.Label className="searchText">
                                            Time
                                        </Form.Label>
                                        <DatePicker
                                            selected={searchQuery.time}
                                            onChange={handleTimeChange}
                                            showTimeSelect
                                            showTimeSelectOnly
                                            timeIntervals={30}
                                            dateFormat="HH:mm"
                                            className="form-control"
                                            placeholderText="Select time"
                                            required
                                        />
                                    </Form.Group>
                                </React.Fragment>
                            )}
                        </Col>
                    </Row>
                </fieldset>

                <div className="d-flex justify-content-center mb-4">
                    <Button type="submit" variant="outline-primary">
                        Search
                    </Button>
                    <div className="mx-2">
                        <Button
                            type="button"
                            variant="outline-info"
                            onClick={handleClearSearch}
                        >
                            Clear Search
                        </Button>
                    </div>
                </div>
            </Form>
            <div>
                {showErrorAlert && (
                    <AlertMessage type={"danger"} message={errorMessage} />
                )}
            </div>
        </section>
    );
};

VeterinarianSearch.propTypes = {
    onSearchResult: PropTypes.func.isRequired,
};

export default VeterinarianSearch;
