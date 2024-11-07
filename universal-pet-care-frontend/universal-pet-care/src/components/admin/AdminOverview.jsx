/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import CardComponent from "../cards/CardComponent";
import { BsPeopleFill } from "react-icons/bs";
import {
    countPatients,
    countUsers,
    countVeterinarians,
} from "../user/UserService";
import { countAppointments } from "../appointment/AppointmentService";
import RegistrationChart from "../charts/RegistrationChart";
import AppointmentChart from "../charts/AppointmentChart";
import AccountChart from "../charts/AccountChart";
import VetSpecializationChart from "../charts/VetSpecializationChart";

const AdminOverview = () => {
    const [userCount, setUserCount] = useState(0);
    const [veterinarianCount, setVeterinarianCount] = useState(0);
    const [patientCount, setPatientCount] = useState(0);
    const [appointmentCount, setAppointmentCount] = useState(0);

    useEffect(() => {
        const fetchCounts = async () => {
            try {
                const usrCount = await countUsers();
                const vetCount = await countVeterinarians();
                const patCount = await countPatients();
                const appCount = await countAppointments();
                setUserCount(usrCount);
                setVeterinarianCount(vetCount);
                setPatientCount(patCount);
                setAppointmentCount(appCount);
            } catch (error) {
                console.error("Error fetching counts:", error);
            }
        };

        fetchCounts();
    }, []);

    return (
        <main>
            <h5 className="chart-title">Activities Overview</h5>

            <div className="main-cards">
                <CardComponent
                    label={"USERS"}
                    count={userCount}
                    IconComponent={BsPeopleFill}
                />

                <CardComponent
                    label={"APPOINTMENTS"}
                    count={appointmentCount}
                    IconComponent={BsPeopleFill}
                />

                <CardComponent
                    label={"VETERINARIANS"}
                    count={veterinarianCount}
                    IconComponent={BsPeopleFill}
                />

                <CardComponent
                    label={"PATIENTS"}
                    count={patientCount}
                    IconComponent={BsPeopleFill}
                />
            </div>

            <div className="charts">
                <div className="chart-container">
                    <RegistrationChart />
                </div>

                <div className="chart-container">
                    <AppointmentChart />
                </div>

                <div className="chart-container">
                    <AccountChart />
                </div>
                <div className="chart-container">
                    <VetSpecializationChart />
                </div>
            </div>
        </main>
    );
};
export default AdminOverview;
