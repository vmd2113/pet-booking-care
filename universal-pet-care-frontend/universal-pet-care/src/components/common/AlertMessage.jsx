import { Alert } from "react-bootstrap";
import PropTypes from "prop-types";
const AlertMessage = ({ type, message }) => {
    if (!message) return null;
    return (
        <Alert variant={type} dismissible>
            {message}
        </Alert>
    );
};

AlertMessage.propTypes = {
    type: PropTypes.string.isRequired, // 'type' is required and should be a string
    message: PropTypes.string, // 'message' is required and should be a string
};

export default AlertMessage;
