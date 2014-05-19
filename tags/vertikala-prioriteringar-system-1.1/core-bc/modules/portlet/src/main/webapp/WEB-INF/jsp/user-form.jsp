<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<portlet:actionURL var="doUserAction">
    <portlet:param name="action" value="doUserAction"/>
</portlet:actionURL>

<div>
  <jsp:include page="head.jsp"/>
  <div style="height: 100%;">
  
  	<div class="window prio-view view_details_color yui3-g">
      <div class="values">
      	<form:form action="${doUserAction}" method="post" modelAttribute="otherUser">
          <c:choose>
            <c:when test="${otherUser.id != null}">
              <h2 class="yui3-u-1">Ändra användare</h2>
            </c:when>
            <c:otherwise>
              <h2 class="yui3-u-1">Lägg till ny användare</h2>
            </c:otherwise>
          </c:choose>
          <div style="margin: auto; text-align: left;" class="yui3-u-1-2 otherUser">
              <form:hidden path="id"/>
              <div class="yui3-g">
                <span class="user-label yui3-u-1-3">Vgr-id</span> <form:input path="vgrId"/> <br/>
                <span class="user-label yui3-u-1-3">Lösen</span> <form:input path="password" /> <br/>
                <span class="user-label yui3-u-1-3">Förnamn</span> <form:input path="firstName"/> <br/>
                <span class="user-label yui3-u-1-3">Efternamn</span> <form:input path="lastName"/> <br/>
                <span class="user-label yui3-u-1-3">Ändra</span> <form:checkbox path="editor"/> <br/>
                <span class="user-label yui3-u-1-3">Godkänna</span> <form:checkbox path="approver"/> <br/>
                <span class="user-label yui3-u-1-3">Ändra användare</span> <form:checkbox path="userEditor" /> <br/> 
              </div>
              <br/>
              <input name="save" type="submit" value="Spara" />
              <input name="cancel" type="submit" value="Avbryt" />
           </div>
           
          <div class="yui3-u-1-2 otherUser">
              <tags:sectors items="${otherUser.sektorRaad}" />
          </div>
           
      	</form:form>
        

      </div>
  	</div>
  
  </div>
</div>
