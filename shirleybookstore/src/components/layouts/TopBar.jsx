import React from 'react';

export const TopBar = () => {
  return (
    <div className="fixed-topbar">
      <div className="container-fluid bg-dark">
        <div className="row py-2 px-lg-5">
          <div className="col-lg-6 text-center text-lg-left mb-2 mb-lg-0">
            <div className="d-inline-flex align-items-center text-white">
              <small><i className="fa fa-phone mr-2"></i>+012 345 6789</small>
              <small className="px-3">|</small>
              <small><i className="fa fa-envelope mr-2"></i>SherlyBook@gmail.com</small>
            </div>
          </div>
          <div className="col-lg-6 text-center text-lg-right">
            <div className="d-inline-flex align-items-center">
              <a className="text-white px-2" href="https://www.facebook.com/">
                <i className="fa fa-facebook"></i>
              </a>
              <a className="text-white px-2" href="https://x.com/?lang=vi">
                <i className="fa fa-twitter"></i>
              </a>
              <a className="text-white px-2" href="https://www.linkedin.com/">
                <i className="fa fa-linkedin"></i>
              </a>
              <a className="text-white px-2" href="https://www.instagram.com/">
                <i className="fa fa-instagram"></i>
              </a>
              <a className="text-white pl-2" href="https://www.youtube.com/">
                <i className="fa fa-youtube-play"></i>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
