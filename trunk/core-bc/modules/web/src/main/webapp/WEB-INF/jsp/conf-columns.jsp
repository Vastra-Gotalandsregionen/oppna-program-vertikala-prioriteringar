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

<div style="background-color: #eeeeee; left:100px; top:100px; position:absolute;">

<form action="conf-columns">
<select name="visibleColumns" multiple="true">
  <c:forEach items="${confCols.visibleColumns}" var="column">
     <c:if test="${column.visible}">
       <option value="${column.id}">${column.label}</option>
     </c:if>
  </c:forEach>
</select>

<input type="submit" name="command" value="mkVisible"/>

<select name="hiddenColumns" multiple="true">
  <c:forEach items="${confCols.hiddenColumns}" var="column">
     <c:if test="${!column.visible}">
       <option value="${column.id}">${column.label}</option>
     </c:if>
  </c:forEach>
</select>
</form>

</div>

</body>
</html>