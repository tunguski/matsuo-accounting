<#macro infoRow left right>
  <tr>
    <td class="left">${left}</td>
    <td class="right">${right}</td>
  </tr>
</#macro>

<#macro summaryRow title value currency>
  <tr>
    <td class="left">${title}</td>
    <td class="numbers">${value}</td>
    <td class="mini">${currency}</td>
  </tr>
</#macro>
