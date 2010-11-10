package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Sector;

@Controller
public class VerticalPrioController {
    private static final Log log = LogFactory.getLog(VerticalPrioController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        log.info("in test() method");
        return "test";
    }

    @RequestMapping(value = "/main")
    public String main(Model model, @ModelAttribute("form") MainForm form) {

        if (form.getMessage() == null || "".equals(form.getMessage())) {
            form.setMessage("tjoho");
        }

        log.info(form.getSectors());

        log.info("in main() method");
        model.addAttribute("sectors", getSectors());

        model.addAttribute("columns", getColumns());
        return "main";
    }

    private List<Sector> getSectors() {
        List<Sector> result = new ArrayList<Sector>();
        for (int i = 0; i < 25; i++) {
            result.add(new Sector("Sector #" + i, i));
        }
        return result;
    }

    private List<Column> getColumns() {
        List<Column> result = new ArrayList<Column>();
        for (int i = 0; i < 15; i++) {
            result.add(new Column(i, "Column #" + i));
        }
        return result;
    }

}
