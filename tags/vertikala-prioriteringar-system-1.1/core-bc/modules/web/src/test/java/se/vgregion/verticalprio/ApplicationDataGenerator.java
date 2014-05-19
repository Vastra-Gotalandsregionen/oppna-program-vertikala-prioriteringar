package se.vgregion.verticalprio;

import se.vgregion.verticalprio.entity.EntityGeneratorTool;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class ApplicationDataGenerator {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String function = "    @Resource(name = \"REPO\")" + "\n    GenerisktKodRepository<TYPE> REPO;"
                + "\n    List<TYPE> everyTYPE;" + "\n    public List<TYPE> getEveryTYPE() {"
                + "\n        if (everyTYPE == null) {"
                + "\n            everyTYPE = new ArrayList<TYPE>(REPO.findAll());" + "\n        }"
                + "\n        return everyTYPE;" + "\n    }";

        for (String type : EntityGeneratorTool.getEntityNames()) {
            String repo = EntityGeneratorTool.toRepoName(type);
            String result = function.replace("TYPE", type);
            result = result.replace("REPO", repo);
            System.out.println(result);
        }

    }

}
