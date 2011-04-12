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
  <tags:editSubmit name="ok" value="Radera det h�r prioriteringsobjektet *" overrideEdit="true"/>
  <input class="button" type="submit" value="Avbryt" name="cancel"/>
</div>
<br/>
<div>
  <c:choose>
    <c:when test="${prio.draft}">
      * Det h�r �r utkasts-versionen av prioriteringsobjektet. Tas den h�r bort f�rsvinner posten 
      permanent fr�n applikationen.
    </c:when>
    <c:otherwise>
      * Det h�r kommer ta bort den godk�nda versionen av posten - den som vanliga anv�ndare ser i applikationen. 
      <br/> Utkast-versionen kommer fortfarande finnas kvar (den g�r ocks� att radera genom att �terigen 
      v�lja posten och trycka p� 'Ta bort'-knappen).
    </c:otherwise>
  </c:choose>  
</div>
</form:form>
</span>

</body>
</html>