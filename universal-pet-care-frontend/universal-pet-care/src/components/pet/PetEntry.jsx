// eslint-disable-next-line no-unused-vars
import React from "react";
import PetColorSelector from "./PetColorSelector";
import PetBreedSelector from "./PetBreedSelector";
import PetTypeSelector from "./PetTypeSelector";
import PropTypes from "prop-types";

import {
    Col,
    Form,
    OverlayTrigger,
    Tooltip,
    Button,
    Row,
} from "react-bootstrap";
import { FaMinus } from "react-icons/fa";

const PetEntry = ({ pet, index, removePet, canRemove, handleInputChange }) => {
    return (
        <fieldset className="field-set mb-4">
            <legend className="legend">{`Pet #${index + 1} details`}</legend>

            <fieldset className="mb-4">
                <Form.Group as={Row}>
                    <Col md={6}>
                        <Form.Control
                            type="text"
                            name="petName"
                            id={`petName -${index}`}
                            value={pet.petName}
                            placeholder="Enter pet name"
                            onChange={handleInputChange}
                            required
                        />
                    </Col>
                    <Col md={6}>
                        <Form.Control
                            type="number"
                            name="petAge"
                            id="petAge"
                            value={pet.petAge}
                            placeholder="Enter pet age"
                            onChange={handleInputChange}
                            required
                        />
                    </Col>
                </Form.Group>
            </fieldset>

            <Form.Group as={Col} className="mb-4">
                <PetColorSelector
                    value={pet.petColr}
                    onChange={handleInputChange}
                />
            </Form.Group>

            <fieldset className="field-set mb-4">
                <legend className="legend">Pet type and Breed</legend>
                <Form.Group as={Row} className="mb-2 d-flex">
                    <Col>
                        <PetTypeSelector
                            value={pet.petType}
                            onChange={handleInputChange}
                        />
                    </Col>
                    <Col>
                        <PetBreedSelector
                            petType={pet.petType}
                            value={pet.petBreed}
                            onChange={handleInputChange}
                        />
                    </Col>
                </Form.Group>
            </fieldset>

            {canRemove && (
                <div className="d-flex justify-content-end mt-2">
                    <OverlayTrigger overlay={<Tooltip>remove pets</Tooltip>}>
                        <Button
                            variant="danger"
                            size="sm"
                            onClick={() => removePet(index)}
                        >
                            <FaMinus />
                        </Button>
                    </OverlayTrigger>
                </div>
            )}
        </fieldset>
    );
};

PetEntry.propTypes = {
    pet: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired,
    removePet: PropTypes.func.isRequired,
    canRemove: PropTypes.bool.isRequired,
    handleInputChange: PropTypes.func.isRequired,
};

export default PetEntry;
