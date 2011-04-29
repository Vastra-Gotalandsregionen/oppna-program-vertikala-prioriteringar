package se.vgregion.verticalprio.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class ControllerBaseTest {

	@Resource(name = "testControllerBase")
	ControllerBase controllerBase;

	MockHttpSession session;

	@Before
	public void setUp() {
		session = new MockHttpSession();
	}

	@Test
	public void getOrCreateSessionObj() {
		PrioriteringsobjektFindCondition condition = controllerBase.getOrCreateSessionObj(session, "condition",
		        PrioriteringsobjektFindCondition.class);

		assertNotNull(condition);
		assertEquals(condition, session.getAttribute("condition"));
	}

	@Test
	public void getMainForm() {
		MainForm mf = controllerBase.getMainForm(session);

		assertNotNull(mf);
	}

	@Test
	public void initMainForm() {
		MainForm form = controllerBase.getOrCreateSessionObj(session, "form", MainForm.class);
		controllerBase.initMainForm(form, session);
	}

}
