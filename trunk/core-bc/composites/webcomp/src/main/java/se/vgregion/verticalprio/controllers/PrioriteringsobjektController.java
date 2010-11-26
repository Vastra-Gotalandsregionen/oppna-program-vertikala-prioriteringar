package se.vgregion.verticalprio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller(value = "")
public class PrioriteringsobjektController {

    @RequestMapping(value = "/save")
    public void save(@RequestParam Prioriteringsobjekt prioriteringsobjekt) {

    }

}
