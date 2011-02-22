<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="main-body">
<div class="sectorsAndButtons yui3-g"> 

<span class="yui3-u sectors">

<c:choose>
  <c:when test="${user != null and loginResult}">
    <a href="logout">Logga ut ${user.firstName} ${user.lastName}</a></c:when>
  <c:otherwise>
  <form method="post" action="login">
    Användare<br/> <input type="text" name="userName"/> <br/>
    Lösen<br/> <input type="password" name="password"/> <br/>
    <input type="submit" name="login" value="Logga in"/>
  </form>  
  </c:otherwise>
</c:choose>

<form:form commandName="form" action="check" method="POST">
  <br/>
  <tags:sector sector="${form.allSektorsRaad}" />
  <br/>
  <tags:sectors items="${form.sectors}" />  
</form:form>

</span>

<div class="yui3-u rowsAndButtons">
<span class="button-row">

<label for="select-prio"><button id="showPrioButton" class="button">Visa prioriteringsobjekt</button></label>
<c:if test="${loginResult && user != null and user.editor}">
  <span class="rPadding2em">
    
    <label for="edit-prio"><button class="button">Ändra</button></label>
    <form action="prio-create"> <tags:editSubmit value="Lägg till nytt" cssClass="button" /> </form>
    <label for="delete-prio"><tags:editButton value="Ta bort" cssClass="button"></tags:editButton></label>
  </span>
</c:if>

<c:if test="${su:canEdit(user, editDir) and user.approver}">
  <label for="approve-prio"><tags:editButton value="Godkänn" cssClass="button"></tags:editButton></label>
</c:if>

<form action="init-conf-columns"><input class="conf-columns button" type="submit" value="Dölj/Visa kolumner" /></form>
<button class="cost button">Kostnad</button>
<span class="export-data-buttons">
<button class="excel button">Excel</button>
<button class="pdf button">Pdf</button>
<button class="print  button">Skriv ut</button>
</span>
<button class="help button">Hjälp</button>
</span>

<c:if test="${not empty message}">
  <div style="color:red">${message}</div>
</c:if>

<form action="prio-open" method="post">

<input type="submit" id="select-prio" name="select-prio"/>
<input type="submit" id="delete-prio" name="delete-prio"/>
<input type="submit" id="edit-prio" name="edit-prio"/>
<input type="submit" id="approve-prio" name="approve-prio"/>

<div style="height:8px;"></div>

<table cellpadding="5">
  <thead class="headerRow">
    <tr>
    <th>#</th>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <th>
          <span title="${column.description}">${su:toUpperCase(column.label)}</span>
        </th>
      </c:if>
    </c:forEach>
    </tr>
  </thead>
  <tbody>
    <tr class="conditionRow">
      <td colspan="2">Filter:</td>
      <c:forEach items="${form.columns}" var="column" varStatus="vs">
        <c:if test="${column.visible and vs.index > 0}">
          <td style="center">
              ${column.columnLabel}
            <c:if test="${not empty su:toString(prioCondition[column.name])}">
              <span title='<tags:cell value="${su:toString(prioCondition[column.name])}"/>'>(*)</span>
            </c:if>
            
            <c:if test="${column.sortable}">
            <span style="font-family: courier">
              <c:choose>
                <c:when test="${column.sorting}">
                  <b style="text-decoration: blink;">&darr;</b>
                </c:when>
                <c:otherwise>
                  <a title="Sortera" href="main?sortField=${column.name}" style="text-decoration: none;">&darr;</a>
                </c:otherwise>
              </c:choose>
              </span>
            </c:if>
          </td>
        </c:if>
      </c:forEach>
    </tr>
    <c:forEach items="${rows}" var="row" varStatus="vs">
      <tr class="${vs.index % 2 == 0 ? 'even' : 'odd'}">
        <td>
          <input type="radio" name="id" value="${row.id}"${vs.index == 0 ? ' checked' : ''}/>
          <c:if test="${empty row.godkaend}">
            <div style="color: red; text-align: center;" title="Ännu ej godkänd">*</div>
          </c:if>
        </td>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <td class="${column.name}"><tags:cell value="${row[column.name]}"/>
            </td>
          </c:if>
        </c:forEach>
      </tr>
    </c:forEach>
  </tbody>
</table>

<c:if test="${empty rows}">

  <%@ include file="help.jsp" %>

</c:if>

</form>

</div>

</div>

</div>
