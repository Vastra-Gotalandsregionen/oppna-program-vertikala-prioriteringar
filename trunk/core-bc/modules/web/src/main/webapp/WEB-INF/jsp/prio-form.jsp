<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="util"%>

  <div>
    <h4>Diagnos</h4>
    <tags:find-select-codes label="Sök diagnoser<br/> (kod/besk.):" codeRefName="diagnosRef" codeRef="${prio.diagnosRef}" submitName="findDiagnoses" styleClass="diagnosRef"/>
    
    <hr style="clear:both"/>
    
    <h4>Åtgärd</h4>
    <tags:find-select-codes label="Sök åtgärder<br/> (kod/besk.):" codeRefName="aatgaerdRef" codeRef="${prio.aatgaerdRef}" submitName="findAatgerder" styleClass="aatgaerdRef"/>

    <hr style="clear:both"/>
    <form:hidden path="id"/>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:label key="tillstaandetsSvaarighetsgradKod"/></div> 
        <div class="cell"><tags:label key="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:label key="patientnyttaEffektAatgaerdsKod" /></div>
        <div class="cell"><tags:label key="patientnyttoEvidensKod" /></div>
        <div class="cell last"><tags:label key="rangordningsKod" /></div>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="tillstaandetsSvaarighetsgradKod" /></div> 
        <div class="cell"><tags:kod key="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:kod key="patientnyttaEffektAatgaerdsKod" /></div>
        <div class="cell"><tags:kod key="patientnyttoEvidensKod" /></div>
        <div class="cell last"><tags:kod key="rangordningsKod" /></div>
    </div>
    <br/><br/>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:label key="haelsonekonomiskEvidensKod" /></div> 
        <div class="cell"><tags:label key="vaardform" /></div> 
        <div class="cell"><tags:label key="vaentetidBesookVeckor" /></div>
        <div class="cell"><tags:label key="vaentetidBehandlingVeckor" /></div>
        <div class="cell last"><tags:label key="sektorRaad" /></div>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="haelsonekonomiskEvidensKod" /></div> 
        <div class="cell"><tags:kod key="vaardform" /></div>
        <div class="cell"><tags:kod key="vaentetidBesookVeckor" /></div>
        <div class="cell"><tags:kod key="vaentetidBehandlingVeckor" /></div>
        <div class="cell last"><tags:sektorRaad key="sektorRaad" label="Sektorsråd" /></div>
        
    </div>
    <br/><br/>
    
    <div style="width:100%" class="prio-form-grid last">
        <div class="cell" style="width:33%"><tags:label key="vaardnivaaKod" /></div>
        
        <div class="cell" style="width:33%">
          <tags:label key="indikationGaf"/>
        </div>

        <div class="cell last" style="width:33%">
          <tags:label key="kommentar" />
        </div>
    </div>
    
    <div style="width:100%" class="prio-form-grid last">
        <div class="cell" style="width:33%"><tags:kod key="vaardnivaaKod" /></div>
        
        <div class="cell" style="width:33%">
          <tags:textarea cssInputBoxStyle="width:90%" key="indikationGaf" />
        </div>

        <div class="cell last" style="width:33%">
          <tags:textarea cssInputBoxStyle="width:90%" key="kommentar" />
        </div>
    </div>
    <br/><br/>
    <div>
      <h4>Atc-kod</h4>
      <tags:find-select-codes label="Sök Atc-koder<br/> (kod/besk.):" codeRefName="atcKoderRef" codeRef="${prio.atcKoderRef}" submitName="findAtcKoder" styleClass="atcKoderRef"/>
    </div>
    <hr style="clear:both"/>
  </div>
