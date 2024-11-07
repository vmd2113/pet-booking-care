/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { generateColor } from "../utils/utilities";
import {
    ResponsiveContainer,
    BarChart,
    XAxis,
    YAxis,
    Tooltip,
    Legend,
    Bar,
    Cell,
} from "recharts";
import { aggregateVetBySpecialization } from "../user/UserService";
import NoDataAvailable from "../common/NoDataAvailable";

const VetSpecializationChart = () => {
    const [vetSpecialization, setVetSpecialization] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const fetchVeterinariansAndProcessData = async () => {
            try {
                const response = await aggregateVetBySpecialization();
                const veterinarians = response;
                const processedData = veterinarians.map((vet) => ({
                    ...vet,
                    color: generateColor(vet.specialization),
                }));
                setVetSpecialization(processedData);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };
        fetchVeterinariansAndProcessData();
    }, []);

    return (
        <section>
            {setVetSpecialization && vetSpecialization.length > 0 ? (
                <React.Fragment>
                    <ResponsiveContainer width={"80%"} height={400}>
                        <h5 className="mb-4 mt-4 chart-title">
                            Veterinarians by Specializations
                        </h5>
                        <BarChart data={vetSpecialization}>
                            <XAxis
                                dataKey="specialization"
                                angle={-30}
                                textAnchor="end"
                                height={70}
                            />
                            <YAxis />

                            <Tooltip
                                content={(props) => {
                                    const { payload } = props;
                                    if (payload && payload.length) {
                                        return (
                                            <div
                                                style={{
                                                    backgroundColor: "#aab5b0",
                                                }}
                                                className="p-4"
                                            >
                                                <p className="text-primary">
                                                    {
                                                        payload[0].payload
                                                            .specialization
                                                    }
                                                    : {payload[0].payload.count}
                                                </p>
                                            </div>
                                        );
                                    }
                                    return null;
                                }}
                            />
                            <Legend layout="vertical" />
                            <Bar dataKey="count" fill="#8884d8">
                                {vetSpecialization.map((entry, index) => (
                                    <Cell
                                        key={`cell-${index}`}
                                        fill={entry.color}
                                    />
                                ))}
                            </Bar>
                        </BarChart>
                    </ResponsiveContainer>
                </React.Fragment>
            ) : (
                <NoDataAvailable
                    dataType={" vet specialization data "}
                    message={errorMessage}
                />
            )}
        </section>
    );
};

export default VetSpecializationChart;
