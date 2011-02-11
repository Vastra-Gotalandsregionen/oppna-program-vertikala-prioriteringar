package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;

import se.vgregion.verticalprio.controllers.ChooseFromListController.ChooseListForm;
import se.vgregion.verticalprio.entity.SektorRaad;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class ChooseFromListControllerTest {
    HttpSession session;
    ModelMap model;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        model = new ModelMap();
        session = new MockHttpSession();

        List<SektorRaad> raads = new ArrayList<SektorRaad>();

        SektorRaad sr = new SektorRaad();
        sr.setKod("One 1");
        sr.setBeskrivning("Besk 1");
        sr.setKortBeskrivning("kortBeskrivning 1");
        sr.setId(0l);
        raads.add(sr);

        sr = new SektorRaad();
        sr.setKod("One 2");
        sr.setBeskrivning("Besk 2");
        sr.setKortBeskrivning("kortBeskrivning 2");
        sr.setId(1l);
        raads.add(sr);

        sr = new SektorRaad();
        sr.setKod("One 3");
        sr.setBeskrivning("Besk 3");
        sr.setKortBeskrivning("kortBeskrivning 3");
        sr.setId(2l);
        raads.add(sr);

        sr = new SektorRaad();
        sr.setKod("One 2");
        sr.setBeskrivning("Besk 2");
        sr.setKortBeskrivning("kortBeskrivning 2");
        sr.setId(1l);
        raads.add(sr);

        ChooseFromListController.ChooseListForm form = new ChooseListForm();
        form.setAllItems(raads);
        form.setAllToChoose(new ArrayList(raads));
        form.setDisplayKey("kortBeskrivning");
        form.setIdKey("id");

        session.setAttribute(ChooseListForm.class.getSimpleName(), form);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.controllers.ChooseFromListController#init(javax.servlet.http.HttpSession)}.
     */
    @Test
    public final void add() {
        ChooseFromListController cfl = new ChooseFromListController();

        List<String> ids = new ArrayList<String>();
        ids.add("0");

        String result = cfl.add(session, ids, model);

        ChooseListForm clf = (ChooseListForm) session.getAttribute(ChooseListForm.class.getSimpleName());

        Assert.assertEquals(3, clf.getAllItems().size());

        Assert.assertEquals(1, clf.getChoosen().size());

        Assert.assertEquals("choose-from-list", result);
    }
}
