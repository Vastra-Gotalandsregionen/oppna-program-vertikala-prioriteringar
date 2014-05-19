<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

    <table cellpadding="0" class="FloatTitle">
      <thead class="headerRow">
        <tr id="scroll_me">
        <th class="first_header">#</th>
        <c:forEach items="${form.columns}" var="column">
          <c:if test="${column.visible and (not column.demandsEditRights or user != null and user.editor)}">
            <th class="${column.name}">
              <div class="padded">
              <span>${su:toUpperCase(column.label)}</span>
              </div>
            </th>
          </c:if>
        </c:forEach>
        </tr>
        
        <tr class="conditionRow">
          <td> &nbsp; </td>
          <c:forEach items="${form.columns}" var="column" varStatus="vs">
            <c:if test="${column.visible and (not column.demandsEditRights or user != null and user.editor)}">
              <td class="enhancedToolTip">
                <div class="padded">              
                <c:if test="${not empty column.description}">
                  <span title="${column.description}">
                    <img src='img/information.png'/>
                  </span>
                </c:if>
                <c:if test="${column.filterAble}">
                  <a href='start-choosing-codes?fieldName=${column.name}'>
                  <c:choose>
                    <c:when test="${not su:isEmpty(prioCondition[column.name])}">
                      <img src='img/tratt_selected.png' width="15px" height="13px" title="Filtrering är gjord på denna kolumn"/>
                    </c:when>
                    <c:otherwise>
                      <img src='img/tratt_unselected.png' title="Filtrera"/>
                    </c:otherwise>
                  </c:choose>
                  </a>                  
                </c:if>
                
                <c:if test="${column.filterAble and not su:isEmpty(prioCondition[column.name])}">
                  <a href='deselect-codes?fieldName=${column.name}' title="Ta bort filtervillkor: <tags:cell value="${su:toString(prioCondition[column.name])}"/>" style="text-decoration: none;">
                    <img src='img/x.png'/>
                  </a>
                </c:if>
                <tags:table-sort-cell column="${column}"/>            
                </div>    
              </td>
            </c:if>
          </c:forEach>
          
        </tr>
        
      </thead>
      <tbody class="result-tbody ${fn:length(rows) > 10 ? 'boxed_' : ''}">
        <c:forEach items="${rows}" var="row" varStatus="vs">
          <tags:prioRow index="${vs.index}" row="${row}"/>
        </c:forEach>
      </tbody>
    </table>
    
    <script type="text/javascript" src="js/TableFloaterTitle.js">
    </script>
    