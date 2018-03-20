<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<jsp:useBean id="productBean" scope="session" class="com.cdc.mitrais.model.ProductBean"></jsp:useBean>
<%@page errorPage="myerror.jsp?from=populate.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Product List</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</head>
<body>
	<basefont face="Arial">
	<!-- Build table of products -->
	<table align="center" width="600">
		<tr>
			<td width="20%"><b>Product ID</b></td>
			<td width="30%"><b>Description</b></td>
			<td width="30%"><b>Manufacturer</b></td>
			<td width="20%"><b>Price</b></td>
		</tr>
			<%
			int rowCount = 0;
			int startRow = 0;
			if (productBean.populate()) {
				String start = (String) request.getParameter("start");
				
				if (start != null) {
					startRow = new Integer(start).intValue();
					productBean.setStartRow(startRow);
				}
				
				while (rowCount < 10 && productBean.nextRow() > 0) {
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
			<!-- Display the back and next links -->
			<tr>
				<td colspan="2" align="center">
					<br><a href="?start=<%= (startRow > 9) ? startRow - 10 : 0%>">Back</a>
				</td>
				<td colspan="2" align="center">
					<br><a href="?start=<%= productBean.getCurrentRow() %>">Next</a>
				</td>
			</tr>
			</table>


</body>
</html>