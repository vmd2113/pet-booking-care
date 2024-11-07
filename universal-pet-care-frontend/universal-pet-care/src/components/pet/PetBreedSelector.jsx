import React, { useState, useEffect } from "react";
import { Form, Col } from "react-bootstrap";
import AddItemModal from "../modals/AddItemModal";
import { getAllPetBreeds } from "./PetService";
import PropTypes from "prop-types";

const PetBreedSelector = ({ petType, value, onChange }) => {
    const [petBreeds, setPetBreeds] = useState([]);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        if (petType) {
            const fetchAllPetBreeds = async () => {
                try {
                    const result = await getAllPetBreeds(petType);
                    setPetBreeds(result.data);
                } catch (error) {
                    console.error(error.message);
                }
            };
            fetchAllPetBreeds();
        } else {
            setPetBreeds([]);
        }
    }, [petType]);

    //handle breed change
    const handleBreedChange = (e) => {
        if (e.target.value === "add-new-item") {
            setShowModal(true);
        } else {
            onChange(e);
        }
    };

    //handle save new item
    const handleSaveNewItem = (newItem) => {
        if (newItem && !petBreeds.includes(newItem)) {
            setPetBreeds([...petBreeds, newItem]);
            onChange({ target: { name: "petBreed", value: newItem } });
        }
    };

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId="petBreed">
                <Form.Control
                    as="select"
                    name="petBreed"
                    value={value || " "}
                    required
                    onChange={handleBreedChange}
                >
                    <option value="">...select breed...</option>
                    {petBreeds.map((breed) => (
                        <option key={breed} value={breed}>
                            {breed}
                        </option>
                    ))}
                    <option value="add-new-item">Add New</option>
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal}
                handleClose={() => setShowModal(false)}
                itemLabel={"Breed"}
                handleSave={handleSaveNewItem}
            />
        </React.Fragment>
    );
};

PetBreedSelector.propTypes = {
    petType: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
};

export default PetBreedSelector;
