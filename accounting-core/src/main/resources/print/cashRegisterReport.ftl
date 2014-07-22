<#include "header.ftl">

<title>Raport kasowy</title>
<link rel="stylesheet" type="text/css" href="../css/print.css"/>
<link rel="stylesheet" type="text/css" href="../css/cashRegister.css"/>
</head>
<body>
<#include "clinicInfo.ftl">

<div class="name"><h1>Raport kasowy dla ${cashRegisterReport.cashRegister.code}</h1></div>
<div class="number">${cashRegisterReport.cashRegister.createdTime?date}</div>
<div class="number">Rapot dzienny numer: ${reportNumber?int}</div>

<div>
  <table class="table">
    <thead>
    <tr>
      <th class="lp">Lp</th>
      <th class="type">Typ</th>
      <th class="create-time">Utworzono</th>
      <th class="buyer">Opłacił</th>
      <th class="serive">Usługi</th>
      <th class="value">Wartość</th>
    </tr>
    </thead>
    <tbody>
    <#list cashRegisterReport.prints as print>
      <#assign printFacade = prints.lookup(print.id)>
    <tr>
      <td>${(print_index+1)?string(",##0")}</td>
      <td>${messages.translate("cashDocument.shortName." + print.printClass.getSimpleName())}</td>
      <td>${print.createdTime?date}</td>
      <td>${printFacade.buyer.name}</td>
      <td>
        <#list printFacade.elements as printPosition>
		  ${printPosition.serviceName}
        </#list>
      </td>
      <td>${print.cashRegisterAmount} zł</td>
    </tr>
    </#list>
    </tbody>

  </table>
</div>

<div class="resume">
  <div class="table-name">
    <h2>Podsumowanie</h2>
  </div>

  <table class="table width_2_5">
    <tbody>
    <tr>
      <td>Liczba pozycji:</td>
      <td class="data-resume-table numbers">${summary.resume}</td>
      <td></td>
    </tr>
    <tr>
      <td>Wartość początkowa:</td>
      <td class="data-resume-table numbers">${cashRegisterReport.startingBalance} </td>
      <td> zł</td>
    </tr>
    <tr>
      <td>Wartość końcowa:</td>
      <td class="data-resume-table numbers">${cashRegisterReport.endingBalance} </td>
      <td>zł</td>
    </tr>
    </tbody>
  </table>

  <div class="table-name">
    <h2>Sumy wg typu dokumentu</h2>
  </div>

  <table class="table width_2_5">
    <tbody>
<#list summary.printTypeSummary?keys as printTypeKey>
      <tr>
        <td>${messages.translate("cashDocument.name." + printTypeKey)}:</td>
        <td class="data-resume-table numbers">${summary.printTypeSummary[printTypeKey]} </td>
        <td>zł</td>
      </tr>
</#list>
    </tbody>
  </table>
</div>

<#include "footer.ftl">