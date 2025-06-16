// HeroSlider.jsx
import React from "react";
import Slider from "react-slick";
import { Link } from "react-router-dom";
import image1 from "../../img/image.png";
import slide2 from "../../img/home-3-slider-2.jpg";
export const HeroSlider = () => {
  const settings = {
    autoplay: true,
    autoplaySpeed: 2000,
    dots: true,
    infinite: true,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  return (
    <section className="container-fluid">
      <Slider {...settings}  style={{marginTop:"170px",height:"280px",width:"1480px",color:"#fff",marginBottom:"100px"}}>
        {/* Slide 1 */}
        <div className="single-slide bg-image-1">
          <div className="container">
            <div className="home-content text-center">
              <div className="row justify-content-end">
                <div className="col-lg-6" style={{marginTop:"30px"}}>
                  <h1>Beautifully Designed</h1>
                  <h2>
                    Cover up front of book and
                    <br />
                    leave summary
                  </h2>
                  <Link to="/cart" className="btn btn-yellow" style={{background:"#FFCC00"}}>
                    Shop Now
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Slide 2 */}
        <div className="bg-image-2">
          <div className="container">
            <div className="home-content text-center">
              <div className="row justify-content-start">
                <div className="col-lg-6" style={{marginTop:"30px"}}>
                  <h1>I Love This Idea!</h1>
                  <h2>
                    Cover up front of book and
                    <br />
                    leave summary
                  </h2>
                  <Link to="/cart" className="btn btn-yellow" style={{background:"#FFCC00"}}>
                    Shop Now
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Slider>
    </section>
  );
};

export default HeroSlider;
