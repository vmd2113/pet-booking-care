/* eslint-disable no-unused-vars */
import React from "react";
import PropTypes from "prop-types";

const CardComponent = ({ label, count, IconComponent }) => {
    return (
        <div className="admin-card">
            <div className="card-inner">
                {label}
                <IconComponent className="card-icon" />
            </div>
            <h3>{count.data}</h3>
        </div>
    );
};
CardComponent.propTypes = {
    label: PropTypes.string.isRequired,
    count: PropTypes.object,
    IconComponent: PropTypes.elementType.isRequired,
};

export default CardComponent;
