<%@ taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/commun/jscalendar.jsp"></s:include>
<%-- Titre et barre de navigation --%>	
<title><s:text name="text.validation.list.title" /></title>
<s:url id="urlValidations" action="Validation_execute" includeParams="none"/>
<s:property value="filAriane.addElementFilAriane(getText('text.validation.list.title'), '', #urlValidations)"/>
<div class="panelData">
  <s:property value="filAriane.texteFilAriane" escape="false"/>
</div>
<br>
<%-- Import files --%>
<div>
  <FIELDSET style="width: 500px;">
    <LEGEND><b><s:text name="title.validate.data"/></b></LEGEND>
    <s:form id="validationForm" action="Validation_valider" enctype="multipart/form-data" method="POST">
      <s:submit value="%{getText('action.validate')}" formId="validationForm"/>
    </s:form>
  </FIELDSET>
  <br><br>
  <FIELDSET style="width: 500px;">
    <LEGEND><b><s:text name="title.shift.vehicleJourneyAtStop"/></b></LEGEND>
    <s:form validate="true" id="decalageForm" action="Validation_decaler" enctype="multipart/form-data" method="POST">
      <s:textfield maxlength="5" id="decalage" name="decalage" key="decalage" required="true"/>
      <s:submit value="%{getText('action.shift')}" formId="decalageForm"/>
    </s:form>
  </FIELDSET>
  <s:if test="useGeometry == 'true'">
    <br><br>
    <FIELDSET style="width: 500px;">
      <LEGEND><b><s:text name="title.calculate.coordinates"/></b></LEGEND>
      <s:form id="barycentreForm" action="Validation_barycentre" enctype="multipart/form-data" method="POST">
        <s:submit value="%{getText('action.calculate.barycentre')}" formId="barycentreForm"/>
      </s:form>
    </FIELDSET>
    <br><br>
    <FIELDSET style="width: 500px;">
      <LEGEND><b><s:text name="title.convert"/></b></LEGEND>
      <s:form id="conversionForm" action="Validation_convertir" enctype="multipart/form-data" method="POST">
        <s:submit value="%{getText('action.convert')}" formId="convertionForm"/>
      </s:form>
    </FIELDSET>
  </s:if>
  <br><br>
  <FIELDSET style="width: 500px;">
    <LEGEND><b><s:text name="title.purge"/></b></LEGEND>
    <s:form validate="true" id="purgeForm" action="Validation_purger" enctype="multipart/form-data" method="POST">
      <s:textfield maxlength="10" id="purge" name="purge" key="purge" required="true"/>
      <script type="text/javascript">
        <!--//
        Calendar.setup({
          singleClick : true,
          firstDay : 1,
          inputField : "purge",	// ID of the input field
          ifFormat : "%Y-%m-%d"	// the date format
        }
      );
        //-->
      </script>
      <s:submit value="%{getText('action.purge')}" formId="purgeForm"/>
    </s:form>
  </FIELDSET>
</div>	
