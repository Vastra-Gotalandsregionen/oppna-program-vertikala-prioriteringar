package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.FindSelectDiagnosesForm;
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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String select(HttpSession session, @RequestParam(required = false) List<String> openedId,
	        @RequestParam(required = false) List<String> selectedId, @RequestParam(required = false) Long closeId,
	        @RequestParam(required = false) Long deSelectId) {
		if (openedId == null) {
			openedId = new ArrayList<String>();
		}
		if (selectedId == null) {
			selectedId = new ArrayList<String>();
		}
		if (closeId != null) {
			openedId.remove(closeId);
		}
		if (deSelectId != null) {
			selectedId.remove(deSelectId);
		}
		List<List<DiagnosKod>> diagnoses = toColumnHierarchy(openedId, selectedId);
		session.setAttribute("diagnoses", diagnoses);

		return "select-diagnosis";
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	private List<List<DiagnosKod>> toColumnHierarchy(List<String> openedId, List<String> selectedId) {
		List<DiagnosKod> roots = diagnosKodRepository.getTreeRoots();
		List<List<DiagnosKod>> result = new ArrayList<List<DiagnosKod>>();
		toColumnHierarchyImpl(openedId, selectedId, 0, roots, result);
		return result;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	private void toColumnHierarchyImpl(List<String> openId, List<String> selectId, int level,
	        List<DiagnosKod> storedDiagnoses, List<List<DiagnosKod>> result) {
		if (level >= result.size()) {
			result.add(new ArrayList<DiagnosKod>());
		}
		result.get(level).addAll(storedDiagnoses);

		for (DiagnosKod kod : storedDiagnoses) {
			if (selectId.contains(kod.getId() + "")) {
				kod.setSelected(true);
			}
			if (openId.contains(kod.getId() + "")) {
				kod.setOpen(true);
				toColumnHierarchyImpl(openId, selectId, level + 1, kod.getChildren(), result);
			}
		}
	}

	@RequestMapping(value = "/find-select-diagnoses")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String findSelectDiagnoses(ModelMap model, FindSelectDiagnosesForm prio) {
		if (prio == null) {
			prio = new FindSelectDiagnosesForm();
		}
		model.addAttribute("prio", prio);

		DiagnosKod diagnos = new DiagnosKod();
		diagnos.setBeskrivning(prio.getFindPattern());
		prio.getFindings().addAll(diagnosKodRepository.findByExample(diagnos, 100));
		if (prio.getDiagnoserId() == null) {
			prio.setDiagnoserId(new ArrayList<Long>());
		}
		prio.getDiagnoserId().remove(null);
		for (Long id : new HashSet<Long>(prio.getDiagnoserId())) {
			DiagnosKod kod = diagnosKodRepository.find(id);
			prio.getSelectedDiagnoses().add(kod);
		}

		return "find-select-diagnoses";
	}

}
