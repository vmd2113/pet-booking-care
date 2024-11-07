/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { ResponsiveContainer, Tooltip, PieChart, Pie, Cell } from "recharts";
import { getAggregatedUsersAccountByActiveStatus } from "../user/UserService";

const AccountChart = () => {
    const [accountData, setAccountData] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const getAccountActivity = async () => {
            try {
                const response =
                    await getAggregatedUsersAccountByActiveStatus();
                const accountActivity = response.data;

                // Transform the backend data into the desired format
                const transformedData = Object.entries(accountActivity).flatMap(
                    ([status, counts]) => [
                        {
                            name: "Active Patients",
                            value: status === "Enabled" ? counts.PATIENT : 0,
                            color: "#d26161",
                        },
                        {
                            name: "Non-Active Patients",
                            value: status === "Enabled" ? 0 : counts.PATIENT,
                            color: "#926262",
                        },
                        {
                            name: "Active Veterinarians",
                            value: status === "Enabled" ? counts.VET : 0,
                            color: "#2f6a32",
                        },
                        {
                            name: "Non-Active Veterinarians",
                            value: status === "Enabled" ? 0 : counts.VET,
                            color: "#557a56",
                        },
                    ]
                );
                setAccountData(transformedData);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        getAccountActivity();
    }, []);

    return (
        <div>
            <h5 className="mt-4 chart-title">Account Activity Overview</h5>
            <ResponsiveContainer width="80%" height={400}>
                <PieChart>
                    <Pie
                        data={accountData}
                        dataKey="value"
                        nameKey="name"
                        outerRadius={110}
                        fill="#8884d8"
                        label
                    >
                        {accountData.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={entry.color} />
                        ))}
                    </Pie>
                    <Tooltip />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
};

export default AccountChart;
