package pl.matsuo.accounting.test;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import pl.matsuo.accounting.service.session.ClinicSessionState;


/**
 * Created by tunguski on 15.01.14.
 */
@Scope
@Order(0)
public class TestClinicSessionState extends ClinicSessionState {
}

