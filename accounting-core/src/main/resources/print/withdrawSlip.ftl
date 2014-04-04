<#include "header.ftl">

  <title>KW</title>
  <link rel="stylesheet" type="text/css" href="../css/print.css" />
  <link rel="stylesheet" type="text/css" href="../css/kp.css" />
</head>
<body>
  <div id="to">
    ${print.seller.name!"(platnik)"}<br /> ${print.seller.address!"(adres platnika)"}
  </div>
  <div id="number">
    <h3>Dowód wypłaty</h3>
    <h1>KW Nr ${print.number!"(numer kw)"}</h1>
    <h3>${print.sellPlace!"(miejscowosc)"}, <#if print.sellDate??>${print.sellDate?date}<#else>(data)</#if></h3>
  </div>
  <div id="from">
    <span>Komu:</span> ${print.buyer.name!"(odbiorca)"}<br /> ${print.buyer.address!"(adres)"}<br />
  </div>
  <div id="maintable">
    <table class="paginated">
      <tr>
        <td width="450px" colspan="2" class="noborder"></td>
        <td width="60px" colspan="2" class="grey">Ma - Kasa</td>
        <td width="90px" class="grey">Winien - Konto</td>
      </tr>
      <tr>
        <td colspan="2" class="grey">Za co</td>
        <td class="grey">zł</td>
        <td class="grey">gr</td>
        <td class="grey">numer</td>
      </tr>
<#if print.elements??>
<#list print.elements as element>
      <tr class="payment-list">
        <td colspan="2">${element.serviceName}</td>
        <td>${element.price?floor?string(",##0")}</td>
        <td>${((element.price*100)%100)}</td>
        <td>${element.accountNumber}</td>
      </tr>
</#list>
</#if>
      <tr>
        <td width="350px">Słownie złotych: <b>${print.totalAmountInWords!"(kwota slownie)"}</b></td>
        <td class="grey">RAZEM:</td>
        <td><b>${(print.totalAmount!0)?floor?string(",##0")}</b></td>
        <td><b>${((print.totalAmount!0)*100%100)?string("#0")}</b></td>
        <td class="noborder"></td>
      </tr>
    </table>
    <table>
      <tr>
        <td class="grey">Wystawił</td>
        <td class="grey">Zatwierdził</td>
        <td class="grey">Rap. Kasowy</td>
        <td class="grey" colspan="2">Kwotę powyższą otrzymałem</td>
      </tr>
      <tr>
        <td rowspan="2">${print.creator!"(stworzyl)"}</td>
        <td rowspan="2">${print.approvingPerson!"(zatwierdzil)"}</td>
        <td rowspan="2">${print.cashReportReference!"(numer raportu kw)"}</td>
        <td>wypłaciłem</td>
        <td>otrzymałem</td>
      </tr>
      <tr>
        <td height="50px"></td>
        <td height="50px"></td>
      </tr>
    </table>
  </div>

<#include "footer.ftl">
