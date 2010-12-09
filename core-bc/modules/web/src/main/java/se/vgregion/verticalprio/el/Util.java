package se.vgregion.verticalprio.el;

import java.util.Collection;

/**
 * A utility class with string functions that could come in handy inside el-expressions.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class Util {

    public static String concat(String first, String second) {
        return first + second;
    }

    public static boolean contains(Collection<?> collection, Object item) {
        return collection.contains(item);
    }

}
