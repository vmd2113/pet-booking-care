/* eslint-disable no-unused-vars */
import React from "react";
import { Pagination } from "react-bootstrap";
import PropTypes from "prop-types";
const Paginator = ({ itemsPerPage, totalItems, currentPage, paginate }) => {
    let active = currentPage;
    let items = [];

    for (
        let number = 1;
        number <= Math.ceil(totalItems / itemsPerPage);
        number++
    ) {
        items.push(
            <Pagination.Item
                key={number}
                active={number === active}
                onClick={() => paginate(number)}
            >
                {number}
            </Pagination.Item>
        );
    }

    return (
        <div className="d-flex justify-content-center">
            <Pagination>{items}</Pagination>
        </div>
    );
};

Paginator.propTypes = {
    itemsPerPage: PropTypes.number.isRequired,
    totalItems: PropTypes.number.isRequired,
    currentPage: PropTypes.number.isRequired,
    paginate: PropTypes.func.isRequired,
};

export default Paginator;
