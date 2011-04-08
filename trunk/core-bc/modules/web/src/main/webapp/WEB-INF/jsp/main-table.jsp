<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

    <table cellpadding="5">
      <thead class="headerRow">
        <tr>
        <th>#</th>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible}">
            <th class="${column.name}">
              <span title="${column.description}">${su:toUpperCase(column.label)}</span>
              <c:if test="${column.sortable}">
                <div style="height: 100%">
                <c:choose>
                  <c:when test="${column.sorting}">
                    <img src="img/arrow-down-active.png"/>
                  </c:when>
                  <c:otherwise>
                    <a title="Sortera" href="main?sortField=${column.name}" style="text-decoration: none;"><img src="img/arrow-down.png"/></a>
                  </c:otherwise>
                </c:choose>
                </div>
              </c:if>
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