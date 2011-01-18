package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import se.vgregion.verticalprio.FindSelectDiagnosesForm;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class SelectDiagnosisControllerTest {

    @Resource(name = "testSelectDiagnosisController")
    SelectDiagnosisController sdc;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.SelectDiagnosisController#select(javax.servlet.http.HttpSession, java.util.List, java.util.List, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public final void select() {
        HttpSession session = new MockHttpSession();
        List<String> openedId = new ArrayList<String>();
        List<String> selectedId = new ArrayList<String>();
        Long closeId = 100l;
        Long deSelectId = 200l;
        sdc.select(session, openedId, selectedId, closeId, deSelectId);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.SelectDiagnosisController#findSelectDiagnoses(org.springframework.ui.ModelMap, se.vgregion.verticalprio.FindSelectDiagnosesForm)}
     * .
     */
    @Test
    public final void findSelectDiagnoses() {
        ModelMap model = new ModelMap();
        FindSelectDiagnosesForm prio = new FindSelectDiagnosesForm();
        sdc.findSelectDiagnoses(model, prio);
    }
}
