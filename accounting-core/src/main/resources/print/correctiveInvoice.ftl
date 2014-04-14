<#import "invoiceMacros.ftl" as macros />
<#import "invoicePositionList.ftl" as pl>

<#include "header.ftl">
<#include "invoiceGeneralData.ftl">

  <div id="reason-of-correction">
    Przyczyna korekty:<br/>${print.reasonOfCorrection!"(przyczyna korekty)"}
  </div>

  <div class="full-width-message">PRZED KOREKTĄ</div>

  <!-- BEGIN:Przed korekta -->
  <@pl.positionList correctedOnly=false/>
  <div id="amount">
    <table>
      <@macros.infoRow left="Do zapłaty:" right=(print.amountDue!0)?string(",##0.00") + " PLN" />
      <@macros.infoRow left="Słownie:" right=print.amountDueInWords!"zero" />
    </table>
  </div>
  <!-- END:Przed korekta -->

  <div class="full-width-message">PO KOREKCIE</div>

  <!-- BEGIN:Po korekcie -->
  <@pl.positionList correctedOnly=true/>
  <div id="amount">
    <table>
      <tr>
        <td class="left">Do&nbsp;zapłaty:</td>
        <td class="numbers"><h2>${(print.amountDueAfterCorrection!0)?string(",##0.00")}</h2></td>
        <td class="mini">PLN</td>
      </tr>
      <@macros.summaryRow title="Słownie:" value=(print.amountDueAfterCorrectionInWords!"zero") currency="PLN" />

      <@macros.summaryRow title="Zmiana ceny brutto:" value=(print.amountDueAfterCorrection!0 - print.amountDue!0)?string(",##0.00") currency="PLN" />
      <@macros.summaryRow title="Zmiana ceny netto:" value=(total["true"].value-total["false"].value)?string(",##0.00") currency="PLN" />
      <@macros.summaryRow title="Zmiana podatku:" value=(total["true"].tax-total["false"].tax)?string(",##0.00") currency="PLN" />
    </table>
  </div>
  <!-- END:Po korekcie -->

  <#if print.areCommentsVisible!true>
    <div id="comments">
      <table>
        <@macros.infoRow left="Uwagi:" right=print.comments!"" />
      </table>
    </div>
  </#if>

<#include "invoiceSignatures.ftl">
<#include "footer.ftl">
