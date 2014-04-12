<%@page import="pl.matsuo.accounting.model.print.Invoice"%>
<%@page import="pl.matsuo.core.web.view.BootstrapRenderer"%>
<%!  BootstrapRenderer renderer = BootstrapRenderer.renderer(); %>

<div ng-controller="CashDocumentController">

  <form name="form" class="form-horizontal">
    <div class="row"><div class="span12"><h4>Podstawowe dane</h4></div></div>

    <div class="row">
      <div class="span6"><%= renderer.create(Invoice.class).attribute("ng-disabled", "true").renderWithName("seller.id", "idSeller") %></div>
      <div class="span3">
        <table class="table table-condensed details-table">
          <tr><td>Nazwa</td><td name="seller_name">{{idSeller.value.firstName ? idSeller.value.firstName + ' ' + idSeller.value.lastName : idSeller.value.fullName}}</td></tr>
          <tr><td>Adres</td><td ng-bind-html="idSeller.value.address | addressPresenter"></td></tr>
        </table>
      </div>

      <div class="span3">
        <table class="table table-condensed details-table">
          <tr><td>NIP</td><td>{{idSeller.value.nip}}</td></tr>
          <tr><td>Regon</td><td>{{idSeller.value.regon}}</td></tr>
        </table>
      </div>
    </div>

    <div class="row"><div class="span12"><hr/></div></div>

    <div class="row">
      <div class="span6"><%= renderer.create(Invoice.class).renderWithName("buyer.id", "idBuyer") %></div>
      <div class="span3">
        <table class="table table-condensed details-table">
          <tr><td>Nazwa</td><td>{{idBuyer.value.firstName ? idBuyer.value.firstName + ' ' + idBuyer.value.lastName : idBuyer.value.fullName}}</td></tr>
          <tr><td>Adres</td><td ng-bind-html="idBuyer.value.address | addressPresenter"></td></tr>
        </table>
      </div>
    </div>
    <div class="row">
      <div class="span6"></div>
      <div class="span3" ng-show="idSeller.value.nip">
        <table class="table table-condensed details-table">
          <tr ng-show="buyer.nip"><td>NIP</td><td>{{idBuyer.value.nip}}</td></tr>
          <tr ng-show="buyer.regon"><td>Regon</td><td>{{idBuyer.value.regon}}</td></tr>
        </table>
      </div>
    </div>

    <div class="row"><div class="span12"><hr/></div></div>

    <div ng-include="cashDocumentBodyUrl"></div>
  </form>


  <div class="form-actions">
    <a class="btn btn-primary" ng-click="save()" translate="save"></a>
    <a class="btn" href="javascript:;" onclick="window.history.back()" translate="cancel"></a>

    <div download-button="entity" />

    <a ng-if="entity.id" class="btn btn-info" ng-href="#/prints/cashDocument?idCorrectedPrint={{entity.id}}">Korekta</a>
  </div>
</div>
