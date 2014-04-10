<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="sector" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.entity.SektorRaad"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<div class="sector-node">
  <c:choose> 
    <c:when test="${not empty sector.children}">
      <c:choose>
      <c:when test="${sector.open}">
        <input style="border:none; background-repeat:no-repeat; background-image:url('${pageContext.request.contextPath}/img/node-open.png'); background-repeat:none; width:15px; height:15px;" id="Sektor${sector.id}"  type="submit" src="${pageContext.request.contextPath}/img/node-closed.png" name="openId" value="            ${sector.id}" />
      </c:when>
      <c:otherwise>
        <input style="border:none; background-repeat:no-repeat; background-image:url('${pageContext.request.contextPath}/img/node-closed.png'); width:15px; height:15px;" id="Sektor${sector.id}" type="submit" src="${pageContext.request.contextPath}/img/unchecked.png" name="openId" value="            ${sector.id}" />
      </c:otherwise>
      </c:choose></label>
    </c:when>
    <c:otherwise>
      <input disabled="disabled" style="border:none; background-repeat:no-repeat; background-image:url('${pageContext.request.contextPath}/img/node-open-disabled.png'); width:15px; height:15px;" id="Sektor${sector.id}" type="submit" src="${pageContext.request.contextPath}/img/unchecked.png" name="openId" value="            ${sector.id}" />
    </c:otherwise>
  </c:choose>
  
<label for="Sektor${sector.id}">
  <c:choose>
  <c:when test="${sector.selected}">
    <input style="border:none; background-repeat:no-repeat; background-image:url('${pageContext.request.contextPath}/img/checked.gif'); background-repeat:none; width:15px; height:15px;" id="Sektor${sector.id}"  type="submit" src="${pageContext.request.contextPath}/img/checked.png" name="sectorId" value="            ${sector.id}" />
  </c:when>
  <c:otherwise>
    <input style="border:none; background-repeat:no-repeat; background-image:url('${pageContext.request.contextPath}/img/unchecked.gif'); width:15px; height:15px;" id="Sektor${sector.id}" type="submit" src="${pageContext.request.contextPath}/img/unchecked.png" name="sectorId" value="            ${sector.id}" />
  </c:otherwise>
</c:choose> ${sector.kod} </label> <c:if test="${sector.open}">
  <tags:sectors items="${sector.children}" />
</c:if></div>