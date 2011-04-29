package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class ChooseCodesControllerTest {

	@Resource(name = "testChooseCodesController")
	ChooseCodesController ccc;

	ModelMap modelMap;
	MockHttpServletRequest request;
	MockHttpSession session;
	MockHttpServletResponse response;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		session = new MockHttpSession();
		List<Prioriteringsobjekt> rows = new ArrayList<Prioriteringsobjekt>();
		session.setAttribute("rows", rows);

		modelMap = new ModelMap();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		ccc = new ChooseCodesController();
	}

	@Test
	public void start() throws Exception {
		for (String fieldName : ccc.formPrototypes.keySet()) {
			ccc.start(session, response, fieldName);
			setUp();
		}
	}

}
