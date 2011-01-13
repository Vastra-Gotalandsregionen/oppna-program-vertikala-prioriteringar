package se.vgregion.verticalprio.el;

import java.util.Collection;

import se.vgregion.verticalprio.controllers.EditDirective;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.User;

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

    public static String labelFor(Integer id, Collection<? extends AbstractKod> collection) {
        if (id == null || collection == null) {
            return "";
        }
        for (AbstractKod kod : collection) {
            if (kod.getId().longValue() == id.longValue()) {
                return kod.getLabel();
            }
        }
        return "";
    }

    public static Boolean canEdit(User user, EditDirective editDirective) {
        if (editDirective.getOverride() != null) {
            return editDirective.getOverride();
        }
        if (user == null) {
            return false;
        }
        Boolean editMode = editDirective.getEditable();
        if (editMode == null || !editMode) {
            return false;
        }
        return user.isEditor();
    }

}
