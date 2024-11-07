/* eslint-disable react/jsx-key */
/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from "react";
import { Table, OverlayTrigger, Tooltip, Row, Col } from "react-bootstrap";
import { Link } from "react-router-dom";
import AlertMessage from "../common/AlertMessage";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { getPatients } from "../patient/PatientService";
import Paginator from "../common/Paginator";
import NoDataAvailable from "../common/NoDataAvailable";
import UserFilter from "../user/UserFilter";
import { BsEyeFill } from "react-icons/bs";

const PatientComponent = () => {
    const [patients, setPatients] = useState([]);
    const [filteredPatients, setFilteredPatients] = useState([]);
    const [selectedEmail, setSelectedEmail] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [patientsPerPage] = useState(10);

    // start pagination
    const indexOfLastPatient = currentPage * patientsPerPage;
    const indexOfFirstPatient = indexOfLastPatient - patientsPerPage;
    const currentPatients = filteredPatients.slice(
        indexOfFirstPatient,
        indexOfLastPatient
    );
    // end pagination
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

    const fetchPatients = async () => {
        try {
            const response = await getPatients();
            setPatients(response.data);
        } catch (error) {
            setErrorMessage(error.message);
            setShowErrorAlert(true);
        }
    };

    useEffect(() => {
        fetchPatients();
    }, []);

    useEffect(() => {
        let filtered = patients;
        if (selectedEmail) {
            filtered = filtered.filter(
                (patient) => patient.email === selectedEmail
            );
        }
        setFilteredPatients(filtered);
    }, [selectedEmail, patients]);

    // Here we are extracting all patients email from the current patient.
    const emails = Array.from(new Set(patients.map((p) => p.email)));

    const handleClearFilters = () => {
        setSelectedEmail("");
    };

    return (
        <main>
            {currentPatients && currentPatients.length > 0 ? (
                <React.Fragment>
                    <Row>
                        <h5>List of Patients</h5>
                        <Col>
                            {" "}
                            <UserFilter
                                values={emails}
                                selectedValue={selectedEmail}
                                onSelectedValue={setSelectedEmail}
                                onClearFilters={handleClearFilters}
                                label={"email"}
                            />
                        </Col>
                        <Col>
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
                        </Col>
                    </Row>

                    <Table bordered hover striped>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>Mobile</th>
                                <th>Gender</th>
                                <th>Registered on</th>
                                <th colSpan={2}>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            {currentPatients.map((patient, index) => (
                                <tr key={index}>
                                    <td>{patient.id}</td>
                                    <td>{patient.firstName}</td>
                                    <td>{patient.lastName}</td>
                                    <td>{patient.email}</td>
                                    <td>{patient.phoneNumber}</td>
                                    <td>{patient.gender}</td>
                                    <td>{patient.createdAt}</td>
                                    <td>
                                        <OverlayTrigger
                                            overlay={
                                                <Tooltip
                                                    id={`tooltip-view-${index}`}
                                                >
                                                    View patient details
                                                </Tooltip>
                                            }
                                        >
                                            <Link
                                                to={`/user-dashboard/${patient.id}/my-dashboard`}
                                                className="text-info"
                                            >
                                                <BsEyeFill />
                                            </Link>
                                        </OverlayTrigger>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    <Paginator
                        currentPage={currentPage}
                        totalItems={filteredPatients.length}
                        paginate={setCurrentPage}
                        itemsPerPage={patientsPerPage}
                    />
                </React.Fragment>
            ) : (
                <NoDataAvailable
                    dataType={" patients data "}
                    message={errorMessage}
                />
            )}
        </main>
    );
};

export default PatientComponent;
