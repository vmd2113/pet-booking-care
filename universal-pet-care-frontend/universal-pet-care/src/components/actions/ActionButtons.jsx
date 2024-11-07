/* eslint-disable no-unused-vars */
import React from "react";
import { Button } from "react-bootstrap";
import PropTypes from "prop-types";

const ActionButtons = ({
    title,
    variant,
    onClick,
    disabled,
    className = "",
}) => {
    return (
        <Button
            variant={variant}
            size="sm"
            disabled={disabled}
            onClick={onClick}
            className={className}
        >
            {title}
        </Button>
    );
};
ActionButtons.propTypes = {
    title: PropTypes.string,
    variant: PropTypes.string,
    onClick: PropTypes.func,
    disabled: PropTypes.bool,
    className: PropTypes.string,
};

export default ActionButtons;
