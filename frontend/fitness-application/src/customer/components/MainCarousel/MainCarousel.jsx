import React from "react";
import AliceCarousel from "react-alice-carousel";
import "react-alice-carousel/lib/alice-carousel.css";
import { MainCarouselData } from "./MainCarouselData";

const items = MainCarouselData.map((item) => (
  <img className="cursor-pointer" src={item.image} alt="" role="presentation" />
));

const MainCarousel = () => (
  <AliceCarousel
    mouseTracking
    items={items}
    controlsStrategy="alternate"
    autoPlay="true"
    infinite
    autoPlayInterval={2000}
    autoHeight
    disableButtonsControls
  />
);

export default MainCarousel;
