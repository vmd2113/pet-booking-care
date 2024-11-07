/* eslint-disable no-unused-vars */
import React from "react";
import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";
import PropTypes from "prop-types";
const RatingStars = ({ rating }) => {
    const totalStars = 5;
    let stars = [];

    // Add full stars
    for (let i = 1; i <= Math.floor(rating); i++) {
        stars.push(<FaStar key={i} color="#ffc107" />);
    }

    // Add half star
    if (rating % 1 !== 0) {
        stars.push(<FaStarHalfAlt key="half" color="#ffc107" />);
    }
    // Add empty stars
    for (let i = stars.length + 1; i <= totalStars; i++) {
        stars.push(<FaRegStar key={i} color="#ffc107" />);
    }
    return <span className="me-2 ms-2">{stars}</span>;
};

RatingStars.propTypes = {
    rating: PropTypes.number.isRequired,
};

export default RatingStars;
