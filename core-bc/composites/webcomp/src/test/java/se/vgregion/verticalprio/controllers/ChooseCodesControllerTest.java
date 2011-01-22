package se.vgregion.verticalprio.controllers;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

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

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        session = new MockHttpSession();
        modelMap = new ModelMap();
        request = new MockHttpServletRequest();
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.ChooseCodesController#start(javax.servlet.http.HttpSession, org.springframework.ui.ModelMap)}
     * .
     */
    @Test
    public final void start() {
        ccc.start(session, modelMap);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.ChooseCodesController#findAndOrSelect(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpSession, org.springframework.ui.ModelMap, java.lang.String)}
     * .
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public final void findAndOrSelect() throws InstantiationException, IllegalAccessException {
        ccc.findAndOrSelect(request, session, modelMap, "aatgaerdRef");
        ccc.findAndOrSelect(request, session, modelMap, "atcKoderRef");
        ccc.findAndOrSelect(request, session, modelMap, "diagnosRef");
        ccc.findAndOrSelect(request, session, modelMap, "vaardformRef");
        ccc.findAndOrSelect(request, session, modelMap, "rangordningsRef");
        ccc.findAndOrSelect(request, session, modelMap, "tillstaandetsSvaarighetsgradRef");

    }
}
