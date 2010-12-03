<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="columns" required="true" rtexprvalue="true" type="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<table>
    <tr>

<c:forEach items="${columns}" var="column" >
  
  <td>
    
    <c:forEach items="${column}" var="diagnos" >
  
    <label for="open${diagnos.id}"> 
      <c:choose>
        <c:when test="${diagnos.open}">
          <input type="radio" checked="checked" name="openedId${diagnos.id}" value="${diagnos.id}" />
          <input type="hidden" name="openedId" value="${diagnos.id}" />
          <input style="display: none" id="open${diagnos.id}" type="submit" name="closeId"
            value="${diagnos.id}" />
        </c:when>
        <c:otherwise>
          <input type="radio" />
          <input style="display: none" id="open${diagnos.id}" type="submit" name="openedId"
            value="${diagnos.id}" /> 
        </c:otherwise>
      </c:choose>
      ${diagnos.kod}
      ${diagnos.beskrivning}
    </label>
    
    
    <label for="${diagnos.id}"> 
      <c:choose>
        <c:when test="${diagnos.selected}">
          <input type="checkbox" checked="checked" name="selectedId" value="${diagnos.id}" />          
          <input style="display: none" id="${diagnos.id}" type="submit" name="deSelectId"
            value="${diagnos.id}" />
        </c:when>
        <c:otherwise>
          <input type="checkbox" />
          <input style="display: none" id="${diagnos.id}" type="submit" name="selectedId"
            value="${diagnos.id}" />
        </c:otherwise>
      </c:choose>  
    </label>
    <br/>
    </c:forEach>
    
</td>
  
</c:forEach>
    </tr>
  </table>