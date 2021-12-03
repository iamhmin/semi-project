package dao;

import static utils.ConnectionUtil.getConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.CancelProductDto;
import dto.OrderDetailDto;


import vo.Order;

public class OrderDao {
	
	private static OrderDao self = new OrderDao();
	private OrderDao() {}
	public static OrderDao getInstance() {
		return self;
	}
	
	public CancelProductDto selectCanceledProductDetailByOrderNoAndStockNo(int orderNo, int stockNo) throws SQLException {
		String sql = "select p.product_no, p.product_img, p.product_brand, p.product_name, p.product_price, "
				+ "          p.product_disprice, i.product_amount, s.product_size, s.product_detail_no, s.product_stock "
				+ "from tb_products p, tb_order_item i, tb_product_stocks s "
				+ "where i.product_detail_no = s.product_detail_no "
				+ "and s.product_no = p.product_no "
				+ "and i.order_no = ? "
				+ "and i.product_detail_no = ?";
		
		CancelProductDto canceledProduct = null;
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, orderNo);
		pstmt.setInt(2, stockNo);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) {
			canceledProduct = new CancelProductDto();
			canceledProduct.setProductNo(rs.getInt("product_no"));
			canceledProduct.setPhoto(rs.getString("product_img"));
			canceledProduct.setBrand(rs.getString("product_brand"));
			canceledProduct.setProductName(rs.getString("product_name"));
			canceledProduct.setPrice(rs.getInt("product_price"));
			canceledProduct.setDisprice(rs.getInt("product_disprice"));
			canceledProduct.setAmount(rs.getInt("product_amount"));
			canceledProduct.setSize(rs.getInt("product_size"));
			canceledProduct.setProductDetailNo(rs.getInt("product_detail_no"));
			canceledProduct.setStock(rs.getInt("product_stock"));
		}
		

		rs.close();
		pstmt.close();
		connection.close();
		
		return canceledProduct;
	}
	
	/**
	 * 지정된 주문번호로 주문상태를 반환한다.
	 * @param orderNo
	 * @return
	 * @throws SQLException
	 */
	public Order selectOrderByOrderNo(int orderNo) throws SQLException {
		Order order = null;
		
		String sql = "select order_no, order_status, order_date, order_total_price "
				   + "from tb_orders "
				   + "where order_no = ? ";
		Connection connection = getConnection();
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setInt(1, orderNo);
	    ResultSet rs = pstmt.executeQuery();
	    
	    if (rs.next()) {
	    	order = new Order();
	    	order.setNo(rs.getInt("order_no"));
	    	order.setStatus(rs.getString("order_status"));
	    	order.setOrderDate(rs.getDate("order_date"));
	    	order.setTotalPrice(rs.getInt("order_total_price"));
	    }
	    
	    rs.close();
		pstmt.close();
		connection.close();
		
		return order;
	}
	
	/**
	 * 주문번호를 클릭하면 그 주문번호에 해당하는 주문리스트를 반환한다.
	 * @param orderNo
	 * @return
	 * @throws SQLException
	 */
	public List<OrderDetailDto> selectAllOrderDetailsByOrderNo(int orderNo) throws SQLException {
		List<OrderDetailDto> orderDetails = new ArrayList<>();
		
		String sql = "select i.order_no, p.product_no, p.product_img, p.product_name, p.product_price, "
				   + "p.product_disprice, p.product_brand, i.product_amount, s.product_size, s.product_detail_no, "
				   + "i.review_status "
				   + "from tb_products p, tb_order_item i, tb_product_stocks s "
				   + "where i.product_detail_no = s.product_detail_no "
				   + "and s.product_no = p.product_no "
				   + "and i.order_no = ?";
		
		Connection connection = getConnection();
	    PreparedStatement pstmt = connection.prepareStatement(sql);
	    pstmt.setInt(1, orderNo);
	    ResultSet rs = pstmt.executeQuery();
	    
	    while (rs.next()) {
	    	OrderDetailDto orderDetail = new OrderDetailDto();
	    	
	    	orderDetail.setOrderNo(rs.getInt("order_no"));
	    	orderDetail.setProductNo(rs.getInt("product_no"));
	    	orderDetail.setProductName(rs.getString("product_name"));
	    	orderDetail.setReviewStatus(rs.getString("review_status"));
	    	orderDetail.setPhoto(rs.getString("product_img"));
	    	orderDetail.setPrice(rs.getInt("product_price"));
	    	orderDetail.setDisPrice(rs.getInt("product_disprice"));
	    	orderDetail.setBrand(rs.getString("product_brand"));
	    	orderDetail.setAmount(rs.getInt("product_amount"));
	    	orderDetail.setSize(rs.getInt("product_size"));
	    	orderDetail.setProductDetailNo(rs.getInt("product_detail_no"));
	    	
	    	orderDetails.add(orderDetail);
	    }
	    
	    rs.close();
		pstmt.close();
		connection.close();
		
		return orderDetails;
	}
	
	/**
	 * 지정한 멤버번호와 상품(재고)번호로 한 건의 아이템만 조회한다
	 * @param memberNo
	 * @param stockNo
	 * @return
	 * @throws SQLException
	 */
	public OrderDetailDto selectOrderDetailByMemberNoAndProductDetailNo(int memberNo, int stockNo) throws SQLException {
		
		String sql = "select o.order_no, o.order_status, o.order_date, o.order_total_price, i.review_status, "
				+ "       m.member_no, m.member_id, m.member_name, "
				+ "       i.product_detail_no, i.product_amount, s.product_size, "
				+ "       p.product_no, p.product_name, p.product_category, p.product_price, p.product_disprice, "
				+ "       p.product_img, p.product_brand, p.product_gender "
				+ "from tb_orders o, tb_members m, tb_order_item i, tb_product_stocks s, tb_products p "
				+ "where o.member_no = m.member_no "
				+ "and o.order_no = i.order_no "
				+ "and p.product_no = s.product_no "
				+ "and s.product_detail_no = i.product_detail_no "
				+ "and m.member_no = ? and i.product_detail_no = ? ";
		
		OrderDetailDto orderDetail = null;
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, memberNo);
		pstmt.setInt(2, stockNo);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			orderDetail = new OrderDetailDto();
			
			orderDetail.setOrderNo(rs.getInt("order_no"));
			orderDetail.setStatus(rs.getString("order_status"));
			orderDetail.setOrderDate(rs.getDate("order_date"));
			orderDetail.setTotalPrice(rs.getInt("order_total_price"));
			orderDetail.setReviewStatus(rs.getString("review_status"));
			orderDetail.setMemberNo(rs.getInt("member_no"));
			orderDetail.setMemberId(rs.getString("member_id"));
			orderDetail.setMemberName(rs.getString("member_name"));
			
			orderDetail.setProductDetailNo(rs.getInt("product_detail_no"));
			orderDetail.setAmount(rs.getInt("product_amount"));
			orderDetail.setSize(rs.getInt("product_size"));
			
			orderDetail.setProductNo(rs.getInt("product_no"));
			orderDetail.setProductName(rs.getString("product_name"));
			orderDetail.setCategory(rs.getString("product_category"));
			orderDetail.setPrice(rs.getInt("product_price"));
			orderDetail.setDisPrice(rs.getInt("product_disprice"));
			orderDetail.setPhoto(rs.getString("product_img"));
			orderDetail.setBrand(rs.getString("product_brand"));
			orderDetail.setGender(rs.getString("product_gender"));
		}
		
		rs.close();
		pstmt.close();
		connection.close();
		
		return orderDetail;
	}
	
	public int selectTotalOrderCount() throws SQLException {
		String sql = "select count(*) cnt "
				   + "from tb_orders";
		
		int totalRecords = 0;
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		totalRecords = rs.getInt("cnt");

    return totalRecords;
	}
	
	
	/**
	 * 멤버번호로 지정한 전체 주문내역을 반환한다.
	 * @param memberNo
	 * @return
	 * @throws SQLException
	 */
	public List<Order> selectAllOrdersByMemberNo(int memberNo) throws SQLException {
		String sql = "select order_no, order_status, "
			       + "order_date, order_total_price, cancel_reason,"
				   + "cancel_status, canceled_date, member_no "
				   + "from tb_orders "
				   + "where member_no = ? ";
		
		List<Order> orders = new ArrayList<>();
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, memberNo);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			Order order = new Order();
			
			order.setMemberNo(rs.getInt("member_no"));
			order.setNo(rs.getInt("order_no"));
			order.setStatus(rs.getString("order_status"));
			order.setOrderDate(rs.getDate("order_date"));
			order.setTotalPrice(rs.getInt("order_total_price"));
			order.setCancelReason(rs.getString("cancel_reason"));
			order.setCancelStatus(rs.getString("cancel_status"));
			order.setCanceledDate(rs.getDate("canceled_date"));
		
			
			orders.add(order);
		}
		
		rs.close();
		pstmt.close();
		connection.close();
		
		return orders;
	}
	
	/**
    * 지정된 멤버번호로 주문상태에 따라 주문 개수를 반환한다.
    * @param memberNo
    * @return
    * @throws SQLException
    */
   public int selectOrderCount(int memberNo, String orderStatus) throws SQLException {
      int count = 0;
      
      String sql = "select count(*) cnt "
               + "from tb_orders "
               + "where order_status = ? "
               + "and member_no = ? ";
      
      Connection connection = getConnection();
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, orderStatus);
      pstmt.setInt(2, memberNo);
      ResultSet rs = pstmt.executeQuery();
      
      rs.next();
      count = rs.getInt("cnt");
      
      rs.close();
      pstmt.close();
      connection.close();
      
      return count;
   }

	/**
	 * 전체 주문내역을 반환한다. (관리자용)
	 * @return
	 * @throws SQLException
	 */
	public List<Order> selectAllOrders(int begin, int end) throws SQLException {
		String sql = "select order_no, order_status, member_no, "
				+ "order_date, order_total_price, cancel_reason, "
				+ "cancel_status, canceled_date "
				+ "from (select row_number() over (order by order_no desc) rn, "
				+ "order_no, order_status, member_no, "
				+ "order_date, order_total_price, cancel_reason, "
				+ "cancel_status, canceled_date "
				+ "from tb_orders )"
				+ "where rn>= ? and rn <= ? "
				+ "order by order_no desc";
		
		List<Order> orders = new ArrayList<>();
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, begin);
		pstmt.setInt(2, end);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			Order order = new Order();
			
			order.setNo(rs.getInt("order_no"));
			order.setMemberNo(rs.getInt("member_no"));
			order.setStatus(rs.getString("order_status"));
			order.setOrderDate(rs.getDate("order_date"));
			order.setTotalPrice(rs.getInt("order_total_price"));
			order.setCancelReason(rs.getString("cancel_reason"));
			order.setCancelStatus(rs.getString("cancel_status"));
			order.setCanceledDate(rs.getDate("canceled_date"));
			
			orders.add(order);
		}
		
		rs.close();
		pstmt.close();
		connection.close();
		return orders;
	}

	
	
	/**
	 * 수정된 정보가 포함된 주문 정보를 테이블에 반영한다.
	 * @param order
	 * @throws SQLException
	 */
	public void updateOrder(Order order) throws SQLException {
		String sql = "update tb_orders "
				   + "set "
				   + "	order_status = ?, "
				   + "	order_total_price = ?,"
				   + "	cancel_reason = ?, "
				   + "	cancel_status = ?, "
				   + "	canceled_date = ? "
				   + "where order_no = ? ";
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, order.getStatus());
		pstmt.setInt(2, order.getTotalPrice());
		pstmt.setString(3, order.getCancelReason());
		pstmt.setString(4, order.getCancelStatus());
		pstmt.setDate(5, new java.sql.Date(order.getCanceledDate().getTime()));
		pstmt.setInt(6, order.getNo());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		connection.close();
	}
	
	/**
	 * 새 주문번호를 반환한다.
	 * @return
	 * @throws SQLException
	 */
	public int getOrderNo() throws SQLException {
		String sql = "select order_no_seq.nextval seq from dual";
		
		int orderNo = 0;
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		
		orderNo = rs.getInt("seq");
		
		rs.close();
		pstmt.close();
		connection.close();
		
		return orderNo;
	}
	
	/**
	 * 신규 주문 정보를 저장한다.
	 * @param order
	 * @throws SQLException
	 */
	public void insertOrder(Order order) throws SQLException {
		
		String sql = "insert into tb_orders "
				   + "(order_no, member_no, order_total_price) "
				   + "values (?, ?, ?) ";
		
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		
		pstmt.setInt(1, order.getNo());
		pstmt.setInt(2, order.getMemberNo());
		pstmt.setInt(3, order.getTotalPrice());
		
		pstmt.executeUpdate();
		
		pstmt.close();
		connection.close();
	}
}
