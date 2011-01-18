<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

  <div>
    Diagnos
    <tags:find-select-codes label="S�k diagnoser (kod/besk.):" codeRefName="diagnosRef" codeRef="${prio.diagnosRef}" submitName="findDiagnoses" styleClass="diagnosRef"/>
    
    <hr style="clear:both"/>
    �tg�rd
    <tags:find-select-codes label="S�k �tg�rder (kod/besk.):" codeRefName="aatgaerdRef" codeRef="${prio.aatgaerdRef}" submitName="findAatgerder" styleClass="aatgaerdRef"/>

    <hr style="clear:both"/>
    <form:hidden path="id"/>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="tillstaandetsSvaarighetsgradKod" label="tillstaandetsSvaarighetsgradKod" /></div> 
        <div class="cell"><tags:kod key="aatgaerdsRiskKod" label="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:kod key="patientnyttaEffektAatgaerdsKod" label="patientnyttaEffektAatgaerdsKod" /></div>
        <div class="cell"><tags:kod key="patientnyttoEvidensKod" label="patientnyttoEvidensKod" /></div>
        <div class="cell last"><tags:kod key="rangordningsKod" label="rangordningsKod" /></div>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="haelsonekonomiskEvidensKod" label="haelsonekonomiskEvidensKod" /></div> 
        <div class="cell"></div>
        <div class="cell"><tags:kod key="vaentetidBesookVeckor" label="vaentetidBesookVeckor" /></div>
        <div class="cell"><tags:kod key="vaentetidBehandlingVeckor" label="vaentetidBehandlingVeckor" /></div>
        <div class="cell last">
          <span class="kod-label">Sektorsr�d</span> 
          <tags:kod key="sektorRaad" label="Sektorsr�d" />
        </div>
    </div>
    
    <div style="width:100%" class="prio-form-grid last">
        <div class="cell" style="width:33%"><tags:kod key="vaardnivaaKod" label="vaardnivaaKod" /></div>

        <div class="cell last" style="width:33%">
          <tags:textarea cssInputBoxStyle="width:90%" key="kommentar" label="kommentar"/>
        </div>
    </div>
    
    <br/>Atc-kod
    <tags:find-select-codes label="S�k Atc-koder (kod/besk.):" codeRefName="atcKoderRef" codeRef="${prio.atcKoderRef}" submitName="findAtcKoder" styleClass="atcKoderRef"/>

    <hr style="clear:both"/>
    
    V�rdformer
    <tags:find-select-codes label="S�k v�rdformer (kod/besk.):" codeRefName="vaardformskoderRef" codeRef="${prio.vaardformskoderRef}" submitName="findVaardformer" styleClass="vaardformskoderRef"/>

    <hr style="clear:both"/>

  </div>
