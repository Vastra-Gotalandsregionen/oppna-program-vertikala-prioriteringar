<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>

  <div>

  <span class="yui3-g">
    <span class="yui3-u-1">
      <tags:code-list-view addItemLabel="Lägg till diagnoser" removeItemLabel="Ta bort valda diagnoser" label="Diagnoser" codeKey="diagnoser" prio="${prio}" />
    </span>
    <hr/>
    <span class="yui3-u-3-4">
      <tags:code-list-view addItemLabel="Lägg till åtgärder" removeItemLabel="Ta bort valda åtgärder" label="Åtgärder" codeKey="aatgaerdskoder" prio="${prio}" />
    </span>
    <span class="yui3-u-1-4">
      <tags:label key="indikationGaf" />
      <tags:textarea key="indikationGaf" />
    </span>
  </span>  

    <hr style="clear:both"/>
    <form:hidden path="id"/>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:label key="tillstaandetsSvaarighetsgradKod"/></div> 
        <div class="cell"><tags:label key="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:label key="patientnyttaEffektAatgaerdsKod" /></div>
        <div class="cell"><span class="kod-label rangordningsKod-label"> Rangordning </span></div>
        <c:if test="${util:canEdit(user, editDir)}">
          <div class="cell last kod-label">Rangordning enligt formel</div>
        </c:if>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="tillstaandetsSvaarighetsgradKod" /></div> 
        <div class="cell"><tags:kod key="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:kod key="patientnyttaEffektAatgaerdsKod" /></div>
        
        <div class="cell last"><tags:kod key="rangordningsKod" /></div>
    </div>
    <br/><br/>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:label key="haelsonekonomiskEvidensKod" /></div> 
        <div class="cell"><tags:label key="kostnadLevnadsaarKod" /></div> 
        <div class="cell"><tags:label key="patientnyttoEvidensKod" /></div>

        <div class="cell"><tags:label key="vaentetidBesookVeckor" /></div>
        <div class="cell"><tags:label key="vaentetidBehandlingVeckor" /></div>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="haelsonekonomiskEvidensKod" /></div> 
        <div class="cell"><tags:kod key="kostnadLevnadsaarKod" /></div> 
        <div class="cell"><tags:kod key="patientnyttoEvidensKod" /></div>

        <div class="cell"><tags:kod key="vaentetidBesookVeckor" /></div>
        <div class="cell"><tags:kod key="vaentetidBehandlingVeckor" /></div>        
    </div>
    <br/><br/>
    
    <div style="width:100%" class="prio-form-grid yui3-g">
        <div class="yui3-u-1-5"><tags:label key="vaardnivaaKod" /><tags:kod key="vaardnivaaKod" /></div>
        <div class="yui3-u-1-5"><tags:label key="vaardform" /><tags:kod key="vaardform" /></div>
        <div class="yui3-u-1-5"><tags:label key="sektorRaad"/><tags:sektorRaad key="sektorRaad"/></div>
        <div class="yui3-u-1-4"><tags:label key="kommentar" /><tags:textarea key="kommentar" cssInputBoxStyle="width:100%;" /></div>
        <div class="yui3-u-1-2">
          <tags:code-list-view addItemLabel="Lägg till ATC-koder" removeItemLabel="Ta bort valda koder" label="ATC-koder" codeKey="atcKoder" prio="${prio}" />
        </div>
    </div>
    <hr style="clear:both"/>
    <br/>
    <div>
          
    </div>
    
  </div>
