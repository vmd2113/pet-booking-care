/* eslint-disable no-unused-vars */
import React, { Fragment, useEffect, useState } from "react";
import { Container, Tabs, Tab, Col, Row, Card } from "react-bootstrap";
import { useParams } from "react-router-dom";
import UserProfile from "./UserProfile";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { getUserById, deleteUser } from "./UserService";
import { deleteUserPhoto } from "../modals/ImageUploaderService";
import AlertMessage from "../common/AlertMessage";
import Review from "../review/Review";
import UserAppointments from "../appointment/UserAppointments";
import CustomPieChart from "../charts/CustomPieChart";
import { formatAppointmentStatus } from "../utils/utilities";
import NoDataAvailable from "../common/NoDataAvailable";
import { logout } from "../auth/AuthService";

const UserDashboard = () => {
    const [user, setUser] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [appointmentData, setAppointmentData] = useState([]);
    const [activeKey, setActiveKey] = useState(() => {
        const storedActiveKey = localStorage.getItem("activeKey");
        return storedActiveKey ? storedActiveKey : "profile";
    });

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

    const { userId } = useParams();

    const currentUserId = localStorage.getItem("userId");

    const isCurrentUser = userId === currentUserId;

    useEffect(() => {
        const getUser = async () => {
            try {
                const data = await getUserById(userId);
                setUser(data.data);
                console.log("The user data:", data.data);
                setAppointments(data.data.appointments);
            } catch (error) {
                setErrorMessage(error.response.data.message);
                setShowErrorAlert(true);
                console.error(error.message);
            }
        };
        getUser();
    }, [userId]);

    useEffect(() => {
        if (user && user.appointments) {
            const statusCounts = user.appointments.reduce(
                (acc, appointment) => {
                    const formattedStatus = formatAppointmentStatus(
                        appointment.status
                    );
                    if (!acc[formattedStatus]) {
                        acc[formattedStatus] = {
                            name: formattedStatus,
                            value: 1,
                        };
                    } else {
                        acc[formattedStatus].value += 1;
                    }
                    return acc;
                },
                {}
            );

            const transformedData = Object.values(statusCounts);
            setAppointmentData(transformedData);
            setAppointments(user.appointments);
        }
    }, [user]);

    const handleRemovePhoto = async () => {
        try {
            const result = await deleteUserPhoto(user.photoId, userId);
            setSuccessMessage(result.message);
            window.location.reload();
            setShowSuccessAlert(true);
        } catch (error) {
            setErrorMessage(error.message);
            setShowErrorAlert(true);
            console.error(error.message);
        }
    };

    const handleDeleteAccount = async () => {
        try {
            const response = await deleteUser(userId);
            setSuccessMessage(response.message);
            setShowSuccessAlert(true);
            setTimeout(() => {
                logout();
            }, 10000);
        } catch (error) {
            setErrorMessage(error.message);
            setShowErrorAlert(true);
        }
    };

    const handleTabSelect = (key) => {
        setActiveKey(key);
        localStorage.setItem("activeKey", key);
    };
    return (
        <Container className="mt-2 user-dashboard">
            <Tabs
                className="mb-2"
                justify
                activeKey={activeKey}
                onSelect={handleTabSelect}
            >
                {isCurrentUser && (
                    <Tab eventKey="profile" title={<h3>Profile</h3>}>
                        <Col>
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

                            {user && (
                                <UserProfile
                                    user={user}
                                    handleRemovePhoto={handleRemovePhoto}
                                    handleDeleteAccount={handleDeleteAccount}
                                />
                            )}
                        </Col>
                    </Tab>
                )}

                {isCurrentUser && (
                    <Tab
                        eventKey="status"
                        title={<h3>Appointments Overview</h3>}
                    >
                        <Row>
                            <h4 className="text-center mt-4">
                                Appointment Overview
                            </h4>
                            <Col>
                                {appointmentData &&
                                appointmentData.length > 0 ? (
                                    <CustomPieChart data={appointmentData} />
                                ) : (
                                    <NoDataAvailable
                                        dataType={"appointment data"}
                                    />
                                )}
                            </Col>
                        </Row>
                    </Tab>
                )}

                {isCurrentUser && (
                    <Tab
                        eventKey="appointments"
                        title={<h3>Appointment Details</h3>}
                    >
                        {" "}
                        <Row>
                            <Col>
                                {user && (
                                    <React.Fragment>
                                        {appointments &&
                                        appointments.length > 0 ? (
                                            <UserAppointments
                                                user={user}
                                                appointments={appointments}
                                            />
                                        ) : (
                                            <NoDataAvailable
                                                dataType={"appointment data"}
                                            />
                                        )}
                                    </React.Fragment>
                                )}
                            </Col>
                        </Row>
                    </Tab>
                )}

                <Tab eventKey="reviews" title={<h3>Reviws</h3>}>
                    <Container className="d-flex justify-content-center align-items-center">
                        <Card className="mt-5 mb-4 review-card">
                            <h4 className="text-center mb-2">Your Reviews</h4>
                            <hr />
                            <Row>
                                <Col>
                                    {user &&
                                    user.reviews &&
                                    user.reviews.length > 0 ? (
                                        user.reviews.map((review, index) => (
                                            <Review
                                                key={index}
                                                review={review}
                                                userType={user.userType}
                                            />
                                        ))
                                    ) : (
                                        <NoDataAvailable
                                            dataType={"review data"}
                                        />
                                    )}
                                </Col>
                            </Row>
                        </Card>
                    </Container>
                </Tab>
            </Tabs>
        </Container>
    );
};

export default UserDashboard;
