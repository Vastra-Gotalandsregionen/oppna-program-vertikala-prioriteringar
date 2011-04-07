<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="jsp/head.jsp"/>
<body style="height: 100%;">

<span class="window prio-view">

<form:form action="prio" method="post" modelAttribute="prio" cssClass="values">
<jsp:include page="jsp/prio-form.jsp" />

<div style="vertical-align: middle;" class="yui3-g">
  <div class="yui3-u-1-5">
    <span class="kod-label">Godk�nd datum</span>
    <c:choose>
      <c:when test="${prio.child != null}">${su:toStringDate(prio.child.godkaend)}</c:when>
      <c:otherwise>${su:toStringDate(prio.godkaend)}</c:otherwise>
    </c:choose>
  </div>
  <div class="yui3-u-1-5">
    <span class="kod-label">Senast uppdaterad datum</span>   
    <c:choose>
   		<c:when test="${prio.senastUppdaterad != null}">
      		${su:toStringDate(prio.senastUppdaterad)}
     	</c:when>
      	<c:otherwise>-</c:otherwise>
    </c:choose>
  </div>
  <div class="yui3-u-1-5"></div>
  <div class="yui3-u-1-5"></div>
  <div class="yui3-u-1-5" align="right">
    <input class="button" type="submit" value="Avbryt" id="cancel" name="cancel"/>
    <tags:editSubmit value="Spara" name="save" />
  </div>
</div>

<jsp:useBean id="messageHome" class="se.vgregion.verticalprio.controllers.MessageHome" scope="session"/>
<span style="color:red;"><jsp:getProperty property="message" name="messageHome"/></span>
<jsp:setProperty property="message" name="messageHome" value=""/>

<c:if test="${prio.child != null and su:isPriosDifferent(prio, prio.child)}">
  <div>
    <br/>
    <img src='img/flag_white.gif' name="changed"/> = F�ltv�rde som skiljer sig fr�n den godk�nda versionen.
  </div>
</c:if>
</span>
</form:form>
</body>
</html>