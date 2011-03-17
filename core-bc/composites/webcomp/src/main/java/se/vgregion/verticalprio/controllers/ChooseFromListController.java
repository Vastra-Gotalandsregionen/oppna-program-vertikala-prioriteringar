package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class ChooseFromListController extends ControllerBase {

    @RequestMapping(value = "/choose-from-list", params = { "ok" })
    public String ok(HttpServletResponse response, HttpSession session, ModelMap model) throws IOException {
        final ChooseListForm form = getChooseListForm(model, session);
        if (form.maxSelection != null && form.maxSelection < form.choosen.size()) {
            MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
            String message = "Du kan maximalt välja " + form.maxSelection + " samtidigt.";
            messageHome.setMessage(message);
            return "choose-from-list";
        }
        form.target.clear();
        form.target.addAll(form.getChoosen());
        response.sendRedirect(form.getOkUrl());
        return null;
    }

    @RequestMapping(value = "/choose-from-list", params = { "cancel" })
    public String cancel(HttpServletResponse response, HttpSession session, ModelMap model) throws IOException {
        final ChooseListForm form = getChooseListForm(model, session);
        response.sendRedirect(form.getCancelUrl());
        return null;
    }

    @RequestMapping(value = "/choose-from-list", params = { "filter" })
    public String filter(HttpSession session, @RequestParam(required = false) List<String> notYetChoosenKeys,
            @RequestParam(required = false) String filterText, ModelMap model) {
        return main(session, filterText, null, null, model);
    }

    @RequestMapping(value = "/choose-from-list", params = { "add" })
    public String add(HttpSession session, @RequestParam(required = false) List<String> notYetChoosenKeys,
            ModelMap model, @RequestParam(required = false) String filterText) {
        final ChooseListForm form = getChooseListForm(model, session);
        if (notYetChoosenKeys != null) {
            for (Object item : form.getAllItems()) {
                BeanMap bm = new BeanMap(item);
                Object keyValue = bm.get(form.getIdKey());
                if (keyValue != null && notYetChoosenKeys.contains(keyValue.toString())) {
                    form.getChoosen().add(item);
                }
            }
        }

        return main(session, filterText, null, null, model);
    }

    @RequestMapping(value = "/choose-from-list", params = { "addAll" })
    public String addAll(HttpSession session, ModelMap model, @RequestParam(required = false) String filterText) {
        final ChooseListForm form = getChooseListForm(model, session);
        String result = main(session, filterText, null, null, model);
        form.getChoosen().clear();
        form.getChoosen().addAll(form.getAllToChoose());
        form.setChoosen(sort(form.getChoosen(), form.getDisplayKey()));
        return result;
    }

    @RequestMapping(value = "/choose-from-list", params = { "remove" })
    public String remove(HttpSession session, @RequestParam(required = false) List<String> choosenKeys,
            ModelMap model, @RequestParam(required = false) String filterText) {
        final ChooseListForm form = getChooseListForm(model, session);
        if (choosenKeys != null) {
            for (Object item : new ArrayList(form.getChoosen())) {
                BeanMap bm = new BeanMap(item);
                Object keyValue = bm.get(form.getIdKey());
                if (keyValue != null && choosenKeys.contains(keyValue.toString())) {
                    form.getChoosen().remove(item);
                }
            }
        }

        return main(session, filterText, null, null, model);
    }

    @RequestMapping(value = "/choose-from-list", params = { "removeAll" })
    public String removeAll(HttpSession session, ModelMap model, @RequestParam(required = false) String filterText) {
        final ChooseListForm form = getChooseListForm(model, session);
        form.getChoosen().clear();
        // form.setAllToChoose(sort(form.getAllToChoose(), form.getDisplayKey()));
        return main(session, filterText, null, null, model);
    }

    private ChooseListForm getChooseListForm(ModelMap model, HttpSession session) {
        ChooseListForm form = (ChooseListForm) model.get(ChooseListForm.class.getSimpleName());
        if (form == null) {
            form = (ChooseListForm) session.getAttribute(ChooseListForm.class.getSimpleName());
            model.addAttribute(ChooseListForm.class.getSimpleName(), form);
        }
        return form;
    }

    @RequestMapping(value = "/choose-from-list")
    public String main(HttpSession session, @RequestParam(required = false) String filterText,
            @RequestParam(required = false) List<String> notYetChoosenKeys,
            @RequestParam(required = false) List<String> choosenKeys, ModelMap model) {

        final ChooseListForm form = getChooseListForm(model, session);

        form.setAllToChoose(sort(form.getAllItems(), form.getDisplayKey()));
        form.setFilterText(filterText);

        if (filterText != null && !"".equals(filterText.trim())) {
            removeThoseWithNoSuchSubstring(form.getAllToChoose(), form.getDisplayKey(), filterText);
        }

        form.setAllItems(sort(form.getAllItems(), form.getDisplayKey()));
        form.setChoosen(sort(form.getChoosen(), form.getDisplayKey()));

        return "choose-from-list";
    }

    private List sort(Collection values, String sortProperty) {
        SortedMap<String, Object> tm = new TreeMap<String, Object>();
        for (Object value : values) {
            BeanMap valueMap = new BeanMap(value);
            Object key = valueMap.get(sortProperty);
            if (key != null) {
                tm.put(key.toString().toLowerCase(), value);
            }
        }
        return new ArrayList(tm.values());
    }

    private void removeThoseWithNoSuchSubstring(List items, String propertyName, String subString) {
        subString = subString.toLowerCase();
        for (Object item : new ArrayList(items)) {
            BeanMap bm = new BeanMap(item);
            String value = (String) bm.get(propertyName);
            if (!(value != null && value.toLowerCase().contains(subString))) {
                items.remove(item);
            }
        }
    }

    public static class ChooseListForm implements Serializable, Cloneable {

        private static final long serialVersionUID = 1L;

        private String displayKey, idKey, choosenLabel, notYetChoosenLabel, filterLabel, filterText, cancelUrl,
                okUrl, okLabel;

        private List allToChoose = new ArrayList();
        private List choosen = new ArrayList();
        private List allItems = new ArrayList();
        private Collection target = new ArrayList();
        private Integer maxSelection = 25; // This indicates the max number of items a user can select
        private Integer minNumberOfItemsForShowingFindButton = 25;

        /**
         * If true the show the Find button
         * 
         * @return
         */
        public boolean isFindingVisible() {
            return (allItems.size() > minNumberOfItemsForShowingFindButton && filterLabel != null && !""
                    .equals(filterLabel.trim())) || (filterText != null && !"".equals(filterText.trim()));
        }

        public String getDisplayKey() {
            return displayKey;
        }

        public void setDisplayKey(String displayKey) {
            this.displayKey = displayKey;
        }

        public String getIdKey() {
            return idKey;
        }

        public void setIdKey(String idKey) {
            this.idKey = idKey;
        }

        public String getChoosenLabel() {
            return choosenLabel;
        }

        public void setChoosenLabel(String choosenLabel) {
            this.choosenLabel = choosenLabel;
        }

        public String getNotYetChoosenLabel() {
            return notYetChoosenLabel;
        }

        public void setNotYetChoosenLabel(String notYetChoosenLabel) {
            this.notYetChoosenLabel = notYetChoosenLabel;
        }

        public String getFilterLabel() {
            return filterLabel;
        }

        public void setFilterLabel(String filterLabel) {
            this.filterLabel = filterLabel;
        }

        public List getAllToChoose() {
            return allToChoose;
        }

        public int getSizeOfAllToChoose() {
            return allToChoose.size();
        }

        public void setAllToChoose(List allToChoose) {
            this.allToChoose = allToChoose;
        }

        public List getChoosen() {
            return choosen;
        }

        public int getSizeOfChoosen() {
            return choosen.size();
        }

        public void setChoosen(List choosen) {
            this.choosen = choosen;
        }

        public List getAllItems() {
            return allItems;
        }

        public void setAllItems(List allItems) {
            this.allItems = allItems;
        }

        public void setFilterText(String filter) {
            this.filterText = filter;
        }

        public String getFilterText() {
            return filterText;
        }

        public void setCancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
        }

        public String getCancelUrl() {
            return cancelUrl;
        }

        public void setOkUrl(String okUrl) {
            this.okUrl = okUrl;
        }

        public String getOkUrl() {
            return okUrl;
        }

        public void setTarget(Collection target) {
            this.target = target;
        }

        public Collection getTarget() {
            return target;
        }

        public void setOkLabel(String okLabel) {
            this.okLabel = okLabel;
        }

        public String getOkLabel() {
            return okLabel;
        }

        /**
         * @inheritDoc
         */
        @Override
        public ChooseListForm clone() {
            ChooseListForm result;
            try {
                result = getClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            BeanMap resultMap = new BeanMap(result);
            BeanMap thisMap = new BeanMap(this);
            for (Object key : thisMap.keySet()) {
                if (resultMap.getWriteMethod(key.toString()) != null) {
                    Object value = thisMap.get(key);
                    if (value instanceof List) {
                        List list = (List) value;
                        value = new ArrayList(list);
                    }
                    resultMap.put(key, value);
                }
            }

            return result;
        }

        public void setMaxSelection(Integer maxSelection) {
            this.maxSelection = maxSelection;
        }

        public Integer getMaxSelection() {
            return maxSelection;
        }
    }

}
