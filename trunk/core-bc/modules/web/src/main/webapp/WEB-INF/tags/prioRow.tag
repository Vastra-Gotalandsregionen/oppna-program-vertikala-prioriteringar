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
    <input title="id=${row.id}" type="radio" name="id" value="${row.id}"${vs.index == 0 ? ' checked' : ''}/>
    <c:if test="${row.godkaend == null && (row.child == null || row.child.godkaend == null)}">
      <div style="color: red; text-align: center;" title="Ännu ej godkänd">*</div>
    </c:if>
  </td>
  <c:forEach items="${form.columns}" var="column">
    <c:if test="${column.visible}">
      <td class="${column.name}"><tags:cell value="${row[column.name]}"/>
      </td>
    </c:if>
  </c:forEach>
</tr>

<c:if test="${row.child != null}">
  <tr class="${index % 2 == 0 ? 'even' : 'odd'}">
    <td>hej knekt</td>
    <c:forEach items="${form.columns}" var="column">
      <c:if test="${column.visible}">
        <td class="${column.name}">
          <hr/>
          <tags:cell value="${row.child[column.name]}"/>
        </td>
      </c:if>
    </c:forEach>
  </tr>
</c:if>


          