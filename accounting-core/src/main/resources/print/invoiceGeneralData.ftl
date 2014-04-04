<#import "invoiceMacros.ftl" as macros />

<title>Faktura VAT</title>
<link rel="stylesheet" type="text/css" href="../css/print.css" />
<link rel="stylesheet" type="text/css" href="../css/fv.css" />
</head>
<body>
<#if print.paymentType??>
  <#assign paymentType = print.paymentType>
<#else>
  <#assign paymentType = "TRANSFER">
</#if>
<div id="logo">
  <img src="../img/mediq.gif" />
</div>
<div id="dates">
  <table>
    <#assign _right><#if print.sellDate??>${print.sellDate?date}<#else>data</#if></#assign>
    <@macros.infoRow left="Data&nbsp;i&nbsp;miejsce&nbsp;sprzedaży:" right=_right />

    <@macros.infoRow left="Miejsce&nbsp;sprzedaży:" right=print.sellPlace!"miejsce sprzedaży" />

    <#assign _right><#if print.issuanceDate??>${print.issuanceDate?date}<#else>data</#if></#assign>
    <@macros.infoRow left="Data wystawienia:" right=_right />

    <tr><td colspan="2" style="padding: 0 !important;"><h3>${print.authenticityText!"oryginał/kopia"}</h3></td></tr>
    <tr>
      <td></td>
      <td></td>
    </tr>
  </table>
</div>
<#if print.isReceipt!false>
<h2>PARAGON&nbsp;&nbsp;${print.number!"(numer paragonu)"}</h2>
<#else>
<h2>FAKTURA VAT&nbsp;&nbsp;${print.number!"(numer faktury)"}</h2>
</#if>
<div id="contractors">
  <table id="seller">
    <@macros.infoRow left="Sprzedawca:" right=print.seller.name!"sprzedawca" />
    <@macros.infoRow left="Adres:" right=print.seller.address!"adres<br/>sprzedawcy" />
    <@macros.infoRow left="NIP:" right=print.seller.nip!"nip" />
  </table>
  <table id="buyer">
    <@macros.infoRow left="Nabywca:" right=print.buyer.name!"nabywca" />
    <@macros.infoRow left="Adres:" right=print.buyer.address!"adres<br/>nabywcy" />
    <tr>
      <td class="left"><#if print.isPerson??>${print.isPerson?string("PESEL","NIP")}:<#else>PESEL</#if></td>
      <td class="right"><#if print.isPerson??>${print.isPerson?string(print.buyer.pesel!"pesel",print.buyer.nip!"pesel")}<#else>(pesel)</#if></td>
    </tr>
  </table>
</div>

<#if ! print.isReceipt!false>
  <div id="payment-method">
    <table>
      <tr>
        <td class="left">Metoda płatności:</td>
        <td class="right"><#if paymentType == "TRANSFER">Przelew na konto<#else>Gotówka</#if></td>
        <td class="left">Termin płatności:</td>
        <td class="right"><#if print.dueDate??>${print.dueDate?date}<#else>data</#if></td>
      </tr>
      <#if paymentType == "TRANSFER">
        <tr>
          <td class="left">Numer konta:</td>
          <td class="right">${print.bankAccountNumber!"(numer konta)"}</td>
        </tr>
      </#if>
    </table>
  </div>
</#if>
