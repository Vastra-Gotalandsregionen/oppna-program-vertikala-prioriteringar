<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="filter-sidebar">
	<div class="column-controls">
	    <ul class="clearfix">
	        <li class="column-control column-control-hide"><a href="" title="G&ouml;m kolumnen med sektorsr&aring;d."><span>G&ouml;m</span></a></li>
	        <li class="column-control column-control-show aui-helper-hidden"><a href="" title="Visa kolumnen med sektorsr&aring;d."><span>Visa</span></a></li>
	    </ul>
	</div>
	
	<div class="filter-sidebar-content">
	    <c:choose>
	        <c:when test="${user != null and loginResult}">
	            <a href="main?logout=true">Logga ut ${user.firstName} ${user.lastName}</a>
	        </c:when>
	        <c:otherwise>
                <portlet:actionURL var="loginUrl">
                    <portlet:param name="action" value="login"/>
                </portlet:actionURL>
	            <form method="post" action="${loginUrl}">Användare<br />
	                <input type="text" name="userName" /> <br />
	                Lösen<br />
	                <input type="password" name="password" /> <br />
	                <input type="submit" name="login" value="Logga in" />
	            </form>
	        </c:otherwise>
	    </c:choose>
        <portlet:actionURL var="checkUrl">
            <portlet:param name="action" value="check"/>
        </portlet:actionURL>
	    <form:form commandName="form" action="${checkUrl}" method="POST">
	        <br />
	        <tags:sector sector="${form.allSektorsRaad}" />
	        <br />
	        <tags:sectors items="${form.sectors}" />
	        
	        <br>
	        
	        <a class="sector-node" href="http://www.vgregion.se/sv/Regionkansliet/Halso--och-sjukvardsavdelningen/Strategisk-utvecklingsenhet/Medicinska-prioriteringar/Annestesioperationintensivvard" target="anestesi">
	          Annestesi
	        </a>
	    </form:form>
	
	</div>

</div>

<div class="logo-wrap">
	<img class="logo" src="${pageContext.request.contextPath}/img/vgr_logo_vit.gif" alt="VGR Logo" />
</div>