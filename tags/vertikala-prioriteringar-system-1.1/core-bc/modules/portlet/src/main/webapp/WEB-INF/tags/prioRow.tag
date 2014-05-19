<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="row" required="true" rtexprvalue="true" type="se.vgregion.verticalprio.entity.Prioriteringsobjekt"%>
<%@ attribute name="index" required="true" rtexprvalue="true" type="java.lang.Integer"%>
<%@ attribute name="trCssClass" required="false" rtexprvalue="true" type="java.lang.String"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<tr class="${index % 2 == 0 ? 'even' : 'odd'}">
  <td>
    <div class="padded"> 
    <input title="id=${row.id}" type="radio" name="id" value="${row.id}"${vs.index == 0 ? ' checked' : ''}/>
    <c:if test="${row.godkaend == null && (row.child == null || row.child.godkaend == null)}">
      <div style="color: red; text-align: center;" title="Ännu ej godkänd">*</div>
    </c:if>
    </div>
  </td>
  <c:forEach items="${form.columns}" var="column">
    <c:if test="${column.visible and (not column.demandsEditRights or user != null and user.editor)}">
      <td class="${column.name}">
        <div class="padded"> 
        <c:choose>
          <c:when test="${row.child == null}">
            <tags:cell value="${row[column.name]}"/>
          </c:when>
          <c:otherwise>
            <tags:cell value="${row.child[column.name]}"/>
          </c:otherwise>
        </c:choose>
        </div>
      </td>
    </c:if>
  </c:forEach>
</tr>

<c:if test="${row.child != null and su:isPriosDifferent(row, row.child)}">
  <tr class="${index % 2 == 0 ? 'even' : 'odd'}">
    <td>
      <div style="color: red; text-align: center;" title="Ännu ej godkänd">*</div>
    </td>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td class="${column.name}">
          <c:if test="${column.name != 'godkaend' and row.child[column.name] != row[column.name]}">
            <img src='${pageContext.request.contextPath}/img/flag_white.gif' name="changed" title="Värde i utkast-version." style="float:right; display:inline;"/>
            <tags:cell value="${row[column.name]}"/>
          </c:if>
        </td>
      </c:if>
    </c:forEach>
  </tr>
</c:if>


          