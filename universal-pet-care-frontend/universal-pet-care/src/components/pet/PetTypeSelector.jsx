import React, { useState, useEffect } from "react";
import { Form, Col } from "react-bootstrap";
import AddItemModal from "../modals/AddItemModal";
import { getAllPetTypes } from "./PetService";
import PropTypes from "prop-types";

const PetTypeSelector = ({ value, onChange }) => {
    const [petTypes, setPetTypes] = useState([]);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchAllPetTypes = async () => {
            try {
                const result = await getAllPetTypes();
                setPetTypes(result.data);
            } catch (error) {
                console.error(error.message);
            }
        };
        fetchAllPetTypes();
    }, []);

    //handle type change
    const handleTypeChange = (e) => {
        if (e.target.value === "add-new-item") {
            setShowModal(true);
        } else {
            onChange(e);
        }
    };

    //handle save new item
    const handleSaveNewItem = (newItem) => {
        if (newItem && !petTypes.includes(newItem)) {
            setPetTypes([...petTypes, newItem]);
            onChange({ target: { name: "petType", value: newItem } });
        }
    };

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId="petType">
                <Form.Control
                    as="select"
                    name="petType"
                    value={value}
                    required
                    onChange={handleTypeChange}
                >
                    <option value="">...select type...</option>
                    {petTypes.map((type) => (
                        <option key={type} value={type}>
                            {type}
                        </option>
                    ))}
                    <option value="add-new-item">Add New</option>
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal}
                handleClose={() => setShowModal(false)}
                itemLabel={"Type"}
                handleSave={handleSaveNewItem}
            />
        </React.Fragment>
    );
};

PetTypeSelector.propTypes = {
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
};
export default PetTypeSelector;
