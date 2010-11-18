package se.vgregion.verticalprio.model;

public class Prio {

    int id;

    String sectorCouncil = "Sektorsråd";
    String diagnoseText = "Diagnostext";
    String diagnoseCode = "Diagnoskod";
    String measureCode = "Åtgärdskod";
    String difficultyDegree = "Tillståndets svårighetsgrad";
    String ranking = "Rangordning";
    String indicationGaf = "Indikations GAF";
    String measureRisc = "Risk m. åtgärd";
    String patientBenefit = "Patientnytta åtgärd - effekt";
    String patientBenefitEvidence = "Evidens för patientnytta";
    String careLevel = "Vårdnivå";
    String careForm = "Vårdform";
    String healthEconomicEvidence = "Hälsoekonomisk evidens";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectorCouncil() {
        return sectorCouncil;
    }

    public void setSectorCouncil(String sectorCouncil) {
        this.sectorCouncil = sectorCouncil;
    }

    public String getDiagnoseText() {
        return diagnoseText;
    }

    public void setDiagnoseText(String diagnoseText) {
        this.diagnoseText = diagnoseText;
    }

    public String getDiagnoseCode() {
        return diagnoseCode;
    }

    public void setDiagnoseCode(String diagnoseCode) {
        this.diagnoseCode = diagnoseCode;
    }

    public String getMeasureCode() {
        return measureCode;
    }

    public void setMeasureCode(String measureCode) {
        this.measureCode = measureCode;
    }

    public String getDifficultyDegree() {
        return difficultyDegree;
    }

    public void setDifficultyDegree(String difficultyDegree) {
        this.difficultyDegree = difficultyDegree;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getIndicationGaf() {
        return indicationGaf;
    }

    public void setIndicationGaf(String indicationGaf) {
        this.indicationGaf = indicationGaf;
    }

    public String getMeasureRisc() {
        return measureRisc;
    }

    public void setMeasureRisc(String measureRisc) {
        this.measureRisc = measureRisc;
    }

    public String getPatientBenefit() {
        return patientBenefit;
    }

    public void setPatientBenefit(String patientBenefit) {
        this.patientBenefit = patientBenefit;
    }

    public String getPatientBenefitEvidence() {
        return patientBenefitEvidence;
    }

    public void setPatientBenefitEvidence(String patientBenefitEvidence) {
        this.patientBenefitEvidence = patientBenefitEvidence;
    }

    public String getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

    public String getCareForm() {
        return careForm;
    }

    public void setCareForm(String careForm) {
        this.careForm = careForm;
    }

    public String getHealthEconomicEvidence() {
        return healthEconomicEvidence;
    }

    public void setHealthEconomicEvidence(String healthEconomicEvidence) {
        this.healthEconomicEvidence = healthEconomicEvidence;
    }

}
