/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";
import { BsCheck, BsX } from "react-icons/bs";
import PropTypes from "prop-types";
const EditablePetRow = ({ pet, index, onSave, onCancel }) => {
    const [editPet, setEditPet] = useState(pet);

    const handlePetChange = (e) => {
        const { name, value } = e.target;
        setEditPet((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    return (
        <tr>
            <td>
                <Form.Control
                    type="text"
                    name="name"
                    value={editPet.name}
                    onChange={handlePetChange}
                />
            </td>

            <td>
                <Form.Control
                    type="text"
                    name="type"
                    value={editPet.type}
                    onChange={handlePetChange}
                />
            </td>

            <td>
                <Form.Control
                    type="text"
                    name="breed"
                    value={editPet.breed}
                    onChange={handlePetChange}
                />
            </td>

            <td>
                <Form.Control
                    type="text"
                    name="color"
                    value={editPet.color}
                    onChange={handlePetChange}
                />
            </td>
            <td>
                <Form.Control
                    type="number"
                    name="age"
                    value={editPet.age}
                    onChange={handlePetChange}
                />
            </td>
            <td>
                {" "}
                <Button
                    variant="success"
                    size="sm"
                    onClick={() => onSave(pet.id, editPet)}
                    className="me-2"
                >
                    <BsCheck />
                </Button>
            </td>
            <td colSpan={2}>
                <Button variant="secondary" size="sm" onClick={onCancel}>
                    <BsX />
                </Button>
            </td>
        </tr>
    );
};

EditablePetRow.propTypes = {
    pet: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired,
    onSave: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
};

export default EditablePetRow;
