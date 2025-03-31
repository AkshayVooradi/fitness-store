import CardCarousel from "../../customer/components/CardCarousel/CardCarousel";
import MainCarousel from "../../customer/components/MainCarousel/MainCarousel";

const Homepage = () => {
  return (
    <div>
      <MainCarousel></MainCarousel>
      <div className="space-y-10 py-20 flex flex-col justify-center px-5 lg:px-10">
        <CardCarousel></CardCarousel>
        <CardCarousel></CardCarousel>
        <CardCarousel></CardCarousel>
        <CardCarousel></CardCarousel>
      </div>
    </div>
  );
};

export default Homepage;
