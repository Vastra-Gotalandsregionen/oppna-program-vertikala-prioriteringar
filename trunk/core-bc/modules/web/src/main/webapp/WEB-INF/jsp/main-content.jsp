<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<div class="main-content">
    <form action="main" method="post">
    
      <div class="button-row">
        <c:if test="${not empty rows}">
          <input type="submit" id="select-prio" name="select-prio" value="Visa prioriteringsobjekt" class="button"/>
        </c:if>
    
        <c:if test="${loginResult && user != null and user.editor}">
            <span class="rPadding2em">
              <c:if test="${not empty rows}">
                  <tags:editSubmit name="edit-prio" value="Ändra" cssClass="button"/>
              </c:if>
              <tags:editSubmit name="prio-create" value="Lägg till nytt" cssClass="button" />
              <c:if test="${not empty rows}">
                  <tags:editSubmit name="delete-prio" value="Ta bort" cssClass="button"/>
              </c:if>
            </span>
        </c:if>
      
        <c:if test="${su:canEdit(user, editDir) and user.approver and not empty rows}">
            <tags:editSubmit name="approve-prio" value="Godkänn" cssClass="button"/>
        </c:if>
      
        <input name="init-conf-columns" class="conf-columns button" type="submit" value="Dölj/Visa kolumner" />
      </div>
    
      <c:if test="${not empty form and not empty form.message}">
        <div style="color:red">${form.message}</div>
        <jsp:setProperty property="message" name="form" value=""/>
      </c:if>
    
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
                <c:if test="${column.filterAble}">
                  <a href='start-choosing-codes?fieldName=${column.name}'>
                  <c:choose>
                    <c:when test="${not empty su:toString(prioCondition[column.name])}">
                      <img src='img/tratt_selected.png'/>
                    </c:when>
                    <c:otherwise>
                      <img src='img/tratt_unselected.png'/>
                    </c:otherwise>
                  </c:choose>
                  </a>
                  
                </c:if>
                <c:if test="${not empty su:toString(prioCondition[column.name])}">
                  <span title='<tags:cell value="${su:toString(prioCondition[column.name])}"/>'>(*)</span>
                  <a href='deselect-codes?fieldName=${column.name}' title="Ta bort filtervillkor">X</a>
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
    
  </form>            
</div>