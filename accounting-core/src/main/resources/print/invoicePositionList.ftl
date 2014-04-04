<#macro positionList correctedOnly>
  <div id="product-list">
    <table class="paginated">
      <tr>
        <th width="20px">Lp</th>
        <th>Nazwa usługi</th>
        <th width="50px">Ilość</th>
        <th width="70px">Cena netto</th>
        <th width="50px">Stawka VAT</th>
        <th width="80px">Wartość netto</th>
        <th width="70px">Wartość VAT</th>
        <th width="70px">Wartość brutto</th>
      </tr>
      <#list print.elements as element>
        <#if (element.isAfterCorrection!false) == correctedOnly>
          <tr>
            <td>${(element_index+1)?string(",##0")}</td>
            <td>${element.serviceName}</td>
            <td class="numbers">${element.count?string(",##0")}</td>
            <td class="numbers">${element.price?string(",##0.00")}</td> <#if
          element.taxRate == "zw">
            <td class="numbers">zw</td> <#assign taxRate = "0"> <#else>
            <td class="numbers">${element.taxRate}%</td> <#assign taxRate =
            element.taxRate> </#if>
            <td class="numbers">${(element.count*element.price)?string(",##0.00")}</td>
            <td class="numbers">${(element.count*element.price*taxRate?number/100)?string(",##0.00")}</td>
            <td class="numbers">${(element.count*element.price*(taxRate?number/100+1))?string(",##0.00")}</td>
          </tr>
        </#if>
      </#list>
      <tr id="sumup">
        <td colspan="4" class="left">RAZEM:&nbsp;</td>
        <td>-</td>
        <td class="numbers">${total.value?string(",##0.00")}</td>
        <td class="numbers">${total.tax?string(",##0.00")}</td>
        <td class="numbers">${total.sum?string(",##0.00")}</td>
      </tr>
      <#assign taxRateListElements = taxRateList?values>
      <#list taxRateList?keys as key>
        <tr class="whitebg">
          <td colspan="4" class="left noborder"></td> <#if key == "zw">
          <td class="numbers">zw</td> <#else>
          <td class="numbers">${key}%</td> </#if>
          <td class="numbers">${taxRateListElements[key_index].value?string(",##0.00")}</td>
          <td class="numbers">${taxRateListElements[key_index].tax?string(",##0.00")}</td>
          <td class="numbers">${taxRateListElements[key_index].sum?string(",##0.00")}</td>
        </tr>
      </#list>
    </table>
  </div>
</#macro>
