import { useEffect, useState } from "react";
import { format } from "date-fns";

export const useAlertWithTimeout = (
    initialVisibility = false,
    duration = 10000
) => {
    const [isVisible, setIsVisible] = useState(initialVisibility);

    useEffect(() => {
        let timer;
        if (isVisible) {
            timer = setTimeout(() => {
                setIsVisible(false);
            }, duration);
        }
        return () => clearTimeout(timer);
    }, [isVisible, duration]);

    return [isVisible, setIsVisible];
};

/* The purpose of this code is to generate a unique color based on the input string. The color
is generated using a hash function, which ensures that different input strings will result
in different colors */

/* Dynamic color generator function that takes a single parameter "str" */
export const generateColor = (str) => {
    /*1. The first if statement checks if the input str is a string and if it has a length greater than 0.
  If the input is not a string or has a length of 0,
   the function returns a default color #8884d8. */

    if (typeof str !== "string" || str.length === 0) {
        return "#8884d8"; // Default color
    }

    /*
    2. Hash Generation:
  The function then initializes a variable hash with the value 0.
  It then loops through each character in the str parameter and performs 
  a hash calculation using the following formula: */

    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }

    /* 3. Color Generation:
  After the hash calculation, the function calculates the hue value by taking the hash value
   modulo 360. This ensures that the hue value is within the range of 0 to 359 degrees, which
  represents the color wheel.
  The function then returns a color in the HSL (Hue, Saturation, Lightness) color format,
  using the calculated hue value and setting the saturation to 70% and the lightness to 50%. */

    const hue = hash % 360;
    return `hsl(${hue}, 70%, 50%)`;
};

/**
 * Formats the given date and time.
 * @param {Date | string} date - The date to format.
 * @param {Date | string} time - The time to format.
 * @returns {Object} An object containing formatted date and time strings.
 */
export const dateTimeFormatter = (date, time) => {
    const formattedDate = format(date, "yyyy-MM-dd");
    const formattedTime = format(time, "HH:mm");
    return { formattedDate, formattedTime };
};

export const UserType = {
    PATIENT: "PATIENT",
    VET: "VET",
};

/* enum constants converter */
export const formatAppointmentStatus = (status) => {
    return status.toLowerCase().replace(/_/g, "-");
};
