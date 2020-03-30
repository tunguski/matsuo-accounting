package pl.matsuo.accounting.test;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import pl.matsuo.accounting.service.session.CashRegisterSessionState;

@Scope
@Order(0)
public class TestCashRegisterSessionState extends CashRegisterSessionState {}
