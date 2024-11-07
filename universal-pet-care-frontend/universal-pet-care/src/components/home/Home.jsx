/* eslint-disable react/no-unescaped-entities */
import d5 from "../../assets/images/d5.jpg";
import vett from "../../assets/images/vett.jpg";
import { Col, Row, Button, Card, ListGroup, Container } from "react-bootstrap";

import VetSlider from "../../components/veterinarian/VetSlider";
import NoDataAvailable from "../common/NoDataAvailable";
import { useEffect, useState } from "react";
import { getVeterinarians } from "../veterinarian/VeterinarianService";

const Home = () => {
    const [vets, setVets] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        getVeterinarians()
            .then((vets) => {
                setVets(vets.data);
            })
            .catch((error) => {
                setErrorMessage(error.message || "Something went wrong!");
            });
    }, []);

    return (
        <Container className="home-container mt-5">
            <Row>
                <Col md={6} className="mb-3">
                    <Card>
                        <Card.Img
                            variant="top"
                            src={d5}
                            alt="About Us"
                            className="hero-image"
                        />
                        <Card.Body>
                            <h2 className="text-info">Who We Are</h2>
                            <Card.Title>
                                Comprehensive Care for your Furry Friends
                            </Card.Title>
                            <Card.Text>
                                At Universal Pet Care, we believe every pet
                                deserves the best. Our team of dedicated
                                professionals is here to ensure your pet's
                                health and happiness through comprehensive
                                veterinary services. With decades of combined
                                experience, our veterinarians and support staff
                                are committed to providing personalized care
                                tailored to the unique needs of each pet.our
                                veterinarians and support staff are committed to
                                providing personalized care tailored to the
                                unique needs of each pet.
                            </Card.Text>
                            <Card.Text>
                                We offer a wide range of services, from
                                preventive care and routine check-ups to
                                advanced surgical procedures and emergency care.
                                Our state-of-the-art facility is equipped with
                                the latest in veterinary technology, which
                                allows us to deliver high-quality care with
                                precision and compassion. We offer a wide range
                                of services, from preventive care and routine
                                check-ups to advanced surgical procedures and
                                emergency care. Our state-of-the-art facility is
                                equipped with the latest in veterinary
                                technology, which allows us to deliver
                                high-quality care with precision and compassion.
                                Our state-of-the-art facility is equipped with
                                the latest in veterinary technology, which
                                allows us to deliver high-quality care.
                            </Card.Text>
                            <Button variant="outline-info">
                                {" "}
                                Meet Our veterinarians
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={6} className="mb-3">
                    <Card className="service-card">
                        <Card.Img
                            variant="top"
                            src={vett}
                            alt="About Us"
                            className="hero-image"
                        />
                        <Card.Body>
                            <h2 className="text-info">Our Services</h2>
                            <Card.Title>What we do</Card.Title>
                            <ListGroup className="services-list">
                                <ListGroup.Item>
                                    Veterinary Check-ups
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    Emergency Surgery
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    Pet Vaccinations
                                </ListGroup.Item>
                                <ListGroup.Item>Dental Care</ListGroup.Item>
                                <ListGroup.Item>
                                    Spaying and Neutering
                                </ListGroup.Item>
                                <ListGroup.Item>
                                    And many more...
                                </ListGroup.Item>
                            </ListGroup>
                            <Card.Text className="mt-3">
                                From routine check-ups to emergency surgery, our
                                full range of veterinary services ensures your
                                pet's health is in good hands.
                            </Card.Text>
                            <Button variant="outline-info">
                                {" "}
                                Meet Our veterinarians
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <div className="card mb-5">
                <h5>
                    What people are saying about{" "}
                    <span className="text-info"> Universal Pet Care</span>{" "}
                    Veterinarians
                </h5>
                <hr />
                {vets.length > 0 ? (
                    <VetSlider vets={vets} />
                ) : (
                    <NoDataAvailable
                        dataType="veterinarians data"
                        errorMessage={errorMessage}
                    />
                )}
            </div>
        </Container>
    );
};

export default Home;
