<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="vo.Product"%>
<%@page import="dao.ProductDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko">
<head>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title></title>
</head>
<body>
<%@ include file = "/common/navbar.jsp" %>
<div class="container">
<%
	int no = Integer.parseInt(request.getParameter("no"));

	ProductDao productDao = ProductDao.getInstance();
	
	Product product = productDao.selectProductbyNo(no);
%>

</div>
<%@ include file = "/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>