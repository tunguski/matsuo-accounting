package pl.matsuo.accounting.web.controller.print;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.matsuo.accounting.model.print.AccountingPrint;
import pl.matsuo.accounting.model.print.Invoice;
import pl.matsuo.accounting.model.print.TotalCost;
import pl.matsuo.clinic.model.medical.appointment.Appointment;
import pl.matsuo.clinic.model.medical.doctor.Doctor;
import pl.matsuo.clinic.model.medical.service.Service;
import pl.matsuo.clinic.model.print.cash.Invoice;
import pl.matsuo.clinic.model.print.cash.InvoicePosition;
import pl.matsuo.clinic.model.print.cash.TotalCost;
import pl.matsuo.clinic.service.medical.services.LoadScheduleForm;
import pl.matsuo.clinic.service.medical.services.ScheduleElement;
import pl.matsuo.core.model.organization.Person;
import pl.matsuo.core.model.query.QueryBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.matsuo.clinic.model.medical.appointment.AppointmentStatus.*;
import static pl.matsuo.clinic.model.print.Print.*;
import static pl.matsuo.clinic.model.print.cash.PaymentType.*;
import static pl.matsuo.accounting.util.PrintUtil.*;
import static pl.matsuo.core.util.collection.CollectionUtil.*;
import static pl.matsuo.core.util.DateUtil.*;
import static pl.matsuo.core.util.NumberSpeaker.*;
import static pl.matsuo.core.util.NumberUtil.*;


@Controller
@RequestMapping("/invoices")
public class InvoiceController extends CashDocumentController<Invoice> {


  protected String numerationName(Invoice invoice) {
    return invoice.getIsReceipt() != null && invoice.getIsReceipt() ? "RECEIPT" : "INVOICE";
  }


  @Override
  protected void preCreate(AccountingPrint print, Invoice cashDocument) {
    // jeśli to faktura, dostaje swój numer
    cashDocument.setNumber(numerationService.getNumber(numerationName(cashDocument)));

    // zmiana statusu wizyty wraz z utworzeniem faktury/paragonu dla tej wizyty
    if (print.getIdEntity() != null) {
      Appointment appointment = database.findById(Appointment.class, print.getIdEntity());
      if (appointment.getStatus().equals(AppointmentStatus.NEW)) {
        appointment.setStatus(AppointmentStatus.AWAITING);
      }
      database.update(appointment);
    }
  }


  @RequestMapping(value = "/for_appointment/{id}", method = GET)
  @ResponseBody
  public AccountingPrint forAppointment(@PathVariable("id") Integer id) {
    AccountingPrint print = print(Invoice.class, id).get();

    Invoice invoice = facadeBuilder.createFacade(print, Invoice.class);

    invoice.setIssuanceDate(date(new Date(), 0, 0));
    invoice.setSellDate(date(new Date(), 0, 0));
    invoice.setDueDate(date(new Date(), 0, 0));
    invoice.setPaymentType(CASH);

    // nadanie testowego numeru faktury
    invoice.setNumber(numerationService.getPreviewNumber(numerationName(invoice)));

    Appointment appointment = database.findById(Appointment.class, id);

    String doctorName = "";
    if (appointment.getIdDoctor() != null) {
      Doctor doctor = database.findById(Doctor.class, appointment.getIdDoctor());
      doctorName = doctor.getPerson().getFirstName() + " " + doctor.getPerson().getLastName();
    }
    Person patient = database.findById(Person.class, appointment.getIdPatient());

    invoice.setComments("Pacjent " + patient.getFirstName() + " " + patient.getLastName());

    List<Service> services = database.find(QueryBuilder.query(Service.class, QueryBuilder.in("code", appointment.getIcd9())));
    Map<Object, Service> servicesMap = toMap(services, "code");

    for (String code : appointment.getIcd9()) {
      print.getElements().add(new PrintElement());
      InvoicePosition invoicePosition = invoice.getElements().get(invoice.getElements().size() - 1);

      Service service = servicesMap.get(code);
      invoicePosition.setServiceName(doctorName + " " + service.getName());

      List<ScheduleElement> scheduleElements = asList(new ScheduleElement(null, null, null, null));
      servicesService.createScheduleResult(new LoadScheduleForm(appointment, code), scheduleElements);

      invoicePosition.setCount(bd("1"));
      invoicePosition.setPrice(scheduleElements.get(0).getPrice());
      invoicePosition.setTaxRate("zw");
    }

    return print;
  }


  protected AccountingPrint fillDocument(AccountingPrint print, Invoice facade) {
    super.fillDocument(print, facade);

    if (facade.getIsReceipt() == null) {
      facade.setIsReceipt(false);
    }

    TotalCost sum = sumInvoicePositions(facade);
    BigDecimal amountPaid = new BigDecimal(0);
    facade.setTotalAmount(sum.getSum());
    facade.setCashRegisterAmount(sum.getSum());

    facade.setAmountAlreadyPaid(amountPaid);
    facade.setAmountDue(sum.getSum().subtract(amountPaid));
    facade.setAmountDueInWords(speakCashAmount(sum.getSum().subtract(amountPaid)));

    facade.setAuthenticityText("ORYGINAŁ");
    facade.setAreCommentsVisible(true);

    return print;
  }
}

