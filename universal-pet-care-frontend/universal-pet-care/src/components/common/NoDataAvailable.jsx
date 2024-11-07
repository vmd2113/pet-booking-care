/* eslint-disable no-unused-vars */
import React from "react";
import PropTypes from "prop-types";
const NoDataAvailable = ({ dataType, errorMessage }) => {
    return (
        <div className="text-center mt-5">
            <h4>No {dataType} available at the moment</h4>
            {errorMessage && <p className="text-danger">{errorMessage}</p>}
        </div>
    );
};

NoDataAvailable.propTypes = {
    dataType: PropTypes.string.isRequired, // Kiểu string và bắt buộc
    errorMessage: PropTypes.string, // Kiểu string, không bắt buộc
};
export default NoDataAvailable;
