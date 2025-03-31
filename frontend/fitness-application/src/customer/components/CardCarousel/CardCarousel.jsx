import React, { useState } from "react";
import HomeSectionCard from "../HomeSectionCards/HomeSectionCard";
import AliceCarousel from "react-alice-carousel";
import "react-alice-carousel/lib/alice-carousel.css";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";
import KeyboardArrowLeftIcon from "@mui/icons-material/KeyboardArrowLeft";
import { ToggleButton } from "@mui/material";
import { Tshirts } from "../../../Data/Tshirts";

const responsive = {
  0: { items: 1 },
  720: { items: 3 },
  1024: { items: 5.5 },
};

const createItems = (length, [handleClick]) => {
  let deltaX = 0;
  let difference = 0;
  const swipeDelta = 20;

  return Array.from({ length }).map((item, i) => (
    <div
      data-value={i + 1}
      className="item"
      onMouseDown={(e) => (deltaX = e.pageX)}
      onMouseUp={(e) => (difference = Math.abs(e.pageX - deltaX))}
      onClick={() => difference < swipeDelta && handleClick(i)}
    >
      <HomeSectionCard product={Tshirts[i]} />
    </div>
  ));
};

const CardCarousel = () => {
  const [activeIndex, setActiveIndex] = useState(0);
  const [items] = useState(createItems(Tshirts.length, [setActiveIndex]));

  const slidePrev = () =>
    setActiveIndex((prevIndex) => Math.max(prevIndex - 1, 0));
  const slideNext = () =>
    setActiveIndex((prevIndex) => Math.min(prevIndex + 1, items.length - 1));
  const syncActiveIndexForSwipeGestures = (EventObject) =>
    setActiveIndex(e.item);

  return (
    <div className="">
      <div className="relative p-5 ">
        <AliceCarousel
          mouseTracking
          disableDotsControls
          disableButtonsControls
          items={items}
          activeIndex={activeIndex}
          responsive={responsive}
        />
        {activeIndex < items.length - responsive[1024].items && (
          <ToggleButton
            variant="contained"
            className="z-50 bg-white"
            onClick={slideNext}
            sx={{
              position: "absolute",
              top: "8rem",
              right: "0rem",
              transform: "translateX(50%)",
              bgcolor: "white",
            }}
            aria-label="next"
          >
            <KeyboardArrowRightIcon />
          </ToggleButton>
        )}

        {activeIndex > 0 && (
          <ToggleButton
            variant="contained"
            className="z-50 bg-white"
            onClick={slidePrev}
            sx={{
              position: "absolute",
              top: "8rem",
              left: "0rem",
              transform: "translateX(-50%)",
              bgcolor: "white",
            }}
            aria-label="prev"
          >
            <KeyboardArrowLeftIcon />
          </ToggleButton>
        )}
      </div>
    </div>
  );
};

export default CardCarousel;
