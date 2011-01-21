<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:forEach items="${items}" var="sector">
  <tags:sector sector="${sector}" />
</c:forEach>