<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:actionURL var="actionUrl">
    <portlet:param name="action" value="doSectorAction"/>
</portlet:actionURL>

<div>
<%@ include file="head.jsp" %>
<div style="height:100%; ">

<div class="main-content edit-sectors">

    <div class="window prio-view view_details_color ">
      <div class="values">

  <tags:message-out />

  <form action="${actionUrl}" method="post" accept-charset="UTF-8" enctype="application/x-www-form-urlencoded" class="yui3-g otherUser">

  <div class="yui3-u-1">
    <h2>Administrera sektorsråd</h2>
    <hr noshade="noshade"/>
    <br/>
  </div>
  
  <div class="yui3-u-1-2 otherUser">
    <c:if test="${user.userEditor}">
      Skapa ny <input type="submit" name="insert-sector" value="       -1" class="sectorAdd" />
    </c:if>
    <tags:sectors-edit-row items="${sectors}" />
  </div>

<div class="yui3-u-1-2 otherUser">
  <ol>
    <li>
      Här kan du ändra på befintliga sektorsråd samt lägga till och ta bort. Ändringarna sparas när du trycker på knappen <input type="submit" name="save" value="Spara ändringar" onclick="return confirm('Ändringarna kommer att sparas, är du säker?')" />
      Efter du har sparat återvänder systemet till huvudsidan.
    </li>
    <li>
     För att ångra och hoppa tillbaka till huvudsidan tryck på <input type="submit" name="toMain" value="Tillbaka/Avbryt" />
    </li>
    <li> 
      <input type="button" class="sectorAdd"/> Lägg till en sektor under en specifik 'föräldrasektor', eller 'högst upp' i trädstrukturen.
    </li> 
    <li> 
      <input type="button" class="sectorDelete"/> Ta bort en sektor. En nod som finns sparad i databasen sedan tidigare får en markering 
      (<img alt="Raderas vid sparande" src="${pageContext.request.contextPath}/img/flag_red.gif">) efter att du har tryckt på knappen.
      Denna kommer att raderas när du väl väljer att spara. Om sektorsrådet har lagts till nyligen, innan du sparat förändringarna, tas den bort med en gång istället.
    <li/>
    <li> 
      <img src="img/icon_padlock.gif"> Den här ikonen markerar ett sektorsråd som du inte har tilltåtelse att ändra. Generellt sett (om du inte är superadministratör)
      får du ändra på de råd som du själv har tillåtelse att godkänna.
    </li>
    <li>
      <img src="${pageContext.request.contextPath}/img/icon_alert.gif" /> Markerar ifall sektorsrådet inte kan tas bort pga att den har tillhörande prioriteringsobjekt. För muspekaren över ikonen för att få reda på hur många poster det rör sig om.
      <br/> Om du vill radera posten: Hoppa till huvudsidan och sök upp prioriteringsobjekten och ändra/radera dessa och hoppa tillbaka hit igen och slutför raderingen.
    </li>
  </ol>
</div>


  <div class="yui3-u-1">
    <br/>
    <hr noshade="noshade"/>
    <br/>
  </div>

<div class="yui3-u-1-2 center"><input type="submit" name="save" value="Spara ändringar" onclick="return confirm('Ändringarna kommer att sparas, är du säker?')" /></div>

<div class="yui3-u-1-2 center"><input type="submit" name="toMain" value="Tillbaka/Avbryt" />  </div>
    
  
  </form>
  
  </div>
  </div>
  
  </div>

</div>
</div>