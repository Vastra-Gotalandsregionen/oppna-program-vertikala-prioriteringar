<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="sector" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.entity.SektorRaad"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<div class="sector-node"><label for="Sektor${sector.id}"> <c:choose>
  <c:when test="${sector.selected}">
    <input style="border:none; background-repeat:no-repeat; background-image:url('img/checked.gif'); background-repeat:none; width:15px; height:15px;" id="Sektor${sector.id}"  type="submit" src="img/checked.png" name="sectorId" value="            ${sector.id}" />
  </c:when>
  <c:otherwise>
    <input style="border:none; background-repeat:no-repeat; background-image:url('img/unchecked.gif'); width:15px; height:15px;" id="Sektor${sector.id}" type="submit" src="img/unchecked.png" name="sectorId" value="            ${sector.id}" />
  </c:otherwise>
</c:choose> ${sector.kod} </label> <c:if test="${sector.selected}">
  <tags:sectors items="${sector.children}" />
</c:if></div>