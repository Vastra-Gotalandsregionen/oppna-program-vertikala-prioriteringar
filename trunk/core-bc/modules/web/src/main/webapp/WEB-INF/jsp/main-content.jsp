<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<div class="main-content">
	<form action="main" method="post">
    
   		<c:if test="${not empty rows}">
      		<div class="button-row">
        
          		<input type="submit" id="select-prio" name="select-prio" value="Visa prioriteringsobjekt" class="button"/>
        
        		<c:if test="${loginResult && user != null and user.editor}">
            		<span class="rPadding2em">
						<tags:editSubmit name="edit-prio" value="Ändra" cssClass="button"/>
						<tags:editSubmit name="prio-create" value="Lägg till nytt" cssClass="button" />
						<tags:editSubmit name="delete-prio" value="Ta bort" cssClass="button"/>
					</span>
				</c:if>
      
        		<c:if test="${su:canEdit(user, editDir) and user.approver}">
            		<tags:editSubmit name="approve-prio" value="Godkänn" cssClass="button"/>
       			</c:if>
          		<input name="init-conf-columns" class="conf-columns button" type="submit" value="Dölj/Visa kolumner" />
      		</div>
		</c:if>
    
		<tags:message-out/>
    
    <c:if test="${not empty rows}">
    <table cellpadding="5">
      <thead class="headerRow">
        <tr>
        <th>#</th>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <th class="${column.name}">
              <c:if test="${column.sortable}">
                <c:choose>
                  <c:when test="${column.sorting}">
                    <b style="text-decoration: blink; float: right;"><img src="img/arrow-down-active.png"/></b>
                  </c:when>
                  <c:otherwise>
                    <a title="Sortera" href="main?sortField=${column.name}" style="text-decoration: none; float: right;"><img src="img/arrow-down.png"/></a>
                  </c:otherwise>
                </c:choose>
              </c:if>
              <span title="${column.description}">${su:toUpperCase(column.label)}</span>
            </th>
          </c:if>
        </c:forEach>
        </tr>
        
        <tr class="conditionRow">
          <td colspan="2">Filter:</td>
          <c:forEach items="${form.columns}" var="column" varStatus="vs">
            <c:if test="${column.visible and vs.index > 0}">
              <td style="center">
                <c:if test="${column.filterAble}">
                  <a href='start-choosing-codes?fieldName=${column.name}'>
                  <c:choose>
                    <c:when test="${not su:isEmpty(prioCondition[column.name])}">
                      <img src='img/tratt_selected.png' width="15px" height="13px"/>
                    </c:when>
                    <c:otherwise>
                      <img src='img/tratt_unselected.png'/>
                    </c:otherwise>
                  </c:choose>
                  </a>
                  
                </c:if>
                
                <c:if test="${column.filterAble and not su:isEmpty(prioCondition[column.name])}">
                  <a href='deselect-codes?fieldName=${column.name}' title="Ta bort filtervillkor: <tags:cell value="${su:toString(prioCondition[column.name])}"/>" style="text-decoration: none;">
                    <img src='img/x.png'/>
                  </a>
                </c:if>
              </td>
            </c:if>
          </c:forEach>
        </tr>
        
      </thead>
      <tbody class="${fn:length(rows) > 10 ? 'boxed' : ''}">
        <c:forEach items="${rows}" var="row" varStatus="vs">
          <tags:prioRow index="${vs.index}" row="${row}"/>
        </c:forEach>
      </tbody>
    </table>
    </c:if>
    
  </form>
  <c:if test="${empty rows}">
    <%@ include file="help.jsp" %>
   </c:if>
              
</div>