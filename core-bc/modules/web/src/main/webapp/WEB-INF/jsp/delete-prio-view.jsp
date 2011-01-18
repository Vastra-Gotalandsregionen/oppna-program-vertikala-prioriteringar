<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="jsp/head.jsp"/>
<body style="height: 100%;">

<span class="window prio-view">

<form:form action="delete-prio" method="post" modelAttribute="prio" cssClass="values">
<jsp:include page="jsp/prio-form.jsp" />
<div style="vertical-align: middle;">
  <tags:editSubmit value="Radera det h�r prioriteringsobjektet" overrideEdit="true"/>
  <a href="main" style="float:right">St�ng</a>
</div>
</form:form>
</span>

</body>
</html>