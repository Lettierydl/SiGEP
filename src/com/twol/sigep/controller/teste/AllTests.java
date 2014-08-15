package com.twol.sigep.controller.teste;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ControllerClienteTest.class, ControllerEstoqueTest.class,
		ControllerFuncionarioTest.class, ControllerPagamentoTest.class, ControllerVendaTest.class })
public class AllTests {

}
