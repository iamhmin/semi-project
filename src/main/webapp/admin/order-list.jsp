<%@page import="java.util.StringJoiner"%>
<%@page import="dto.OrderMemberInfoDto"%> 
<%@page import="org.apache.commons.lang3.ObjectUtils"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="dto.OrderDetailDto"%>
<%@page import="vo.Criteria2"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.math.NumberUtils"%>
<%@page import="vo.Pagination2"%>
<%@page import="dao.OrderDao"%>
<%@page import="vo.Order"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko">
<head>
   <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
   <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" >
   <link rel="stylesheet" href="../resources/css/style.css" />
    <title></title>
</head>
<body>
<%@ include file="admin-common.jsp" %>

<% 
 
OrderDao orderDao = OrderDao.getInstance();

// 총 데이터 갯수를 조회한다.
//int totalRecords = orderDao.selectTotalOrderCount();


//List<Order> orderList = orderDao.selectAllOrders(pagination.getBegin(), pagination.getEnd());


// 요청파라미터에 pageNo값이 존재하지 않으면 Pagination객체에서 1페이지로 설정한다.
	int pageNo = NumberUtils.toInt(request.getParameter("page"), 1);
	String option = StringUtils.defaultString(request.getParameter("option"), "");
	String keyword = StringUtils.defaultString(request.getParameter("keyword"), "");
	
	Criteria2 criteria = new Criteria2();
	
	// 검색옵션과 검색키워드가 모두 있는 경우에만 Criteria객체에 검색옵션과 검색 키워드를 저장한다.
    if (!StringUtils.isEmpty(option) && !StringUtils.isEmpty(keyword)) {
      	criteria.setOption(option);
      	criteria.setKeyword(keyword);
    }
	
	
 // 검색조건에 맞는 게시글의 총 갯수를 조회한다.
    int totalRows = orderDao.getTotalOrderMemberInfoRows(criteria);
 
    // 페이징처리에 필요한 정보를 제공하는 Pagination객체를 생성한다.
    Pagination2 pagination = new Pagination2(pageNo, totalRows);
    
    // 게시글 리스틀 조회할 때 필요한 조회범위를 Criteria객체에 저장한다.
    criteria.setBeginIndex(pagination.getBeginIndex());
    criteria.setEndIndex(pagination.getEndIndex());
	
    
	// 현재 페이지번호에 해당하는 게시글 목록을 조회한다.
List<OrderMemberInfoDto> orderDetailList = orderDao.selectAllOrderMemberInfo(criteria);

%>
<div class="container">    
<div class="row">
		<div class="col breadcrumb">
			<ul class="nav">
				<li class="crumb home"><a href="" class="nav-link p-0">HOME</a></li>
				<li class="crumb">관리자페이지</li>
				<li class="crumb">주문목록</li>
			</ul>
		</div>
	</div>
	<div class="row">
		<div class="col p-0 page-title">
			<h1>관리자페이지</h1>
		</div>
	</div>
	<div class="row mypage">
		<!-- aside 시작 -->
		<div class="col-2 p-0 aside">
			<span class="aside-title">관리자 페이지</span>
			<ul class="nav flex-column p-0">
					<li class=""><a href="member-list.jsp" class="nav-link p-0">회원목록 조회</a></li>
				<li class=""><a href="member-left-list.jsp" class="nav-link p-0">탈퇴회원 목록 조회</a></li>
				<li class=""><a href="product-list.jsp" class="nav-link p-0">전체 상품 조회</a></li>
				<li class=""><a href="registerform.jsp" class="nav-link p-0">신규 상품 등록</a></li>
				<li class=""><a href="stock-management.jsp" class="nav-link p-0">재고 관리</a></li>
				<li class=""><a href="order-list.jsp" class="nav-link p-0">주문 관리</a></li>
				<li class=""><a href="qna-list.jsp" class="nav-link p-0">QnA 목록</a></li>
				<li class=""><a href="review-list.jsp" class="nav-link p-0">리뷰 목록</a></li>
			</ul>
		</div>	
		<div class="col-9">
		<h4>주문 목록</h4>
		<table class="table table-hover table-striped">
		<colgroup>
			<col width="3%">
			<col width="5%">
			<col width="20%">
			<col width="8%">
			<col width="8%">
			<col width="8%">
			<col width="11%">
		</colgroup>
		<thead>
			<tr>
				<th>No.</th>
				<th>주문자</th>
				<th>상품명</th>
				<th>총액</th>
				<th>주문일</th>
				<th>주문상태</th>
				<th>취소사유</th>
				
			</tr>
		</thead>
		<tbody>
			<% 
	for (OrderMemberInfoDto order : orderDetailList) {
		List<OrderDetailDto> orderItemList = orderDao.selectAllOrderDetailsByOrderNo(order.getNo());
%>	
			<tr>
			
				<td><a href="order-detail.jsp?no=<%=order.getNo()%>"><%=order.getNo()%></td>		
				<td><%=order.getMemberName() %></td>		
				<td>
<%					
	for(OrderDetailDto orderItem : orderItemList)	{
%>					
				
				<strong><a href="product-detail.jsp?no=<%=orderItem.getProductNo()%>"><%=orderItem.getProductName()%></a></strong>(<%=orderItem.getSize() %>)
				
<% 
	}
%>					
	
				</td>
				<td><%=order.getTotalPrice() %></td>
				<td><small class="text-muted"><%=order.getOrderDate() %></small></td>
<% if ("주문취소".equals(order.getStatus())) { 
%>		
				<td style="font-size: 14px; color: red;"><%=order.getStatus()%></td>
				
<% 
} else if ("배송완료".equals(order.getStatus())) { 
%>		
				<td style="font-size: 14px; color: blue;"><%=order.getStatus()%></td>
				
<% 
} else {
%>
				<td>
				<select name="status" style="height: 29px; font-size: 14px;" id="status-<%=order.getNo()%>" onchange="change(<%=order.getNo() %>)">
						 <option selected disabled value=<%=order.getStatus()%>><%=order.getStatus()%></option>
						 <option value="주문완료">주문완료</option>
						 <option value="상품준비중">상품준비중</option>
						 <option value="배송중">배송중</option>
						 <option value="배송완료">배송완료</option>
  				</select>
				</td>
<% 
} 
%>				
				
				<td><small><%=StringUtils.defaultString(order.getCancelReason(), "") %></small>
				<small class="text-muted"><%=order.getCanceledDate() != null ? "("+order.getCanceledDate()+")" : ""%></small></td>
				
				
			</tr>
<% 
	}
