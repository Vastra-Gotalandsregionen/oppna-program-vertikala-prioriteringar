package se.vgregion.verticalprio.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.AbstractKod;

@SuppressWarnings("serial")
public abstract class ManyCodesRef<T extends AbstractKod> implements Serializable {
    private String searchWord;
    private List<T> findings = new ArrayList<T>();
    private List<Long> selectedCodesId = new ArrayList<Long>();

    private List<T> codes = new ArrayList<T>();

    /**
     * @return the codes
     */
    public List<T> getCodes() {
        return codes;
    }

    /**
     * @param codes
     *            the codes to set
     */
    public void setCodes(List<T> codes) {
        this.codes = codes;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
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

    public void setSelectedCodesId(List<Long> selectedCodesId) {
        this.selectedCodesId = selectedCodesId;
    }

}