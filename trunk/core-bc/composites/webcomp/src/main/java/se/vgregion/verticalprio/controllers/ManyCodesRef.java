package se.vgregion.verticalprio.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.vgregion.verticalprio.entity.AbstractKod;

@SuppressWarnings("serial")
public abstract class ManyCodesRef<T extends AbstractKod> implements Serializable {
    private String searchBeskrivningText;
    private String searchKodText;
    private List<T> findings = new ArrayList<T>();
    private List<Long> selectedCodesId = new ArrayList<Long>();

    private Set<T> codes = new HashSet<T>();

    /**
     * @return the codes
     */
    public Set<T> getCodes() {
        return codes;
    }

    /**
     * @param codes
     *            the codes to set
     */
    public void setCodes(Set<T> codes) {
        this.codes = codes;
    }

    public List<T> getFindings() {
        return findings;
    }

    public void setFindings(List<T> findings) {
        this.findings = findings;
    }

    public List<Long> getSelectedCodesId() {
        return selectedCodesId;
    }

    private void setSelectedCodesId(List<Long> selectedCodesId) {
        this.selectedCodesId = selectedCodesId;
    }

    public String getSearchBeskrivningText() {
        return searchBeskrivningText;
    }

    public void setSearchBeskrivningText(String searchBeskrivningText) {
        this.searchBeskrivningText = searchBeskrivningText;
    }

    public String getSearchKodText() {
        return searchKodText;
    }

    public void setSearchKodText(String searchKodText) {
        this.searchKodText = searchKodText;
    }

}