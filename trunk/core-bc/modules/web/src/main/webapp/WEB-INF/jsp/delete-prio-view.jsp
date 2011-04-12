<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="jsp/head.jsp"/>
<body style="height: 100%;">

<span class="window prio-view view_details_color">

<form:form action="delete-prio" method="post" modelAttribute="prio" cssClass="values">
<jsp:include page="jsp/prio-form.jsp" />
<div style="vertical-align: middle;">
  <tags:editSubmit name="ok" value="Radera det här prioriteringsobjektet *" overrideEdit="true"/>
  <input class="button" type="submit" value="Avbryt" name="cancel"/>
</div>
<br/>
<div>
  <c:choose>
    <c:when test="${prio.draft}">
      * Det här är utkasts-versionen av prioriteringsobjektet. Tas den här bort försvinner posten 
      permanent från applikationen.
    </c:when>
    <c:otherwise>
      * Det här kommer ta bort den godkända versionen av posten - den som vanliga användare ser i applikationen. 
      <br/> Utkast-versionen kommer fortfarande finnas kvar (den går också att radera genom att återigen 
      välja posten och trycka på 'Ta bort'-knappen).
    </c:otherwise>
  </c:choose>  
</div>
</form:form>
</span>

</body>
</html>