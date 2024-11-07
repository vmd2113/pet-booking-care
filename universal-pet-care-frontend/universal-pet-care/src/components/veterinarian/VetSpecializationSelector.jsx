/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import AddItemModal from "../modals/AddItemModal";
import { Form, Col, Row } from "react-bootstrap";
import { getAllSpecializations } from "./VeterinarianService";

const VetSpecializationSelector = ({ value, onChange }) => {
    const [specializations, setSpecializations] = useState([]);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchAllSpecializations = async () => {
            try {
                const result = await getAllSpecializations();
                setSpecializations(result.data);
            } catch (error) {
                console.error(error.message);
            }
        };
        fetchAllSpecializations();
    }, []);

    const handleSpecializationChange = (e) => {
        if (e.target.value === "Add-New") {
            setShowModal(true);
        } else {
            onChange(e);
        }
    };

    const handleSaveNewItem = (newItem) => {
        if (newItem && !specializations.includes(newItem)) {
            setSpecializations([...specializations, newItem]);
            onChange({ target: { name: "specialization", value: newItem } });
        }
    };

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId="specialization" className="mb-4">
                <Form.Control
                    as="select"
                    name="specialization"
                    value={value}
                    required
                    onChange={handleSpecializationChange}
                >
                    <option value="">...select specialization...</option>
                    {specializations.map((specialization) => (
                        <option key={specialization} value={specialization}>
                            {specialization}
                        </option>
                    ))}
                    <option value="Add-New">Add New</option>
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal}
                handleClose={() => setShowModal(false)}
                itemLabel={"Specialization"}
                handleSave={handleSaveNewItem}
            />
        </React.Fragment>
    );
};

export default VetSpecializationSelector;
