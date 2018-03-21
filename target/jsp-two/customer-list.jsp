<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page errorPage="myerror.jsp?from=customer-detail.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</head>
<body>
	<%@ include file="header.jsp" %>
	<jsp:useBean id="custBean" scope="session" class="com.cdc.mitrais.model.CustomerBean"/>
	
	<basefont face="Arial">
	<div class="container">
	  <center><span class="badge badge-success"><h3>Product List</h3></span></center>
	  <br>
	  <table class="table">
	    <thead class="thead-dark">
	      <tr>
	        <th>Product ID</th>
	        <th>Description</th>
	        <th>Manufacturer</th>
	        <th>Price</th>
	      </tr>
	    </thead>
	    <tbody>
			<%
			int rowCount = 0;
			int startRow = 0;
			if (custBean.populate()) {
				String start = (String) request.getParameter("start");
				
				if (start != null) {
					startRow = new Integer(start).intValue();
					custBean.setStartRow(startRow);
				}
				
				while (rowCount < 10 && custBean.nextRow() > 0) {
					rowCount++;					
			%>
			<tr>
				<td width="20%"><jsp:getProperty name="productBean" property="prodID"/></td>
				<td width="30%"><jsp:getProperty name="productBean" property="prodDesc"/></td>
				<td width="30%"><jsp:getProperty name="productBean" property="prodManuf"/></td>
				<td width="20%"><jsp:getProperty name="productBean" property="prodPrice"/></td>
			</tr>
			<%
				}
			}
			%>
			<tr>
				<td colspan="2" align="center">
					<br><a href="?start=<%= (startRow > 9) ? startRow - 10 : 0%>">Back</a>
				</td>
				<td colspan="2" align="center">
					<br><a href="?start=<%= custBean.getCurrentRow() %>">Next</a>
				</td>
			</tr>
			</tbody>
		  </table>
		</div>
	
	
</body>
</html>