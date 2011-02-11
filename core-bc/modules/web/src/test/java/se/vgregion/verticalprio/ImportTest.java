package se.vgregion.verticalprio;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        File file = new File("C:\\temp\\vp-import\\onkologi.txt");
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

    private <T extends AbstractKod> T getByKod(List<T> codes, String kod) {
        kod = kod.replace(".", "");
        kod = kod.replace(" ", "");

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

    private <T extends AbstractKod> Set<T> getItemsByKoder(List<T> codes, String kod) {
        String[] frags = kod.split(Pattern.quote(","));
        Set<T> result = new HashSet<T>();

        for (String k : frags) {
            if (k.contains("-")) {
                result.addAll(getItemsByKodInterval(codes, k));
            } else {
                T item = getByKod(codes, k);
                if (item != null) {
                    result.add(item);
                }
            }
        }

        return result;
    }

    private <T extends AbstractKod> Set<T> getItemsByKodInterval(List<T> codes, String kod) {
        Set<T> result = new HashSet<T>();
        String[] fromTo = kod.split(Pattern.quote("-"));

        String charCode = fromTo[0].replaceAll("[0-9]", "");
        int start = toInt(fromTo[0]);
        int end = toInt(fromTo[1]);

        for (int i = start; i <= end; i++) {
            String code = charCode;
            if (i < 10) {
                code += "0";
            }
            code += i;
            T item = getByKod(codes, code);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    private Integer toInt(String s) {
        if (s == null) {
            return null;
        }
        s = s.replaceAll("[^0-9]", "");
        while (s.startsWith("0")) {
            s = s.substring(1);
        }
        if ("".equals(s)) {
            return null;
        }
        return Integer.parseInt(s);
    }

}
