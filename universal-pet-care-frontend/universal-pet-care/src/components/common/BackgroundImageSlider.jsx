import { useState } from "react";
import bg1 from "../../assets/images/bg1.png";
import bg from "../../assets/images/bg.jpg";
import bg3 from "../../assets/images/bg3.png";
import { Carousel } from "react-bootstrap";

const BackgroundImageSlider = () => {
    const backgrounds = [bg1, bg, bg3];
    const [index, setIndex] = useState(0);

    const handleSelect = (selectedIndex) => {
        setIndex(selectedIndex);
    };
    return (
        <div className="background-slider">
            <Carousel
                activeIndex={index}
                onSelect={handleSelect}
                interval={20000}
            >
                {backgrounds.map((background, idx) => (
                    <Carousel.Item key={idx}>
                        <img
                            className="d-block w-100"
                            src={background}
                            alt={`Slide ${idx}`}
                        />
                    </Carousel.Item>
                ))}
            </Carousel>
        </div>
    );
};

export default BackgroundImageSlider;
