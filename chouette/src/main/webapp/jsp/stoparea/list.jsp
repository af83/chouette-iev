<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="/jsp/commun/scriptaculous.jsp" />

<%-- Titre et barre de navigation --%>
<s:url id="urlPositionGeographiques" action="list" namespace="/stoparea" includeParams="none">
  <s:param name="typePositionGeographique" value="%{typePositionGeographique}"/>
</s:url>
<title><s:text name="text.zone.list.title" /></title>
<s:property value="filAriane.addElementFilAriane(getText('text.zone.list.title'), '', #urlPositionGeographiques)"/>
<div class="panelData">
  <s:property value="filAriane.texteFilAriane" escape="false"/>
</div>

<br>

<%-- Filtre --%>
<div>
  <s:form action="list" namespace="/stoparea">
    <s:hidden name="typePositionGeographique" value="%{typePositionGeographique}" />
    <s:select name="listCriteria.idReseau" label="%{getText('filtre.select.reseau')}" value="%{idReseau}" list="reseaux" listKey="id" listValue="name" headerKey="" headerValue="%{getText('filtre.reseau.dropDownListItem.tous')}" />
    <s:textfield name="listCriteria.nomArret" label="%{getText('filtre.select.nomArret')}"></s:textfield>
    <s:textfield name="listCriteria.codeInsee" label="%{getText('filtre.select.codeInsee')}"></s:textfield>
    <s:submit value="%{getText('action.filtrer')}"/>
  </s:form>
</div>

<br>

<%-- Actions --%>
<div class="actions">
  <s:url action="add" namespace="/stoparea" id="editPositionGeographique">
    <s:param name="typePositionGeographique" value="%{typePositionGeographique}" />
  </s:url>
  <s:a href="%{editPositionGeographique}"><b><s:text name="text.zone.create.button"/></b></s:a>
</div>

<br>

<%-- Tableau --%>
<div id="displaytag"> 
  <display:table name="positionGeographiques" pagesize="20" requestURI="" id="positionGeographique" export="false">
    <display:column title="Action" sortable="false">
      <%-- BOUTON EDITER --%>
      <s:url id="editUrl" action="edit" namespace="/stoparea">
        <s:param name="idPositionGeographique">${positionGeographique.id}</s:param>
      </s:url>
      <s:a href="%{editUrl}">
        <img border="0" alt="Edit" src="/images/editer.png" title="<s:text name="tooltip.edit"/>">
      </s:a>&nbsp;&nbsp;
      <%-- BOUTON SUPPRIMER --%>
      <s:url id="deletePositionGeographique" action="delete" namespace="/stoparea">
        <s:param name="idPositionGeographique">${positionGeographique.id}</s:param>
        <s:hidden name="operationMode" value="STORE" />
      </s:url>
      <s:a href="%{deletePositionGeographique}" onclick="return confirm('%{getText('zone.delete.confirmation')}');">
        <img border="0" alt="Delete" src="/images/supprimer.png" title="<s:text name="tooltip.delete"/>">
      </s:a>
    </display:column>
    <display:column title="Nom" property="name" />
    <display:column title="Identifiant de l'objet" property="objectId" />
    <display:column title="Type" >
      <s:text name="%{#attr.positionGeographique.areaType}"/>
    </display:column>
  </display:table>
</div>

<script type="text/javascript"><!--
  // <![CDATA[

  var arretsPhysiques = <%=request.getAttribute("jsonArrets")%>;
	
  function autocompletion() {
    new Autocompleter.Local('nomArretDestination', 'listeArrets', Object.keys(arretsPhysiques), {});
    $('nomArretDestination').focus();
  }
	
  Event.observe(window, 'load', autocompletion);
	
  var TridentAutoComplete = {
    beforeSubmit : function() {
      var value = arretsPhysiques[$('nomArretDestination').value];
      if (value == null) $('idArretDestination').value="";
      else $('idArretDestination').value = value;
      return true;
    }
  };
	
  // ]]>
  --></script>