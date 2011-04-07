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
        <div class="cell"><tags:label key="rangordningsKod" label="Rangordning" /></div>
        <div class="cell"><tags:label key="rangordningEnligtFormel" /></div>
    </div>
    
    <div style="width:100%" class="prio-form-grid">
        <div class="cell"><tags:kod key="tillstaandetsSvaarighetsgradKod" /></div> 
        <div class="cell"><tags:kod key="aatgaerdsRiskKod" /></div>
        <div class="cell"><tags:kod key="patientnyttaEffektAatgaerdsKod" /></div>
        <div class="cell"><tags:kod key="rangordningsKod" /></div>
        <div class="cell last" id="rangordningEnligtFormel">${util:toCellText(prio.rangordningEnligtFormel)}</div>
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
        <div class="yui3-u-2-5"><tags:label key="kommentar" /><tags:textarea key="kommentar" cssInputBoxStyle="width:100%;" /></div>
    </div>
    
    <hr style="clear:both"/>
    
    <div class="yui3-u-1-2">
      <tags:code-list-view addItemLabel="Lägg till ATC-koder" removeItemLabel="Ta bort valda koder" label="ATC-koder" codeKey="atcKoder" prio="${prio}" />
    </div>
    <hr style="clear:both"/>
    <br/>
    <div>
          
    </div>
    
  </div>
  
  <script type="text/javascript">

  function computeApplyRangordningEnligtFormel() {
      try{
          computeApplyRangordningEnligtFormelImpl();
      }catch(e) {
          alert(e.message);
      }
  }
  
  function getSelectValue(id) {
      var select = document.getElementById(id);
      var index = select.selectedIndex;
      return select.options[index].value;
  }
  
  function getSelectDisplay(id, value) {
      var select = document.getElementById(id);
      if (value) {
          var opts = select.options;
          for (var i = 0; i < opts.length; i++)
              if (opts[i].value.trim() == value.trim())
                 return select.options[i].innerHTML;
      } 
      var index = select.selectedIndex;
      return select.options[index].innerHTML;
  }
  
  function computeApplyRangordningEnligtFormelImpl() {
    var tillstaandetsSvaarighetsgradKodId = getSelectValue('tillstaandetsSvaarighetsgradKodId');
    var aatgaerdsRiskKodId = getSelectValue('aatgaerdsRiskKodId');
    var patientnyttaEffektAatgaerdsKodId = getSelectValue('patientnyttaEffektAatgaerdsKodId');
    var patientnyttoEvidensKodId = getSelectValue('patientnyttoEvidensKodId');
    
    function isDigit(s) {
        var r = new RegExp('[0-9]+');
        s = s + '';
        return s.match(r);
    }
    
    if (!(isDigit(tillstaandetsSvaarighetsgradKodId) 
            && isDigit(aatgaerdsRiskKodId)
            && isDigit(patientnyttaEffektAatgaerdsKodId) 
            && isDigit(patientnyttoEvidensKodId))) {
        document.getElementById('rangordningEnligtFormel').innerHTML = '-';
    } else {
        var result = tillstaandetsSvaarighetsgradKodId - 0.6 
        + aatgaerdsRiskKodId * 0.2 
        + patientnyttaEffektAatgaerdsKodId * 0.2
        + patientnyttoEvidensKodId * 0.2;
        result=Math.round(result * 100) / 100;
        document.getElementById('rangordningEnligtFormel').innerHTML = result;
    }
    
  }  
  
  function onChange(e) {
      try{
          onChangeImpl(e);
      }catch(e) {
          alert(e.message);
      }
  }
  
  function onChangeImpl(e) {
      var target = e.target;
      var id = target.get('id');
      var tagName = target.get('tagName');
      var value = null;
      var idAtEnd = new RegExp('.+Id');
      var oldValueFlagId = (id.match(idAtEnd) ? id.substring(0, id.length - 2):id) + 'ChangeFlag';
      var changeFlag = document.getElementById(oldValueFlagId);
      var oldValueId = (id.match(idAtEnd) ? id.substring(0, id.length - 2):id) + 'ApprovedValue';
      var oldValueTag = document.getElementById(oldValueId);
      if (!oldValueTag) return;
      var oldValue = oldValueTag.innerHTML;
      
      if ('SELECT' == tagName) {
          value = getSelectValue(id);
      } else if ('TEXTAREA' == tagName) {
          value = target.get('value');
      }

      changeFlag.style.display = (value.trim() != oldValue.trim()) ? 'inline' : 'none';
  }
  
  function onCancel(e) {
      var changed = document.getElementsByName('changed');
      for (var i = 0; i < changed.length; i++) {
          if (changed[i].style.display != 'none') {
              var msg = 'Du har osparade ändringar. Vill du stänga den här vyn och förlora inmatad data?';
              if (confirm(msg)) {
                  return true;
              } else {
                  e.halt(true);
                  return false;
              }
          }
      }
  }
  
  function foo(e) {
      try {
        var s = '';
        for (var k in e) {
            s += k + ' = ' + e[k] + '\n';
            //s += ' ' + k;
        }
        alert(s);
      }catch(ee) {
          alert('error in foo\n' + ee.message);
      }
  }
  
  YUI({ filter: 'raw' }).use("node", function (Y) {
      Y.on("change", computeApplyRangordningEnligtFormel, "#tillstaandetsSvaarighetsgradKodId");
      Y.on("change", computeApplyRangordningEnligtFormel, "#aatgaerdsRiskKodId");
      Y.on("change", computeApplyRangordningEnligtFormel, "#patientnyttaEffektAatgaerdsKodId");
      Y.on("change", computeApplyRangordningEnligtFormel, "#patientnyttoEvidensKodId");
      
      Y.on("change", onChange, ".standardInput");
      
      //Y.on("change", foo, ".standardInput");
      
      //Y.on("click", onCancel, "#cancel");
  });
  
  
  
</script>
