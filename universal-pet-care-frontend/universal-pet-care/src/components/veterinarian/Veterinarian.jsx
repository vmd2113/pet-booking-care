/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { Container, Row, Card, Col } from "react-bootstrap";
import UserImage from "../common/UserImages";
import { BsFillArrowRightSquareFill } from "react-icons/bs";
import Review from "../review/Review";
import UseMessageAlerts from "../hooks/UseMessageAlerts";
import { getUserById } from "../user/UserService";
import AlertMessage from "../common/AlertMessage";
import RatingStars from "../rating/RatingStars";
import Rating from "../rating/Rating";
import Paginator from "../common/Paginator";
import LoadSpinner from "../common/LoadSpinner";

const Veterinarian = () => {
    const [vet, setVet] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const { vetId } = useParams();
    const [currentPage, setCurrentPage] = useState(1);
    const [reviewPerPage] = useState(2);

    const { errorMessage, showErrorAlert, setErrorMessage, setShowErrorAlert } =
        UseMessageAlerts();

    const getUser = async () => {
        setIsLoading(true);
        try {
            const result = await getUserById(vetId);
            setVet(result.data);
            setTimeout(() => {
                setIsLoading(false);
            }, 1000);
        } catch (error) {
            setErrorMessage(error.response.data.message);
            setShowErrorAlert(true);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        getUser();
    }, [vetId]);

    const vetReviews = vet?.reviews || [];
    const indexOfLastReview = currentPage * reviewPerPage;
    const indexOfFirstReview = indexOfLastReview - reviewPerPage;
    const currentReviews =
        vetReviews.slice(indexOfFirstReview, indexOfLastReview) || [];

    if (isLoading) {
        return (
            <div>
                <LoadSpinner />
            </div>
        );
    }

    return (
        <Container className="d-flex justify-content-center align-items-center mt-5">
            {showErrorAlert && (
                <AlertMessage type={"danger"} message={errorMessage} />
            )}
            {vet && (
                <Card className="review-card mt-2">
                    <Row>
                        <Col>
                            <UserImage
                                userId={vet.id}
                                userPhoto={vet.photo}
                                altText={`${vet.firstName}'s photo`}
                            />
                        </Col>
                        <Col>
                            <Link to={"/doctors"}>
                                <BsFillArrowRightSquareFill /> back to
                                veterinarians
                            </Link>
                        </Col>
                    </Row>
                    <Card.Body>
                        <Card.Title>
                            Dr. {vet.firstName} {vet.lastName}
                        </Card.Title>
                        <Card.Text>
                            Specialization : {vet.specialization}
                        </Card.Text>

                        {vet.averageRating > 0 && (
                            <Card.Text className="rating-stars">
                                Ratings: (
                                {vet.averageRating > 0
                                    ? Number(vet.averageRating.toFixed(1))
                                    : "0.0"}
                                ) stars
                                <RatingStars rating={vet.averageRating} /> rated
                                by ({vet.totalReviewers || 0}{" "}
                                {vet.totalReviewers === 1 ? "person" : "people"}
                                ){" "}
                            </Card.Text>
                        )}
                        <Link
                            to={`/book-appointment/${vet.id}/new-appointment`}
                            className="link"
                        >
                            Book appointment
                        </Link>
                        <hr />

                        <p className="about">
                            About Dr. {vet.firstName} {vet.lastName}{" "}
                        </p>
                        <p>
                            Lorem ipsum dolor sit amet consectetur adipisicing
                            elit. Maxime mollitia, molestiae quas vel sint
                            commodi repudiandae consequuntur voluptatum laborum
                            numquam blanditiis harum quisquam eius sed odit
                            fugiat iusto fuga praesentium optio, eaque rerum!
                            Provident similique accusantium nemo autem.
                            Veritatis obcaecati tenetur iure corporis!
                        </p>
                        <hr />
                        <Rating veterinarianId={vet.id} onReviewSubmit={null} />
                        <h4 className="text-center mb-4">Reviews</h4>
                        <hr />

                        {/* Render paginated reviews */}
                        {currentReviews && currentReviews.length > 0 ? (
                            currentReviews.map((review) => (
                                <Review
                                    key={review.id}
                                    review={review}
                                    userType={vet.userType}
                                />
                            ))
                        ) : (
                            <p>No reviews available yet.</p>
                        )}

                        <Paginator
                            itemsPerPage={reviewPerPage}
                            totalItems={vet.reviews.length}
                            paginate={setCurrentPage}
                            currentPage={currentPage}
                        ></Paginator>
                    </Card.Body>
                </Card>
            )}
        </Container>
    );
};
export default Veterinarian;
