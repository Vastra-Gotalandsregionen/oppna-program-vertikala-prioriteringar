package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import se.vgregion.verticalprio.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class EditSektorRaadControllerTest {

	@Resource(name = "testEditSektorRaadController")
	EditSektorRaadController testEditSektorRaadController;

	ModelMap modelMap;

	MockHttpSession session;

	MockHttpServletResponse response;

	User user;

	List<String> id, parentId, kod, markedAsDeleted, prioCount, locked;

	Long delete = 1L, insert = 1L;

	@Before
	public void setUp() throws Exception {
		modelMap = new ModelMap();
		user = new User();
		user.setUserEditor(true);
		session = new MockHttpSession();
		session.setAttribute("user", user);
		response = new MockHttpServletResponse();

		id = Arrays.asList("1");
		parentId = Arrays.asList("1");
		kod = Arrays.asList("kod");
		markedAsDeleted = Arrays.asList("false");
		prioCount = Arrays.asList("0");
		locked = Arrays.asList("false");

	}

	@Test
	public void list() {
		testEditSektorRaadController.list(modelMap, session, response);
	}

	@Test
	public void delete() {
		testEditSektorRaadController.delete(id, parentId, kod, markedAsDeleted, delete, prioCount, locked,
		        modelMap, session);
	}

	@Test
	public void insert() {
		testEditSektorRaadController.insert(id, parentId, kod, markedAsDeleted, insert, prioCount, locked,
		        modelMap, session);
	}

	@Test
	public void toMain() throws IOException {
		testEditSektorRaadController.toMain(response);
	}

	@Test
	public void save() throws IOException {
		testEditSektorRaadController.save(response, kod, id, parentId, markedAsDeleted, prioCount, locked,
		        modelMap, session);
	}

}
