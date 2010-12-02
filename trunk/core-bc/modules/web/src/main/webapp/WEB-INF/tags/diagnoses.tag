<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:forEach items="${items}" var="diagnos" >
  <div class="diagnos-node">
  
    <label for="${diagnos.id}"> 
      <c:choose>
        <c:when test="${diagnos.open}">
          <input type="radio" checked="checked" />
          <input style="display: none" id="${diagnos.id}" type="submit" name="closeId"
            value="${diagnos.id}" />
        </c:when>
        <c:otherwise>
          <input type="radio" />
          <input style="display: none" id="${diagnos.id}" type="submit" name="openId"
            value="${diagnos.id}" /> 
        </c:otherwise>
      </c:choose>
      ${diagnos.kod}  
    </label>
    
    
    <label for="${diagnos.id}"> 
      <c:choose>
        <c:when test="${diagnos.selected}">
          <input type="checkbox" checked="checked" />
          <input style="display: none" id="${diagnos.id}" type="submit" name="deSelectId"
            value="${diagnos.id}" />
        </c:when>
        <c:otherwise>
          <input type="checkbox" />
          <input style="display: none" id="${diagnos.id}" type="submit" name="selectId"
            value="${diagnos.id}" />
        </c:otherwise>
      </c:choose>  
    </label>
    
  </div>
  
</c:forEach>