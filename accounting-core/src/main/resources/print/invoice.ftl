<#import "invoiceMacros.ftl" as macros />
<#import "invoicePositionList.ftl" as pl>

<#include "header.ftl">
<#include "invoiceGeneralData.ftl">
  <@pl.positionList correctedOnly=false />

  <div id="amount">
    <table>
    <@macros.infoRow left="Wartość brutto razem:" right=(print.totalAmount!0)?string(",##0.00") + " PLN" />
    <@macros.infoRow left="Zapłacono:" right=(print.amountAlreadyPaid!0)?string(",##0.00") + " PLN" />

    <#assign _right><h2>${(print.amountDue!0)?string(",##0.00")} PLN</h2></#assign>
    <@macros.infoRow left="Do zapłaty:" right=_right />

    <@macros.infoRow left="Słownie:" right=print.amountDueInWords!"zero" />
    </table>
  </div>
  <#if print.areCommentsVisible!true>
    <div id="comments">
      <table>
        <@macros.infoRow left="Uwagi:" right=print.comments!"(uwagi)" />
      </table>
    </div>
  </#if>

<#include "invoiceSignatures.ftl">
<#include "footer.ftl">
