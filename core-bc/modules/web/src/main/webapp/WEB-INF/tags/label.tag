<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="key" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="label" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/vgr-util.tld" prefix="su"%>

<span class="kod-label ${key}-label" title="${prio.child != null ? su:mkChangedToolTip(prio.child[key], prio[key]) : ''}">
  ${label == null ? prio.columns[key].label : label} 
  <img src="img/flag_white.gif" class="changedFlag" name="ChangeFlag" title="${prio.child != null ? su:mkChangedToolTip(prio.child[key], null) : ''}" id="${key}ChangeFlag" style="display:${(prio.child != null and prio[key] != prio.child[key])? 'inline':'none'}"/>
  <img src="img/changed.png" class="unsavedChange" name="EditedFlag" title="Ej sparad. ${prio.child != null ? su:mkChangedToolTip(prio.unalteredVersion[key], null) : ''}" id="${key}EditedFlag" style="display:${(prio.unalteredVersion != null and prio[key] != prio.unalteredVersion[key])? 'inline':'none'}"/>
</span> 
