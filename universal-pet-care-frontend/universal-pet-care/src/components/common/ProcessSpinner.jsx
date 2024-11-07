/* eslint-disable react/prop-types */
/* eslint-disable no-unused-vars */
import React from "react";
import { Spinner } from "react-bootstrap";
import PropTypes from "prop-types";

const ProcessSpinner = ({
    size = "sm",
    animation = "border",
    message = "",
}) => {
    return (
        <div className="text-center">
            <Spinner
                as="span"
                animation={animation}
                size={size}
                role="status"
                aria-hidden="true"
            />
            {message && <span className="sr-only">{message}</span>}
        </div>
    );
};

ProcessSpinner.prototypes = {
    size: PropTypes.string,
    animation: PropTypes.string,
    message: PropTypes.string,
};

export default ProcessSpinner;
