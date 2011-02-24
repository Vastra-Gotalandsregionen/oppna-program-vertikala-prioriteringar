<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.lang.Object"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

${su:toCellText(value)}
<%-- 
<c:choose>
  <c:when test="${su:isCollection(value)}">
    
      <c:forEach items="${value}" var="cellContent">
        <div>${cellContent}</div>
      </c:forEach>
    
  </c:when>
 
  <c:otherwise>
     <c:out value="${value}" />
  </c:otherwise>
  
</c:choose>
--%>