package se.vgregion.verticalprio.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import se.vgregion.verticalprio.repository.VaardformsKodRepository;
import se.vgregion.verticalprio.util.TextToBeanConverter;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@ContextConfiguration("classpath:testApplicationContext.xml")
public class TestVaardformsKod extends AbstractTransactionalJUnit4SpringContextTests {
    private static final Log log = LogFactory.getLog(TestVaardformsKod.class);

    @Resource(name = "vaardformsKodRepository")
    VaardformsKodRepository vaardformsKodRepository;

    @Test
    @Rollback(false)
    public void load() {
        VaardformsKod a = new VaardformsKod();
        a.setCode("labelcode");
        a.setDescription("description");
        vaardformsKodRepository.persist(a);
        vaardformsKodRepository.flush();

    }

    private <T> List<T> toBeans(String entityName, Class<T> klass) throws FileNotFoundException {
        File file = getFileByNameOnClassPath("/dbLoad/" + entityName + ".data");
        TextToBeanConverter converter = new TextToBeanConverter();
        List<T> codes = converter.load(new FileInputStream(file), klass);
        return codes;
    }

    @Test
    @Rollback(false)
    public void loadCodeTables() throws FileNotFoundException {
        List<VaardformsKod> codes = toBeans("VaardformsKod", VaardformsKod.class);
        for (VaardformsKod code : codes) {
            vaardformsKodRepository.persist(code);
        }
        vaardformsKodRepository.flush();
    }

    private File getFileByNameOnClassPath(String name) {
        URL url = getClass().getResource(name);
        String path = url.toString().replace("file:/", "");
        return new File(path);
    }

    /**
     * Generates the repository classes for *Kod -classes. Puts the *.java classes in c:\temp\
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        URL url = TestVaardformsKod.class.getResource(VaardformsKod.class.getSimpleName() + ".class");
        System.out.println(url);
        String path = url.toString().replace("file:/", "");
        File file = new File(path);
        file = file.getParentFile();
        System.out.println(file.getAbsolutePath());

        String packageText = "package se.vgregion.verticalprio.repository;\n";
        String imports = "import se.vgregion.dao.domain.patterns.repository.Repository;"
                + "\nimport se.vgregion.verticalprio.entity.DiagnosKod;\n"
                + "import se.vgregion.verticalprio.entity.NAME;";
        String interfaceText = packageText + imports
                + "public interface NAMERepository extends Repository<NAME, Long> {}";

        String tempDir = "C:\\temp\\";

        for (String item : file.list()) {
            System.out.println(item);
            if (item.endsWith("Kod.class")) {
                String klass = item.replace(".class", "");
                File icf = new File(tempDir + klass + "Repository.java");
                icf.createNewFile();
                FileWriter fw = new FileWriter(icf);
                String resultingInterface = interfaceText.replace("NAME", klass);
                fw.write(resultingInterface);
                fw.flush();
                fw.close();
            }
        }

        String implText = "package se.vgregion.verticalprio.repository;"
                + "\nimport org.springframework.stereotype.Repository;"
                + "\nimport se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;"
                + "\nimport se.vgregion.verticalprio.entity.NAME;"
                + "\nimport se.vgregion.verticalprio.repository.PrioRepository;"
                + "\n@Repository"
                + "\npublic class JpaNAMERepository extends DefaultJpaRepository<NAME> implements NAMERepository {}";

        for (String item : file.list()) {
            System.out.println(item);
            if (item.endsWith("Kod.class")) {
                String klass = item.replace(".class", "");
                File icf = new File(tempDir + "Jpa" + klass + "Repository.java");
                icf.createNewFile();
                FileWriter fw = new FileWriter(icf);
                String resultingInterface = implText.replace("NAME", klass);
                fw.write(resultingInterface);
                fw.flush();
                fw.close();
            }
        }

        String confRow = "\n<bean id=\"VARRepository\" class=\"se.vgregion.verticalprio.repository.JpaNAMERepository\"/>";

        File icf = new File(tempDir + "hmm.xml");
        icf.createNewFile();
        FileWriter fw = new FileWriter(icf);

        for (String item : file.list()) {
            System.out.println(item);
            if (item.endsWith("Kod.class")) {
                String klass = item.replace(".class", "");
                String row = confRow.replace("NAME", klass);
                String var = klass.substring(0, 1).toLowerCase() + klass.substring(1, klass.length());
                row = row.replace("VAR", var);
                fw.write(row);
                fw.flush();
            }
        }
        fw.close();

    }

}
