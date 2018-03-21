<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		String Title = "Items Purchased";
		List<String> previousItems;
		synchronized(session){
			
			previousItems = (List<String>) session.getAttribute("previousItems");
			
			if(previousItems == null){
				previousItems = new ArrayList<String>();
			}
			
			String newItem = request.getParameter("item");
			if(newItem != null && (!newItem.trim().equals(""))){
				previousItems.add(newItem);
			}
			
			session.setAttribute("previousItems", previousItems);
			
			
		}
		
		
	%>
	
	<h3><%=Title %></h3>
	<%
		if(previousItems.size() ==0){
			%>
			<I>No Items</I>
			<% 		
		}else{
			for(String item : previousItems){
			%>
				<li><%=item %></li>
			<%	
			}
		}
			%>
	
</body>
</html>