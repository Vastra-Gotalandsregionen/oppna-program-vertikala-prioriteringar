package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
public class VerticalPrioControllerTest {

    @Resource(name = "testVerticalPrioController")
    private VerticalPrioController vpc;

    private MockHttpServletRequest request;

    private HttpSession session;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        session = request.getSession();
        MainForm form = new MainForm();
        session.setAttribute("form", form);
        response = new MockHttpServletResponse();
    }

    @Test
    public void main() {
        String defaultResult = vpc.main(session);
        Assert.assertEquals("main", defaultResult);
    }

    private List<Column> getVisibleColumns() {
        List<Column> result = new ArrayList<Column>(Prioriteringsobjekt.getDefaultColumns());

        for (Column column : Prioriteringsobjekt.getDefaultColumns()) {
            if (!column.isVisible()) {
                result.remove(column);
            }
        }

        return result;
    }

    private List<String> toNameList(List<Column> columns) {
        List<String> result = new ArrayList<String>();
        for (Column column : columns) {
            result.add(column.getName());
        }
        return result;
    }

    @Test
    public void check() throws IOException {
        String result = vpc.check(session, 1, response);
        Assert.assertEquals("main", result);
    }

    @Test
    public void getMarkedLeafs() {
        SektorRaad root = new SektorRaad(0l);
        List<SektorRaad> roots = new ArrayList<SektorRaad>();
        roots.add(new SektorRaad(1l));
        roots.add(root);
        roots.add(new SektorRaad(2l));

        root.setSelected(true);
        root.setChildren(new ArrayList<SektorRaad>());
        root.getChildren().add(new SektorRaad(1l));
        List<SektorRaad> result = vpc.getMarkedLeafs(roots);
        System.out.println(result);
    }

}
