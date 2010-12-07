package se.vgregion.verticalprio.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.util.TextToBeanConverter;

/**
 * Loads data from [Code entity name (class 'SimpleName')].data files into the db. Put the *.data files under the
 * dbLoad directory on the class path.
 * 
 * The main method in this class is used to generate the rest of it. Can come in handy if the entities changes.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@ContextConfiguration("classpath:testApplicationContext.xml")
public class LoadCodeDataTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource(name = "sektorRaadRepository")
    GenerisktKodRepository<SektorRaad> sektorRaadRepository;

    @Test
    @Rollback(false)
    public void loadSektorRaadValuesIntoDb() throws FileNotFoundException {
        List<SektorRaad> codes = toBeans("SektorRaad", SektorRaad.class);
        for (SektorRaad code : codes) {
            sektorRaadRepository.persist(code);
        }
        sektorRaadRepository.flush();
    }

    @Resource(name = "aatgaerdsKodRepository")
    GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;

    @Test
    @Rollback(false)
    public void loadAatgaerdsKodValuesIntoDb() throws FileNotFoundException {
        List<AatgaerdsKod> codes = toBeans("AatgaerdsKod", AatgaerdsKod.class);
        for (AatgaerdsKod code : codes) {
            aatgaerdsKodRepository.persist(code);
        }
        aatgaerdsKodRepository.flush();
    }

    @Resource(name = "aatgaerdsRiskKodRepository")
    GenerisktKodRepository<AatgaerdsRiskKod> aatgaerdsRiskKodRepository;

    @Test
    @Rollback(false)
    public void loadAatgaerdsRiskKodValuesIntoDb() throws FileNotFoundException {
        List<AatgaerdsRiskKod> codes = toBeans("AatgaerdsRiskKod", AatgaerdsRiskKod.class);
        for (AatgaerdsRiskKod code : codes) {
            aatgaerdsRiskKodRepository.persist(code);
        }
        aatgaerdsRiskKodRepository.flush();
    }

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

    @Test
    @Rollback(false)
    public void loadDiagnosKodValuesIntoDb() throws FileNotFoundException {
        List<DiagnosKod> codes = toBeans("DiagnosKod", DiagnosKod.class);
        for (DiagnosKod code : codes) {
            diagnosKodRepository.persist(code);
        }
        diagnosKodRepository.flush();
    }

    @Resource(name = "haelsonekonomiskEvidensKodRepository")
    GenerisktKodRepository<HaelsonekonomiskEvidensKod> haelsonekonomiskEvidensKodRepository;

    @Test
    @Rollback(false)
    public void loadHaelsonekonomiskEvidensKodValuesIntoDb() throws FileNotFoundException {
        List<HaelsonekonomiskEvidensKod> codes = toBeans("HaelsonekonomiskEvidensKod",
                HaelsonekonomiskEvidensKod.class);
        for (HaelsonekonomiskEvidensKod code : codes) {
            haelsonekonomiskEvidensKodRepository.persist(code);
        }
        haelsonekonomiskEvidensKodRepository.flush();
    }

    @Resource(name = "kostnadLevnadsaarKodRepository")
    GenerisktKodRepository<KostnadLevnadsaarKod> kostnadLevnadsaarKodRepository;

    @Test
    @Rollback(false)
    public void loadKostnadLevnadsaarKodValuesIntoDb() throws FileNotFoundException {
        List<KostnadLevnadsaarKod> codes = toBeans("KostnadLevnadsaarKod", KostnadLevnadsaarKod.class);
        for (KostnadLevnadsaarKod code : codes) {
            kostnadLevnadsaarKodRepository.persist(code);
        }
        kostnadLevnadsaarKodRepository.flush();
    }

    @Resource(name = "patientnyttaEffektAatgaerdsKodRepository")
    GenerisktKodRepository<PatientnyttaEffektAatgaerdsKod> patientnyttaEffektAatgaerdsKodRepository;

    @Test
    @Rollback(false)
    public void loadPatientnyttaEffektAatgaerdsKodValuesIntoDb() throws FileNotFoundException {
        List<PatientnyttaEffektAatgaerdsKod> codes = toBeans("PatientnyttaEffektAatgaerdsKod",
                PatientnyttaEffektAatgaerdsKod.class);
        for (PatientnyttaEffektAatgaerdsKod code : codes) {
            patientnyttaEffektAatgaerdsKodRepository.persist(code);
        }
        patientnyttaEffektAatgaerdsKodRepository.flush();
    }

    @Resource(name = "patientnyttoEvidensKodRepository")
    GenerisktKodRepository<PatientnyttoEvidensKod> patientnyttoEvidensKodRepository;

    @Test
    @Rollback(false)
    public void loadPatientnyttoEvidensKodValuesIntoDb() throws FileNotFoundException {
        List<PatientnyttoEvidensKod> codes = toBeans("PatientnyttoEvidensKod", PatientnyttoEvidensKod.class);
        for (PatientnyttoEvidensKod code : codes) {
            patientnyttoEvidensKodRepository.persist(code);
        }
        patientnyttoEvidensKodRepository.flush();
    }

    @Resource(name = "rangordningsKodRepository")
    GenerisktKodRepository<RangordningsKod> rangordningsKodRepository;

    @Test
    @Rollback(false)
    public void loadRangordningsKodValuesIntoDb() throws FileNotFoundException {
        List<RangordningsKod> codes = toBeans("RangordningsKod", RangordningsKod.class);
        for (RangordningsKod code : codes) {
            rangordningsKodRepository.persist(code);
        }
        rangordningsKodRepository.flush();
    }

    @Resource(name = "tillstaandetsSvaarighetsgradKodRepository")
    GenerisktKodRepository<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodRepository;

    @Test
    @Rollback(false)
    public void loadTillstaandetsSvaarighetsgradKodValuesIntoDb() throws FileNotFoundException {
        List<TillstaandetsSvaarighetsgradKod> codes = toBeans("TillstaandetsSvaarighetsgradKod",
                TillstaandetsSvaarighetsgradKod.class);
        for (TillstaandetsSvaarighetsgradKod code : codes) {
            tillstaandetsSvaarighetsgradKodRepository.persist(code);
        }
        tillstaandetsSvaarighetsgradKodRepository.flush();
    }

    @Resource(name = "vaardformsKodRepository")
    GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;

    @Test
    @Rollback(false)
    public void loadVaardformsKodValuesIntoDb() throws FileNotFoundException {
        List<VaardformsKod> codes = toBeans("VaardformsKod", VaardformsKod.class);
        for (VaardformsKod code : codes) {
            vaardformsKodRepository.persist(code);
        }
        vaardformsKodRepository.flush();
    }

    @Resource(name = "vaardnivaaKodRepository")
    GenerisktKodRepository<VaardnivaaKod> vaardnivaaKodRepository;

    @Test
    @Rollback(false)
    public void loadVaardnivaaKodValuesIntoDb() throws FileNotFoundException {
        List<VaardnivaaKod> codes = toBeans("VaardnivaaKod", VaardnivaaKod.class);
        for (VaardnivaaKod code : codes) {
            vaardnivaaKodRepository.persist(code);
        }
        vaardnivaaKodRepository.flush();
    }

    @Resource(name = "vaentetidsKodRepository")
    GenerisktKodRepository<VaentetidsKod> vaentetidsKodRepository;

    @Test
    @Rollback(false)
    public void loadVaentetidsKodValuesIntoDb() throws FileNotFoundException {
        List<VaentetidsKod> codes = toBeans("VaentetidsKod", VaentetidsKod.class);
        for (VaentetidsKod code : codes) {
            vaentetidsKodRepository.persist(code);
        }
        vaentetidsKodRepository.flush();
    }

    private <T> List<T> toBeans(String entityName, Class<T> klass) throws FileNotFoundException {
        File file = getFileByNameOnClassPath("/dbLoad/" + entityName + ".data");
        if (file == null || !file.exists()) {
            // Assert.fail();
            file = getFileByNameOnClassPath("/dbLoad/generic.data");
        }
        TextToBeanConverter converter = new TextToBeanConverter();
        converter.setValueDelimiterExpr(Pattern.quote("|"));
        List<T> codes = converter.load(new FileInputStream(file), klass);
        return codes;
    }

    private File getFileByNameOnClassPath(String name) {
        return EntityGeneratorTool.getFileByNameOnClassPath(name);
    }

    public static void main(String[] args) {

        Prioriteringsobjekt prio = new Prioriteringsobjekt();
        BeanMap bm = new BeanMap(prio);
        for (Object key : bm.keySet()) {
            System.out.println(key + "=" + key);
        }

        final String function = "@Resource(name = \"ENT_REPO\")" + "\n"
                + GenerisktKodRepository.class.getSimpleName() + "<ENT_NAME> ENT_REPO;" + "\n    @Test"
                + "\n    @Rollback(false)"
                + "\n    public void loadENT_NAMEValuesIntoDb() throws FileNotFoundException {"
                + "\n        List<ENT_NAME> codes = toBeans(\"ENT_NAME\", ENT_NAME.class);"
                + "\n        for (ENT_NAME code : codes) {" + "\n            ENT_REPO.persist(code);"
                + "\n        }" + "\n        ENT_REPO.flush();" + "\n    }";

        for (String ent : getEntityNames()) {
            String entRepo = toRepoName(ent);
            String result = function.replace("ENT_NAME", ent);
            result = result.replace("ENT_REPO", entRepo);

            System.out.println("\n" + result);
        }

    }

    private static List<String> getEntityNames() {
        return EntityGeneratorTool.getEntityNames();
    }

    private static String toRepoName(String klass) {
        return EntityGeneratorTool.toRepoName(klass);
    }

}
