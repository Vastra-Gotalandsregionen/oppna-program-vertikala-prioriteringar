package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class EditPrioriteringControllerTest {

	@Resource(name = "testEditPrioriteringController")
	EditPrioriteringController epc;

	ModelMap modelMap;
	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockHttpSession session;
	PrioriteringsobjektForm pf;
	List<String> selectedIds;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		modelMap = new ModelMap();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		pf = new PrioriteringsobjektForm();
		selectedIds = new ArrayList<String>();
		session = new MockHttpSession();
	}

	/**
	 * Test method for
	 * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#initView(org.springframework.ui.ModelMap, java.lang.Long)}
	 * .
	 */
	@Test
	public final void testInitView() {
		Long id = 100l;
		String result = epc.initView(modelMap, request.getSession(), id);
		Assert.assertEquals("prio-view", result);
	}

	/**
	 * Test method for
	 * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#save(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, se.vgregion.verticalprio.controllers.PrioriteringsobjektForm)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testSave() throws IOException {
		session.setAttribute("prio", new PrioriteringsobjektForm());
		String result = epc.save(request, response, session, pf);
		Assert.assertEquals("prio-view", result);
	}

	@Test
	public void initDeleteView() {
		User user = new User();
		MockPrioRepository mpr = (MockPrioRepository) epc.prioRepository;
		Prioriteringsobjekt prio = new Prioriteringsobjekt();
		SektorRaad sr = new SektorRaad();
		prio.setSektorRaad(sr);
		sr.setKod("kod abc");
		mpr.setIdFindResult(prio);

		session.setAttribute("user", user);
		epc.initDeleteView(new ModelMap(), session, 1l);
		MessageHome mh = (MessageHome) session.getAttribute("messageHome");
		session.removeAttribute("messageHome");
		Assert.assertTrue(mh.getMessage().contains("kod abc"));

		user.getSektorRaad().add(sr);
		epc.initDeleteView(new ModelMap(), session, 1l);
		Assert.assertEquals(null, session.getAttribute("messageHome"));
	}

	@Test
	public final void testRemoveCodes() {
		List<String> ids = Arrays.asList("diagnoser:1", "diagnoser:2", "diagnoser:3");
		int count = pf.getDiagnoser().size();
		addEmptyDiagnosWithIdOnly(pf.getDiagnoser(), 1l, 2l, 3l);
		session.setAttribute("prio", pf);
		epc.removeCodes(ids, modelMap, session, pf);
		Assert.assertEquals(count, pf.getDiagnoser().size());
	}

	private void addEmptyDiagnosWithIdOnly(Collection<DiagnosKod> diagnoses, Long... ids) {
		for (Long id : ids) {
			DiagnosKod dk = new DiagnosKod();
			dk.setId(1l);
			diagnoses.add(dk);
		}
	}
}
