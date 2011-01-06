<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="jsp/head.jsp"/>
<body style="height: 100%;">
<div class="popup-overlay">
</div>

<span class="window prio-view">

<form:form action="choose-codes" method="post" modelAttribute="prioCondition">

  <div class="values">
    Valda koder
    <%-- <tags:find-select-codes label="Sök diagnoser (kod/besk.):" codeRefName="diagnosRef" codeRef="${prio.diagnosRef}" submitName="findAndSelectCodes" styleClass="diagnosRef"/> --%>
    <tags:find-select-codes label="Sök (kod/besk.):" codeRefName="${param.codeRefName}" codeRef="${prioCondition[param.codeRefName]}" submitName="findAndSelectCodes" styleClass="diagnosRef"/>
    
  </div>

  <input type="hidden" name="codeRefName" value="${param.codeRefName}" />

  <a href="main">Ok</a>

</form:form>

</span>


</body>
</html>