package se.vgregion.verticalprio.controllers;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;

/**
 * Controller for page giving the user a chance to select one or more diagnoses.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class SelectDiagnosisController extends ControllerBase {

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

    @RequestMapping(value = "/select-diagnosis")
    public String select(HttpSession session) {
        String diagnosesKey = "diagnoses";
        List<DiagnosKod> diagnoses = (List<DiagnosKod>) session.getAttribute(diagnosesKey);
        if (diagnoses == null) {
            diagnoses = diagnosKodRepository.getTreeRoots();
            session.setAttribute(diagnosesKey, diagnoses);
        }

        return "select-diagnosis";
    }

}
