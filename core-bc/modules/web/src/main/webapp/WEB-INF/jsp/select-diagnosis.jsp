<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:include page="jsp/head.jsp"/>

<body style="height: 100%;">

<jsp:include page="jsp/main-body.jsp" />

<div class="popup-overlay select-diagnoses">

<span class="window">
<form action="select-diagnosis" method="post">
  <tags:diagnoses columns="${diagnoses}"/>
</form>
</span>

</div>

</body>
</html>