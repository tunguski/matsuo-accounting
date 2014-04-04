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
        <td class="left">Kwota (brutto) do zapłaty przez nabywcę:</td>
        <td class="right">${(print.amountDueAfterCorrection!0 - print.amountDue!0)?string(",##0.00")}</td>
        <td class="middle"></td>
        <td class="left" rowspan="2">Do&nbsp;zapłaty:</td>
        <td class="right" rowspan="2"><h2>${(print.amountDueAfterCorrection!0)?string(",##0.00")} PLN</h2></td>
      </tr>
      <tr>
        <td class="left">Kwota&nbsp;zmniejszenia&nbsp;ceny&nbsp;bez&nbsp;podatku&nbsp;(netto):</td>
        <td class="right">${(total.value-totalAfterCorrection.value)?string(",##0.00")}</td>
        <td class="middle"></td>
      </tr>
      <tr>
        <td class="left">Kwota zmniejszenia podatku należnego:</td>
        <td class="right">${(total.tax-totalAfterCorrection.tax)?string(",##0.00")}</td>
        <td class="middle"></td>
        <td class="left">Słownie:</td>
        <td class="right">${print.amountDueAfterCorrectionInWords!"zero"}</td>
      </tr>
      <@macros.infoRow left="Kwota (brutto) do zwrotu nabywcy:" right=(totalAfterCorrection.sum-total.sum)?string(",##0.00") />
      <@macros.infoRow left="Kwota zwiększenia ceny bez podatku (netto):" right=(totalAfterCorrection.value-total.value)?string(",##0.00") />
      <@macros.infoRow left="Kwota zwiększenia podatku należnego:" right=(totalAfterCorrection.tax-total.tax)?string(",##0.00") />
    </table>
  </div>
  <!-- END:Po korekcie -->

  <#if print.areCommentsVisible!true>
    <div id="comments">
      <table>
        <@macros.infoRow left="Uwagi:" right=print.comments!"(uwagi)" />
      </table>
    </div>
  </#if>

<#include "invoiceSignatures.ftl">
<#include "footer.ftl">
