package se.vgregion.verticalprio.el;

import java.util.Collection;

/**
 * A utility class with string functions that could come in handy inside el-expressions.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class Util {

    public static final String CELL_SEPARATOR = ", ";

    public static String concat(String first, String second) {
        return first + second;
    }

    public static boolean contains(Collection<?> collection, Object item) {
        return collection.contains(item);
    }

    public static boolean isCollection(Object item) {
        return item instanceof Collection;
    }

    public static String cellToString(Object obj) {
        if (isCollection(obj)) {
            // lets "explode" the collection
            Collection<?> col = (Collection<?>) obj;
            if (!col.isEmpty()) {
                StringBuilder buf = new StringBuilder();
                for (Object cell : col) {
                    buf.append(cell);
                    buf.append(CELL_SEPARATOR);
                }
                buf.delete(buf.length() - CELL_SEPARATOR.length(), buf.length());
                return buf.toString();
            } else {
                // the collection was empty so let´s take care of that
                return "";
            }
        }
        return String.valueOf(obj);
    }

    public static Collection toCollection(Object obj) {
        return (Collection) obj;
    }

}
