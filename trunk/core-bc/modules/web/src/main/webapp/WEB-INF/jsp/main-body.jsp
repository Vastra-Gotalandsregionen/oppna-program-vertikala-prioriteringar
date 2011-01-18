<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="main-body">
<div class="sectorsAndButtons yui3-g"> 

<span class="yui3-u sectors"><form:form commandName="form"
  action="check" method="POST">
  
<c:choose>
  <c:when test="${user != null and user.editor}"><a href="login">Logga ut</a></c:when>
  <c:otherwise><a href="login">Logga in</a></c:otherwise>
</c:choose>
  
  <br/>
  <br/>
  <tags:sectors items="${form.sectors}" />  
</form:form></span>

<div class="yui3-u rowsAndButtons">
<span class="button-row">
<label for="select-prio"><button class="button left">Visa prioriteringsobjekt</button></label>
<c:if test="${user != null and user.editor}">
  <label for="delete-prio"><tags:editButton value="Radera prioriteringsobjekt" cssClass="left button"></tags:editButton></label>
</c:if>
<form action="prio-create"> <tags:editSubmit value="Skapa prioriteringsobjekt" cssClass="button left" /> </form>
<form action="init-conf-columns"><input class="conf-columns button left" type="submit" value="Dölj/Visa kolumner" /></form>
<button class="cost">Kostnad</button>
<span class="export-data-buttons left button">
<button class="excel left button">Excel</button>
<button class="pdf left button">Pdf</button>
<button class="print left button">Skriv ut</button>
</span>
<button class="help">Hjälp</button>
</span>

<form action="prio-open" method="post" style="clear:left">

<input type="submit" id="select-prio" name="select-prio"/>
<input type="submit" id="delete-prio" name="delete-prio"/>

<table cellpadding="5">
  <thead>
    <td><h3>#</h3></td>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td>
          <h3 title="${column.description}">${column.columnLabel}</h3>
          <tags:cell value="${prioCondition[column.name]}"/>
        </td>
      </c:if>
    </c:forEach>
  </thead>
  <tbody>
    <c:forEach items="${rows}" var="row" varStatus="vs">
      <tr class="${vs.index % 2 == 0 ? 'even' : 'odd'}">
        <td><input type="radio" name="id" value="${row.id}"${vs.index == 0 ? ' checked' : ''}/></td>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <td class="${column.name}"><tags:cell value="${row[column.name]}"/></td>
          </c:if>
        </c:forEach>
      </tr>
    </c:forEach>
  </tbody>
</table>
</form>

</div>

</div>

</div>
