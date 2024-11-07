/* eslint-disable no-unused-vars */
import React from "react";
import { Carousel, Container, Col, Row, Card } from "react-bootstrap";
import RatingStars from "../rating/RatingStars";
import { Link } from "react-router-dom";
import placeholderImage from "../../assets/images/placeholder.jpg";
import PropTypes from "prop-types";
const VetSlider = ({ vets }) => {
    return (
        <main>
            <Carousel interval={5000} indicators={true} controls={true}>
                {vets &&
                    vets.map((vet, index) => (
                        <Carousel.Item key={index}>
                            <Row className="align-items-center">
                                <Col xs={12} md={4} className="text-center">
                                    <Card.Img
                                        src={
                                            vet.photo
                                                ? `data:image/png;base64,${vet.photo}`
                                                : placeholderImage
                                        }
                                        alt={"photo"}
                                        style={{
                                            maxWidth: "400px",
                                            maxHeight: "400px",
                                            objectFit: "contain",
                                        }}
                                    />
                                </Col>
                                <Col xs={12} md={8}>
                                    <div>
                                        <RatingStars
                                            rating={vet.averageRating}
                                        />
                                    </div>
                                    <div>
                                        <p className="text-success">
                                            Dr.{" "}
                                            {`${vet.firstName} ${vet.lastName}`}
                                        </p>
                                    </div>
                                    <p>{vet.specialization}</p>
                                    <p>
                                        <span className="text-info">
                                            Dr.{" "}
                                            {`${vet.firstName} ${vet.lastName}`}{" "}
                                            is a {vet.specialization}.
                                        </span>
                                        Lorem ipsum dolor sit amet consectetur
                                        adipisicing elit. Maxime mollitia,
                                        molestiae quas vel sint commodi
                                        repudiandae consequuntur voluptatum
                                        laborum numquam blanditiis harum
                                        quisquam eius sed odit fugiat iusto fuga
                                        praesentium optio, eaque rerum!
                                        Provident similique accusantium nemo
                                        autem. Veritatis obcaecati tenetur iure
                                        corporis!
                                    </p>
                                    <p>
                                        <Link
                                            className="me-3 link-2"
                                            to={`/vet-reviews/${vet.id}/veterinarian`}
                                        >
                                            What are people saying about
                                        </Link>
                                        Dr. {`${vet.firstName} ${vet.lastName}`}{" "}
                                        ?
                                    </p>
                                    <p>
                                        <Link className="me-3" to={"/doctors"}>
                                            Meet all Veterinarians
                                        </Link>
                                    </p>
                                </Col>
                            </Row>
                        </Carousel.Item>
                    ))}
            </Carousel>
        </main>
    );
};

VetSlider.propTypes = {
    vets: PropTypes.arrayOf(PropTypes.object),
};

export default VetSlider;
