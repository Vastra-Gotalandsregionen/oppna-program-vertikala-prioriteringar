<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="filter-sidebar">
    <c:choose>
        <c:when test="${user != null and loginResult}">
            <a href="main?logout=true">Logga ut ${user.firstName} ${user.lastName}</a>
        </c:when>
        <c:otherwise>
            <form method="post" action="main">Användare<br />
                <input type="text" name="userName" /> <br />
                Lösen<br />
                <input type="password" name="password" /> <br />
                <input type="submit" name="login" value="Logga in" />
            </form>
        </c:otherwise>
    </c:choose>
    <form:form commandName="form" action="check" method="POST">
        <br />
        <tags:sector sector="${form.allSektorsRaad}" />
        <br />
        <tags:sectors items="${form.sectors}" />
    </form:form>
</div>