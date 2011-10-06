<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="column" required="true" rtexprvalue="true"
	type="se.vgregion.verticalprio.entity.Column"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${column.sortable}">
	<span> 
		<c:choose>
			<c:when test="${column.sorting}">
				<img src="img/arrow-down-active.png"
					title="Sortering är gjord med denna kolumn" />
			</c:when>
			<c:otherwise>
				<a title="Sortera" href="main?sortField=${column.name}"
					style="text-decoration: none;"><img src="img/arrow-down.png" /></a>
			</c:otherwise>
		</c:choose> 
	</span>
</c:if>