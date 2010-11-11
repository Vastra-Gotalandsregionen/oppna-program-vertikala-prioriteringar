<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body style="height:100%; ">

<jsp:include page="jsp/main-body.jsp"/>
<jsp:useBean id="confCols" scope="session" class="se.vgregion.verticalprio.ConfColumnsForm"></jsp:useBean>

<div style="left:0px; top:0px; height:100%; width:100%; position:absolute; z-index:10;"></div>

<div style="background-color: #eeeeee; left:100px; top:100px; position:absolute; z-index:100;">

<form action="conf-columns">
<select name="hiddenColumns" multiple="multiple">
  <c:forEach items="${confCols.hiddenColumns}" var="column">
    <option value="${column.id}" id="${column.id}">${column.label}</option>
  </c:forEach>
</select>

<input type="submit" name="command" value="show" id="show-selected"/>
<input type="submit" name="command" value="hide"/>

<select name="visibleColumns" multiple="multiple">
  <c:forEach items="${confCols.visibleColumns}" var="column">
    <option value="${column.id}" id="${column.id}">${column.label}</option>
  </c:forEach>
</select>

</form>

</div>

</body>
</html>