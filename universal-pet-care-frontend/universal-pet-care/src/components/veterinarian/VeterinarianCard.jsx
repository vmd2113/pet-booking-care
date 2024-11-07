import PropTypes from "prop-types";
import { Accordion, Col, Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import UserImage from "../common/UserImages.jsx";
import placeholder from "../../assets/images/placeholder.jpg";

const VeterinarianCard = ({ vet }) => {
    return (
        <div>
            <Col key={vet.id} className="mb-4 xs={12}">
                <Accordion>
                    <Accordion.Item eventKey="0">
                        <Accordion.Header>
                            <div className="d-flex align-items-center">
                                <Link to={""}>
                                    <UserImage
                                        userId={vet.id}
                                        userPhoto={vet.photo}
                                        placeholder={placeholder}
                                    />
                                </Link>
                            </div>
                            <div className="flex-grow-1 ml-3 px-5">
                                <Card.Title className="title">
                                    Dr.{vet.firstName} {vet.lastName}
                                </Card.Title>
                                <Card.Title>
                                    <h6>{vet.specialization}</h6>
                                </Card.Title>
                                <Card.Text className="review rating-stars">
                                    Reviews: Some stars
                                </Card.Text>
                                <Link
                                    to={`/book-appointment/${vet.id}/new-appointment`}
                                    className="link-1"
                                >
                                    Book appointment
                                </Link>
                            </div>
                        </Accordion.Header>
                        <Accordion.Body>
                            <div>
                                <Link
                                    to={`/veterinarians/${vet.id}/veterinarian`}
                                    className="link-2"
                                >
                                    See what people are saying about
                                </Link>{" "}
                                <span className="margin-left-space">
                                    Dr.{vet.firstName}
                                </span>
                            </div>
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Col>
        </div>
    );
};

// Define prop types for the component
VeterinarianCard.propTypes = {
    vet: PropTypes.shape({
        id: PropTypes.number.isRequired,
        firstName: PropTypes.string.isRequired,
        lastName: PropTypes.string.isRequired,
        photo: PropTypes.string,
        specialization: PropTypes.string.isRequired,
    }).isRequired,
};

export default VeterinarianCard;
