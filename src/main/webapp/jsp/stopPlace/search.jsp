<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%-- Titre et barre de navigation --%>	
<title><s:text name="text.positionGeographique.search.title" /></title>
<s:url id="urlPositionGeographiqueRecherche" action="search" namespace="/stopPlace">
	<s:param name="idPositionGeographique" value="%{idPositionGeographique}"/>
</s:url>
<s:property value="filAriane.addElementFilAriane(getText('text.positionGeographique.search.title'), '', #urlPositionGeographiqueRecherche)"/>
<div class="panelData">
	<s:property value="filAriane.texteFilAriane" escape="false"/>
</div>
<br>
<%-- Formulaire de recherche --%>
<div class="panel">
  <s:form cssClass="panelDataInnerForm" action="searchResults" namespace="/stopPlace">
		<s:hidden name="typePositionGeographique" value="zone" />
		<s:hidden name="idPositionGeographique" value="%{idPositionGeographique}" />
		<s:hidden name="authorizedType" value="%{authorizedType}" />
		<s:hidden name="actionSuivante" value="%{actionSuivante}" />
		<s:textfield key="searchCriteria.name"/>
		<s:select emptyOption="true" key="searchCriteria.areaType" list="%{getStopAreaTypeEnum(#attr.authorizedType)}" />
		<s:textfield key="searchCriteria.countryCode" />
		<s:submit key="action.search" />
  </s:form>
</div>

<%-- Tableau résultat --%>
<div class="panel" id="displaytag"> 
	<display:table name="positionGeographiquesResultat" pagesize="15" requestURI="" id="positionGeographique" export="false">
	  	<display:column titleKey="table.title.action" sortable="false">
			<s:url id="addUrl" action="PositionGeographique_%{actionSuivante}">
				<s:param name="idChild" value="%{#attr.positionGeographique.id}" />
				<s:param name="idFather" value="%{#attr.positionGeographique.id}" />
				<s:param name="idPositionGeographique" value="%{idPositionGeographique}" />
				<s:param name="typePositionGeographique" value="zone"/>
			</s:url>
			<s:a href="%{addUrl}"><s:text name="action.select"/></s:a>&nbsp;&nbsp;
	  	</display:column>	  	
	  	<display:column titleKey="table.title.name" property="name" />
	  	<display:column titleKey="table.title.type" >
	  		<s:text name="%{#attr.positionGeographique.areaType}"/>
	  	</display:column>	
	  	<display:column titleKey="table.title.inseeCode" property="countryCode" />
	</display:table>
</div>