%>			
		</tbody>
	</table>
		<div class="row mb-3">
		<div class="col-6 offset-3">
			<nav>
				<ul class="pagination justify-content-center">
					<!-- 
						Pagination객체가 제공하는 isExistPrev()는 이전 블록이 존재하는 경우 true를 반환한다.
						Pagination객체가 제공하는 getPrevPage()는 이전 블록의 마지막 페이지값을 반환한다.
					 -->
					<li class="page-item <%=!pagination.isExistPrev() ? "disabled" : "" %>">
					<a href="" class="page-link" onclick="moveToPage(event, <%=pagination.getPrev()%>)" >이전</a>
					</li>
<%
	// Pagination 객체로부터 해당 페이지 블록의 시작 페이지번호와 끝 페이지번호만큼 페이지내비게이션 정보를 표시한다.
	for (int num = pagination.getBegin(); num <= pagination.getEnd(); num++) {
%>					
						<li class="page-item <%=num == pagination.getPage() ? "active" : ""%>">
						<a href="" class="page-link" onclick="moveToPage(event, <%=num%>)"><%=num%></a>
					</li>
<%
	}
%>					
					<!-- 
						Pagination객체가 제공하는 isExistNext()는 다음 블록이 존재하는 경우 true를 반환한다.
						Pagination객체가 제공하는 getNexPage()는 다음 블록의 첫 페이지값을 반환한다.
					 -->
					<li class="page-item <%=!pagination.isExistNext() ? "disabled" : "" %>">
					<a href="" class="page-link" onclick="moveToPage(event, <%=pagination.getNext()%>)">다음</a></li>
				</ul>
			</nav>
		</div>
	</div>
		</div>
		<div class="row mb-3">
			<div class="col">
				<!--  
					페이징처리와 검색에 필요한 값을 서버로 제출할 때 사용하는 폼이다.
					페이지번호를 클릭하거나 검색버튼을 클릭하면 폼의 입력요소에 적절한 값을 설정하고, 폼 입력값을 제출한다.
					
					검색옵션과 검색어가 존재하면 해당 해당 옵션이 선택되고, 검색어가 입력필드에 표시된다.
				-->
				<form id="form-search" class="row row-cols-lg-auto g-3" method="get" action="order-list.jsp">
					<input type="hidden" id="page-field" name="page" value="<%=pageNo%>">
					<div class="col-2 offset-3">
						<div class="input-group">
							<select class="form-select" id="search-option" name="option">
								<option value="no" <%="no".equals(option) ? "selected" : ""%>>주문번호</option>
								<option value="memberName" <%="memberName".equals(option) ? "selected" : ""%>>주문자이름</option>
								<option value="productName" <%="productName".equals(option) ? "selected" : ""%>>상품이름</option>
							</select>
						</div>
					</div>
					<div class="col-3">
						<div class="input-group">
							<input type="text" class="form-control" id="search-keyword" name="keyword" value="<%=StringUtils.isBlank(keyword) ? "" : keyword%>"	placeholder="검색어를 입력하세요">
						</div>
					</div>
					<div class="col-2">
						<div class="input-group">
							<button class="btn btn-primary" type="button" id="btn-search" onclick="searchBoards(1)">검색</button>
						</div>
					</div>
				</form>
			</div>
		</div>
</div>	
</div>
<%@ include file="../common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript">
//페이지번호를 클릭했을 때 실행되는 함수
function moveToPage(event, page) {
	event.preventDefault();	// a태그에서 onclick이벤트가 발생하면 href에 정의된 주소로 이동하는 기본동작이 일어나지 않게 함.
	searchBoards(page);
}

//검색버튼을 클릭했을 때 실행되는 함수
function searchBoards(page) {
	document.getElementById("page-field").value = page;
	var form = document.getElementById("form-search");
	form.submit();
}  

function change(orderNo) {
	var status = document.getElementById("status-" + orderNo).value;
	location.href="order-status-change.jsp?no=" + orderNo + "&status=" + status;
}
</script>
</body>
</html>