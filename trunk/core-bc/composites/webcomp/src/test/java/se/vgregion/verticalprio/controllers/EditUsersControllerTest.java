package se.vgregion.verticalprio.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
public class EditUsersControllerTest {

	@Resource(name = "testEditUsersController")
	EditUsersController euc;

	HttpSession session;
	ModelMap model;
	HttpServletResponse response;
	MockGenerisktKodRepository<User> userRepository;

	User otherUser;

	@Before
	public void setUp() {
		userRepository = (MockGenerisktKodRepository<User>) euc.usersRepository;
		session = new MockHttpSession();
		model = new ModelMap();
		response = new MockHttpServletResponse();

		User adminUser = new User();
		adminUser.setFirstName("admin-fn");
		adminUser.setLastName("admin-fn");
		adminUser.setEditor(true);
		adminUser.setUserEditor(true);
		adminUser.setApprover(true);
		session.setAttribute("user", adminUser);

		otherUser = new User();
		otherUser.setFirstName("ou-fn");
		otherUser.setLastName("ou-fn");
		otherUser.setEditor(true);
		otherUser.setUserEditor(true);
		otherUser.setApprover(true);
		session.setAttribute("otherUser", otherUser);
	}

	@Test
	public void listUsers() {
		euc.listUsers(model, session);
		assertNotNull(model.get("users"));
	}

	@Test
	public void delete() throws IOException {
		euc.delete(model, 1l, session, response);
		assertEquals((Long) 1l, userRepository.getDeletedItemsId().iterator().next());
	}

	@Test
	public void edit() throws IOException {
		session.setAttribute("otherUser", null);
		euc.edit(model, 1l, session, response);
		assertNotNull(session.getAttribute("otherUser"));
	}

	@Test
	public void create() {
		session.setAttribute("otherUser", null);
		euc.create(model, session);
		User newUser = (User) session.getAttribute("otherUser");
		assertNotNull(newUser);
		assertNull(newUser.getId());
	}

	@Test
	public void backToMain() throws IOException {
		euc.backToMain(response);
	}

	@Test
	public void save() {
		euc.save(model, otherUser, session);
		assertEquals(otherUser, userRepository.getAddedItems().iterator().next());
	}

	@Test
	public void cancelEdit() {
		euc.cancelEdit(model, session);
		assertNull(model.get("otherUser"));
		assertNull(session.getAttribute("otherUser"));
	}

	@Test
	public void selectSectors() {
		euc.selectSectors(model, otherUser, 1l, session);
	}

}
