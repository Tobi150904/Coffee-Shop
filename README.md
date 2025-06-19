# Coffee-Shop
Hệ thống Quản lý Quán Cafe (Coffee Shop Management System)

Mô tả dự án

Đây là một ứng dụng web toàn diện được thiết kế để quản lý các hoạt động chính của một quán cafe nhỏ hoặc vừa. Hệ thống bao gồm các chức năng quản lý sản phẩm, đơn hàng, bàn, thanh toán, kho nguyên liệu, khách hàng thân thiết, nhân viên và báo cáo thống kê. Đặc biệt, ứng dụng còn hỗ trợ khả năng đặt hàng trực tuyến cho khách hàng, mang lại trải nghiệm tiện lợi và hiệu quả cho cả người dùng nội bộ và khách hàng.

Các tính năng chính

1. Quản lý Sản phẩm/Menu

•
Thêm/Sửa/Xóa sản phẩm (đồ uống, đồ ăn nhẹ) với thông tin chi tiết (tên, giá, mô tả, danh mục).

•
Quản lý danh mục sản phẩm.

•
Tìm kiếm và lọc sản phẩm theo tên, danh mục.

2. Quản lý Đơn hàng

•
Tạo đơn hàng mới từ nhân viên tại quầy/bàn hoặc từ khách hàng đặt trực tuyến.

•
Cập nhật trạng thái đơn hàng (đang chờ, đang pha chế, đã hoàn thành, đã thanh toán, đã giao hàng).

•
Xem chi tiết và hủy đơn hàng.

3. Quản lý Bàn (nếu là quán có chỗ ngồi)

•
Thêm/Sửa/Xóa bàn, quản lý trạng thái bàn (trống, đang có khách, đã đặt trước).

•
Hỗ trợ chuyển bàn, gộp bàn.

4. Thanh toán

•
Tự động tính tổng tiền đơn hàng.

•
Hỗ trợ áp dụng giảm giá/khuyến mãi (tùy chọn).

•
Xác nhận thanh toán và ghi nhận trạng thái.

•
Tạo hóa đơn chi tiết để in hoặc hiển thị.

5. Quản lý Kho nguyên liệu

•
Thêm/Sửa/Xóa nguyên liệu thô (hạt cà phê, sữa, đường, v.v.) với số lượng tồn kho và đơn vị tính.

•
Định lượng nguyên liệu cho từng sản phẩm.

•
Tự động trừ kho khi sản phẩm được bán.

•
Cảnh báo tồn kho thấp.

6. Quản lý Khách hàng thân thiết

•
Lưu trữ thông tin khách hàng (tên, số điện thoại, email).

•
Tự động tích điểm dựa trên giá trị đơn hàng.

•
Cho phép khách hàng sử dụng điểm để đổi ưu đãi/giảm giá.

•
Xem lịch sử giao dịch của khách hàng.

7. Quản lý Nhân viên/Người dùng

•
Hệ thống đăng nhập/đăng xuất an toàn.

•
Phân quyền người dùng (Admin, Nhân viên).

•
Thêm/Sửa/Xóa thông tin nhân viên (tên, tài khoản, mật khẩu, vai trò).

8. Thống kê/Báo cáo

•
Báo cáo doanh thu theo ngày/tháng/năm.

•
Liệt kê sản phẩm bán chạy nhất.

•
Thống kê số lượng đơn hàng đã xử lý.

•
Báo cáo tồn kho nguyên liệu.

9. Tìm kiếm và Lọc Nâng cao

•
Tìm kiếm sản phẩm, đơn hàng, nhân viên theo nhiều tiêu chí.

•
Áp dụng bộ lọc dữ liệu linh hoạt.

Công nghệ sử dụng

•
Backend: Java (Spring Boot Framework)

•
Frontend: HTML, CSS, JavaScript (có thể tích hợp React, Angular, Vue.js)

•
Cơ sở dữ liệu: MySQL

•
Web server/Database server: XAMPP (cho MySQL và phpMyAdmin)

•
Giao tiếp: RESTful API giữa Frontend và Backend

Lợi ích của dự án

•
Kỹ năng Full-stack: Thể hiện khả năng từ thiết kế database, phát triển RESTful API backend với Spring Boot, đến xây dựng giao diện người dùng web tương tác với HTML/CSS/JavaScript.

•
Dự án thực tế và hiện đại: Kiến trúc phổ biến trong phát triển ứng dụng web hiện nay, cho thấy khả năng xây dựng các hệ thống phức tạp và có tính ứng dụng cao.

•
Kỹ năng quản lý dữ liệu: Làm việc với các bảng, mối quan hệ, truy vấn phức tạp trong MySQL.

•
Kỹ năng OOP và thiết kế API: Áp dụng các nguyên tắc hướng đối tượng để xây dựng một hệ thống có cấu trúc, dễ bảo trì và mở rộng, đồng thời thiết kế các API rõ ràng, hiệu quả.

•
Xử lý lỗi và bảo mật: Thể hiện khả năng xử lý các trường hợp ngoại lệ, xác thực người dùng (Authentication) và phân quyền (Authorization) trong môi trường web.

•
Khả năng học hỏi và tích hợp: Thể hiện khả năng học các framework và công nghệ mới.

Đối tượng người dùng

•
Admin (Quản lý): Có quyền truy cập đầy đủ vào tất cả các chức năng quản lý (sản phẩm, nguyên liệu, nhân viên, báo cáo, v.v.) thông qua giao diện web quản trị.

•
Nhân viên (Staff): Có quyền truy cập vào các chức năng liên quan đến việc bán hàng tại quầy/bàn, tạo đơn hàng, thanh toán, quản lý bàn thông qua giao diện web dành cho nhân viên.

•
Khách hàng (Customer): Có thể truy cập giao diện web riêng biệt để xem menu, đặt hàng trực tuyến, theo dõi trạng thái đơn hàng và quản lý thông tin khách hàng thân thiết.

Cải tiến tiềm năng

•
Trực quan hóa dữ liệu: Biểu đồ doanh thu, biểu đồ sản phẩm bán chạy (sử dụng Chart.js, D3.js).

•
Xuất/Nhập dữ liệu: Cho phép xuất báo cáo (CSV, Excel, PDF) và nhập dữ liệu (Excel/CSV).

•
Nhật ký hoạt động người dùng: Ghi lại các hành động quan trọng của người dùng.

•
Quản lý ca làm việc: Module quản lý lịch làm việc, phân ca cho nhân viên.

•
Sao lưu và phục hồi dữ liệu: Chức năng sao lưu cơ sở dữ liệu định kỳ.

•
Tích hợp Cổng thanh toán: Cho phép thanh toán trực tuyến qua các cổng thanh toán phổ biến (PayPal, Stripe, VNPay).

