package se.vgregion.verticalprio.el;

/**
 * A utility class with string functions that could come in handy inside el-expressions.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class StringUtil {

    public static String concat(String first, String second) {
        /*
         * StringBuilder sb = new StringBuilder(); for (String subText : subTexts) { sb.append(subText); } return
         * sb.toString();
         */
        return first + second;
    }

}
