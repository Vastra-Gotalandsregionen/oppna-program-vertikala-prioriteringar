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
    <span class="kod-label">Godkänd</span>
    ${su:toStringDate(prio.godkaend)}
  </div>
  <div class="yui3-u-1-5">
    <span class="kod-label">Senast uppdaterad</span>
    ${su:toStringDate(prio.senastUppdaterad)}
  </div>
  <div class="yui3-u-1-5"></div>
  <div class="yui3-u-1-5"></div>
  <div class="yui3-u-1-5" align="right">
    <input class="button" type="submit" value="Avbryt" name="cancel"/>
    <tags:editSubmit value="Spara" name="save" />
  </div>
</div>

<jsp:useBean id="messageHome" class="se.vgregion.verticalprio.controllers.MessageHome" scope="session"/>
<span style="color:red;"><jsp:getProperty property="message" name="messageHome"/></span>
<jsp:setProperty property="message" name="messageHome" value=""/>

</form:form>

</span>


</body>
</html>