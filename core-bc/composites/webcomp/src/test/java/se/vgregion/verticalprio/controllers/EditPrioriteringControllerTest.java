package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

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
        String result = epc.save(request, response, pf);
        Assert.assertEquals("main", result);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#findAatgerder(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, se.vgregion.verticalprio.controllers.PrioriteringsobjektForm, java.util.List)}
     * .
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public final void testFindAatgerder() throws InstantiationException, IllegalAccessException {
        String result = epc.findAatgerder(request, modelMap, pf, selectedIds);
        Assert.assertEquals("prio-view", result);

    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#findDiagnoses(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, se.vgregion.verticalprio.controllers.PrioriteringsobjektForm, java.util.List)}
     * .
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public final void testFindDiagnoses() throws InstantiationException, IllegalAccessException {
        String result = epc.findDiagnoses(request, modelMap, pf, selectedIds);
        Assert.assertEquals("prio-view", result);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#findVaardformskoder(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, se.vgregion.verticalprio.controllers.PrioriteringsobjektForm, java.util.List)}
     * .
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public final void testFindVaardformskoder() throws InstantiationException, IllegalAccessException {
        String result = epc.findVaardformskoder(request, modelMap, pf, selectedIds);
        Assert.assertEquals("prio-view", result);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.EditPrioriteringControllerTest#findAtckoder(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, se.vgregion.verticalprio.controllers.PrioriteringsobjektForm, java.util.List)}
     * .
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public final void testFindAtckoder() throws InstantiationException, IllegalAccessException {
        String result = epc.findAtckoder(request, modelMap, pf, selectedIds);
        Assert.assertEquals("prio-view", result);
    }
}
