package se.vgregion.verticalprio.entity;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class EntityGeneratorTool {
    public static List<String> getEntityNames() {
        List<String> result = new ArrayList<String>();
        URL url = TestVaardformsKod.class.getResource(VaardformsKod.class.getSimpleName() + ".class");
        // System.out.println(url);
        String path = url.toString().replace("file:/", "");
        File file = new File(path);
        file = file.getParentFile();
        // System.out.println(file.getAbsolutePath());

        for (String item : file.list()) {
            // System.out.println(item);
            if (item.endsWith("Kod.class") && !item.startsWith("Abstract")) {
                String klass = item.replace(".class", "");
                result.add(klass);
            }
        }
        return result;
    }

    public static String toRepoName(String klass) {
        klass = klass.substring(0, 1).toLowerCase() + klass.substring(1, klass.length());
        klass += "Repository";
        return klass;
    }

    public static File getFileByNameOnClassPath(String name) {
        URL url = EntityGeneratorTool.class.getResource(name);
        if (url == null) {
            return null;
        }
        String path = url.toString().replace("file:/", "");
        return new File(path);
    }
}
