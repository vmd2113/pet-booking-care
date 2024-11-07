/* eslint-disable no-unused-vars */
import React from "react";
import { Button, InputGroup, Form } from "react-bootstrap";
import PropTypes from "prop-types";

const UserFilter = ({
    label,
    values = [],
    selectedValue,
    onSelectedValue,
    onClearFilters,
}) => {
    return (
        <InputGroup className="mb-2">
            <InputGroup.Text>Filter by {label}</InputGroup.Text>
            <Form.Select
                className="form-control"
                value={selectedValue}
                onChange={(e) => onSelectedValue(e.target.value)}
            >
                <option value="">Select {label.toLowerCase()}</option>
                {values.map((value, index) => (
                    <option key={index} value={value}>
                        {value}
                    </option>
                ))}
            </Form.Select>
            <Button variant="secondary" onClick={onClearFilters}>
                Clear Filter
            </Button>
        </InputGroup>
    );
};
UserFilter.propTypes = {
    label: PropTypes.string.isRequired,
    values: PropTypes.arrayOf(PropTypes.string),
    selectedValue: PropTypes.string,
    onSelectedValue: PropTypes.func,
    onClearFilters: PropTypes.func,
};

export default UserFilter;
