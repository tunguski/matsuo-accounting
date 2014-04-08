<%@page import="pl.matsuo.accounting.model.print.WithdrawSlip"%>
<%@page import="pl.matsuo.core.web.view.BootstrapRenderer"%>
<%!  BootstrapRenderer.BootstrapRenderingBuilder renderer =
             BootstrapRenderer.renderer().create(WithdrawSlip.class).entityName("entity.fields"); %>


<div ng-controller="WithdrawSlipController">
  <div class="row">
    <div class="span6"><%= renderer.render("issuanceDate", "sellPlace")%></div>
    <div class="span5"><%= renderer.render("sellDate", "number") %></div>
  </div>

  <div class="row">
    <div class="span12">
      <h4>Za co</h4>

      <table class="table table-condensed table-hover table-striped input-table">
        <thead>
        <tr>
          <th class="number">Nr</th>
          <th style="width: 50%;">Nazwa</th>
          <th class="number">Ma - Kasa</th>
          <th class="number">Winien - Konto</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="position in entity.elements">
          <td class="number">{{$index + 1}}.</td>
          <td><input type="text" ng-model="position.fields.serviceName"></td>
          <td class="number"><input type="text" ng-change="recalculateSummaries()" ng-model="position.fields.price"/></td>
          <td class="number"><input type="text" ng-model="position.fields.accountNumber"/></td>
          <td><a class="icon-remove" ng-click="remove(position)"></a></td>
        </tr>

        <tr>
          <td colspan="1"></td>
          <td class="number">Razem:</td>
          <td class="number">{{ summary | number:2 }}</td>
          <td colspan="2"></td>
        </tr>
        </tbody>
      </table>

      <h4><a class="icon-plus-sign" ng-click="addWithdrawSlipPosition()"></a></h4>
    </div>
  </div>
</div>
