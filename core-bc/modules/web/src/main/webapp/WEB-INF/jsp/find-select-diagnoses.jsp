<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:include page="jsp/head.jsp"/>

<body style="height: 100%;">

<jsp:include page="jsp/main-body.jsp" />

<div class="popup-overlay">

<span class="window prio-view">

<form:form action="find-select-diagnoses" method="post" modelAttribute="prio">

  <form:input path="findPattern"/>
  
  <c:forEach items="${prio.findings}" var="item">
    <label for="">
      <form:checkbox path="diagnoserId" value="${item.id}" disabled="${util:contains(prio.diagnoserId, item.id)}" />
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  
  <br/><br/>
  <input type="submit" value="Välj!" name="command" />
  <br/><br/>
  
  <c:forEach items="${prio.selectedDiagnoses}" var="item">
    <label for="">
      <input type="checkbox" name="diagnoserId" value="${item.id}" checked="checked"/>
      ${item.kod} ${item.beskrivning} 
    </label>
    <br/>
  </c:forEach>
  
  <div style="display: none;">
    <jsp:include page="jsp/prio-form.jsp" />
  </div>
  
</form:form>

</span>

</div>

</body>
</html>