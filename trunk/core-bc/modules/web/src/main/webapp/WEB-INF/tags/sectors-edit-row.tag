<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="parentDeleted" required="false" rtexprvalue="true" type="java.lang.Boolean"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:forEach items="${items}" var="item" varStatus="vs">
  <div class="sector-node">
    <span><input name="id" type="hidden" value="${item.id}"/></span>
    <span><input name="parentId" type="hidden" value="${item.parentId}"/></span>
    
    <span>
      <c:choose>
        <c:when test="${not item.locked}">
          <input name="kod" type="text" value="${item.kod}" maxlength="255" style="width: 20em"/>
        </c:when>
        <c:otherwise>
          <input name="kod" type="hidden" value="${item.kod}"/>
          ${item.kod}
        </c:otherwise>
      </c:choose>
    </span>
       
      <c:if test="${item.deleteAble and not item.locked and not parentDeleted and item.prioCount == 0}">
        <span><input name="delete" type="submit" value="       ${item.id}" class="sectorDelete"/></span>
        <c:if test="${item.markedAsDeleted}"> <img src="img/flag_red.gif" alt="Raderas vid sparande" /> </c:if>
      </c:if>
      <span>
        <input name="markedAsDeleted" type="hidden" value="${item.markedAsDeleted}"/>
        <input name="locked" type="hidden" value="${item.locked}"/>
        <input name="prioCount" type="hidden" value="${item.prioCount}"/>
      </span>
      
      <c:if test="${item.prioCount > 0}">
        <span><img src="img/icon_alert.gif"  title="Kan ej raderas, har ${item.prioCount} prioriteringsobjekt." /> </span>
      </c:if>
      
      <c:choose>
        <c:when test="${not item.locked and not item.markedAsDeleted}">
          <span><input name="insert" type="submit" value="       ${item.id}" class="sectorAdd"/></span>
        </c:when>
        <c:when test="${item.locked and not item.markedAsDeleted}">
          <img src="img/icon_padlock.gif" alt="Låst för din användare" />
        </c:when>
      </c:choose>
      
    
    <tags:sectors-edit-row items="${item.beanChildren}" parentDeleted="${item.markedAsDeleted}" />
  </div>
</c:forEach>