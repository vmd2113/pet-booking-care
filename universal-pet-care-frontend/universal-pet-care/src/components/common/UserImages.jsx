import { Fragment } from "react";
import { Card } from "react-bootstrap";
import PropTypes from "prop-types";
const UserImage = ({
    userId,
    placeholder,
    userPhoto,
    altText = "User_photo",
}) => {
    return (
        <Fragment>
            {userPhoto ? (
                <Card.Img
                    src={`data:image/png;base64, ${userPhoto}`}
                    className="user-image"
                    alt={altText}
                />
            ) : (
                <Card.Img
                    src={placeholder}
                    className={`${userId} ${altText}`}
                    alt={altText}
                />
            )}
        </Fragment>
    );
};

UserImage.propTypes = {
    userId: PropTypes.number.isRequired, 
    placeholder: PropTypes.string, 
    userPhoto: PropTypes.string, 
    altText: PropTypes.string, 
};

export default UserImage;
