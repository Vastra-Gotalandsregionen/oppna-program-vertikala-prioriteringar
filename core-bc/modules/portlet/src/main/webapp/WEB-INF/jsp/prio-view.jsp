<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<jsp:include page="head.jsp"/>

<div class="window prio-view view_details_color">

    <portlet:actionURL var="prioViewFormUrl">
        <portlet:param name="action" value="prioViewForm"/>
    </portlet:actionURL>
    <form:form action="${prioViewFormUrl}" method="post" modelAttribute="prio" cssClass="values">
    <jsp:include page="prio-form.jsp" />

    <div style="vertical-align: middle;" class="yui3-g">
      <div class="yui3-u-1-5">
        <span class="kod-label">Godkänd datum</span>
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
      <div class="yui3-u-2-5" align="right">
        <span style="white-space: nowrap;">
          <input type="button" class="button" value="Skriv ut" onclick="window.print()">
          <input class="button" type="submit" value="Avbryt" id="cancel" name="cancel"/>
          <tags:editSubmit value="Spara" name="save" />
          <tags:editSubmit value="Kopiera" name="copy" />
        </span>
      </div>
    </div>

    <jsp:useBean id="messageHome" class="se.vgregion.verticalprio.controllers.MessageHome" scope="session"/>
    <span style="color:red;"><jsp:getProperty property="message" name="messageHome"/></span>
    <jsp:setProperty property="message" name="messageHome" value=""/>

      <br/>
      <div id="ChangeFlag" style="display: ${(prio.child != null and su:isPriosDifferent(prio, prio.child)) ? 'block' : 'none'}">
        <img src='${pageContext.request.contextPath}/img/flag_white.gif'/> = Fältvärde som skiljer sig från den godkända versionen.
      </div>

      <div id="EditedFlag" style="display: ${(su:isPriosDifferent(prio, prio.unalteredVersion)) ? 'block' : 'none'}">
        <img src='${pageContext.request.contextPath}/img/changed.png'/> = Ej sparat värde.
      </div>
    </form:form>
</div>