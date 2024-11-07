/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { BsCheck, BsX } from "react-icons/bs";
import VetSpecializationSelector from "./VetSpecializationSelector";
import { Button, Form } from "react-bootstrap";
import PropTypes from "prop-types";

const VetEditableRows = ({ vet, onSave, onCancel }) => {
    const [editedVet, setEditedVet] = useState(vet);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedVet((prevState) => ({ ...prevState, [name]: value }));
    };

    const handleSave = () => {
        onSave(vet.id, editedVet, onCancel);
    };

    return (
        <tr>
            <td>
                <Form.Control
                    type="text"
                    name="firstName"
                    value={editedVet.firstName}
                    onChange={handleInputChange}
                />
            </td>
            <td>
                <Form.Control
                    type="text"
                    name="lastName"
                    value={editedVet.lastName}
                    onChange={handleInputChange}
                />
            </td>

            <td>
                <Form.Control
                    type="email"
                    name="email"
                    value={editedVet.email}
                    onChange={handleInputChange}
                />
            </td>
            <td>
                <Form.Control
                    type="text"
                    name="phoneNumber"
                    value={editedVet.phoneNumber}
                    onChange={handleInputChange}
                />
            </td>

            <td>
                <Form.Control
                    as="select"
                    name="gender"
                    value={editedVet.gender}
                    onChange={handleInputChange}
                >
                    <option value="">Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </Form.Control>
            </td>

            <td>
                <VetSpecializationSelector
                    value={editedVet.specialization}
                    onChange={handleInputChange}
                />
            </td>

            <td>
                <Button
                    variant="success"
                    size="sm"
                    onClick={handleSave}
                    className="me-2"
                >
                    <BsCheck />
                </Button>

                <Button variant="secondary" size="sm" onClick={onCancel}>
                    <BsX />
                </Button>
            </td>
        </tr>
    );
};
VetEditableRows.propTypes = {
    vet: PropTypes.object.isRequired,
    onSave: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
};

export default VetEditableRows;
