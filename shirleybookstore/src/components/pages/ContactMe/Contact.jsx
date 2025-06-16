import React from 'react';

const Contact = () => {
  return (
    <div style={{
      maxWidth: '800px',
      margin: '175px auto',
      padding: '20px',
      lineHeight: 1.7,
      backgroundColor: '#fff',
      borderRadius: '10px',
      boxShadow: '0 0 12px rgba(0, 0, 0, 0.1)'
    }}>
      <p style={{ fontSize: '22px', fontWeight: 'bold' }}>
        🌟 Xin chào bạn, người yêu truyện thân mến!
      </p>

      <p>
        Trước tiên, chúng tôi xin chân thành cảm ơn vì sự quan tâm của bạn đến nền tảng <strong>Serley Book Store</strong>.
        Chúng tôi luôn đánh giá cao tinh thần sáng tạo và niềm đam mê viết truyện của cộng đồng tác giả.Chúng tôi luôn mong muốn có thể mang đến trai nghiệm tốt nhất những áng văn chương hay nhất cho cả bạn và những người dùng của chúng tôi.
      </p>

      <p>
        Tuy nhiên,thật tiếc rằng hệ thống đăng ký tác giả  của chúng tôi vẫn đang đang trong quá trình hoàn thiện. Trong thời gian này, nếu bạn mong muốn trở thành một phần của Serley Book Store với vai trò tác giả,
        vui lòng liên hệ trực tiếp với chúng tôi qua email <strong>tienthinh28112004@gmail.com</strong> để được hỗ trợ sớm nhất.
      </p>

      <p style={{fontSize:"18px", marginTop: '20px' }}>
        🔔 Khi gửi email, bạn vui lòng đính kèm:
      </p>
      <ul style={{ paddingLeft: '20px' }}>
        <li>Tên tài khoản đã đăng ký trong hệ thống</li>
        <li>CV của bạn (nếu có)</li>
        <li>Link Facebook cá nhân</li>
        <li>Một vài thông tin giới thiệu về bạn hoặc các dự án bạn từng tham gia</li>
      </ul>

      <p>
        Những thông tin bạn cung cấp sẽ giúp chúng tôi hiểu rõ hơn về bạn và đánh giá một cách công tâm nhất.
      </p>

      <p style={{ marginTop: '20px' }}>
        Sau khi nhận được email, chúng tôi sẽ nhanh chóng xác nhận và liên hệ lại với bạn trong thời gian sớm nhất.
      </p>

    <div className='btn-block'>
        <a
            href="https://mail.google.com/"
            target="_blank"
            rel="noopener noreferrer"
            className='btn'
            style={{background:"#E5FFCC"}}
        >
            Gửi email cho chúng tôi
      </a>
    </div>

      <p style={{ marginTop: '30px' }}>
        Một lần nữa, xin cảm ơn bạn đã quan tâm đến Serley Book Store. Chúc bạn luôn giữ được ngọn lửa đam mê,
        sáng tạo thật nhiều tác phẩm hay và sớm trở thành một phần của cộng đồng tác giả của chúng tôi! ✨
      </p>
    </div>
  );
};

export default Contact;
