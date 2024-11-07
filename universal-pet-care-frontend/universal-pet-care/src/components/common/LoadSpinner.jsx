/* eslint-disable no-unused-vars */
import React from "react";
import { Spinner } from "react-bootstrap";
import PropTypes from "prop-types";

const LoadSpinner = ({ variant = "success" }) => {
    return (
        <div
            className="d-flex justify-content-center align-items-center mt-5"
            style={{ height: "100%" }}
        >
            <Spinner animation="border" variant={variant} />
        </div>
    );
};

LoadSpinner.propTypes = {
    variant: PropTypes.string,
};

export default LoadSpinner;
