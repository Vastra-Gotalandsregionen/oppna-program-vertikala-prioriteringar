package se.vgregion.verticalprio;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;

/**
 * 
 * Use this 'test' to import data from flat file.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
@TransactionConfiguration(defaultRollback = false)
public class ImportTest {

    @Resource(name = "applicationData")
    ApplicationData applicationData;

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

    @Resource(name = "aatgaerdsKodRepository")
    GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;

    @Resource(name = "vaardformsKodRepository")
    GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;

    @Resource(name = "atcKodRepository")
    GenerisktKodRepository<AtcKod> atcKodRepository;

    @Resource(name = "prioRepository")
    PrioRepository prioRepository;

    @Test
    public void dummy() {
        Assert.assertTrue(true);
    }

    // @Test
    @Transactional()
    @Rollback(false)
    public void main() throws Exception {

        File file = new File("C:\\temp\\vp-import\\eye.txt");
        FileReader fr = new FileReader(file);
        int c = fr.read();
        StringBuffer sb = new StringBuffer();
        do {
            sb.append((char) c);
            c = fr.read();
        } while (c != -1);
        String[] rows = sb.toString().split("\\n");
        System.out.println("Antal rader " + rows.length);

        for (int i = 1; i < rows.length; i++) {
            String row = rows[i];
            handleRow(row);
        }

    }

    int hits;

    @Transactional
    private void handleRow(String row) {
        try {
            String[] values = row.split(Pattern.quote("|"));

            Prioriteringsobjekt prio = new Prioriteringsobjekt();
            prio.setDiagnoser(getItemsByKoder(applicationData.getDiagnosKodList(), values[0]));

            TillstaandetsSvaarighetsgradKod svaarighetsgradKod = getByKod(
                    applicationData.getTillstaandetsSvaarighetsgradKodList(), values[2]);
            prio.setTillstaandetsSvaarighetsgradKod(svaarighetsgradKod);

            prio.setIndikationGaf(values[3]);

            prio.setAatgaerdskoder(getItemsByKoder(applicationData.getAatgaerdsKodList(), values[4]));

            prio.setAatgaerdsRiskKod(getByKod(applicationData.getAatgaerdsRiskKodList(), values[6]));

            prio.setPatientnyttaEffektAatgaerdsKod(getByKod(
                    applicationData.getPatientnyttaEffektAatgaerdsKodList(), values[7]));

            prio.setPatientnyttoEvidensKod(getByKod(applicationData.getPatientnyttoEvidensKodList(), values[8]));

            prio.setQualy(toInt(values[9]));

            prio.setHaelsonekonomiskEvidensKod(getByKod(applicationData.getHaelsonekonomiskEvidensKodList(),
                    values[10]));

            prio.setVaentetidBesookVeckor(getByKod(applicationData.getVaentetidBesookVeckorList(), values[11]));

            prio.setVaentetidBehandlingVeckor(getByKod(applicationData.getVaentetidBehandlingVeckorList(),
                    values[12]));

            prio.setVaardnivaaKod(getByKod(applicationData.getVaardnivaaKodList(), values[13]));

            prio.setVaardform(getByKod(applicationData.getVaardformsKodList(), values[14]));

            // 15 rang enligt formel... ska inte hårdkodas in.

            prio.setRangordningsKod(getByKod(applicationData.getRangordningsKodList(), values[16]));

            if (values.length > 17) {
                prio.setKommentar(values[17]);
            }

            // mkPrioInsert(prio);

            prioRepository.store(prio);
            prioRepository.flush();

            TreeMap<String, Object> tm = new TreeMap<String, Object>(new BeanMap(prio));
            System.out.println(tm);

            hits++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(row);
        }

    }

    private Integer toInt(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }
        return Integer.parseInt(s);
    }

    private <T extends AbstractKod> T getByKod(List<T> codes, String kod) {
        if (codes.isEmpty()) {
            throw new RuntimeException();
        }
        for (T ak : codes) {
            if (kod.equals(ak.getKod())) {
                return ak;
            }
        }
        return null;
    }

    private <T extends AbstractKod> List<T> getItemsByKoder(List<T> codes, String kod) {
        kod = kod.replace(".", "");
        String[] frags = kod.split(Pattern.quote(","));
        List<T> result = new ArrayList<T>();

        for (String k : frags) {
            T item = getByKod(codes, k);
            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }

}
