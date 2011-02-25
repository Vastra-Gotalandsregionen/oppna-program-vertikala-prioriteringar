package se.vgregion.verticalprio.el;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import se.vgregion.verticalprio.controllers.EditDirective;
import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;

/**
 * A utility class with string functions that could come in handy inside el-expressions.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class Util {

    private static DecimalFormat df = new DecimalFormat("#.##");

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
        return "-";
    }

    public static Boolean canEdit(User user, EditDirective editDirective) {
        if (editDirective == null) {
            return false;
        }
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

    public static String toOptions(Long id, List<AbstractKod> items) {
        if (items == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean haveChildren = (items.size() > 0 && items.get(0) instanceof AbstractHirarkiskKod);
        toOptions(id, 0, items, sb, haveChildren, null);
        return sb.toString();
    }

    public static String toRaadOptions(Long id, List<AbstractKod> items, List<SektorRaad> raad) {
        StringBuilder sb = new StringBuilder();
        toOptions(id, 0, items, sb, true, toList(raad));
        return sb.toString();
    }

    private static List<Long> toList(Collection<SektorRaad> raad) {
        List<Long> result = new ArrayList<Long>();
        for (SektorRaad sr : raad) {
            result.add(sr.getId());
        }
        return result;
    }

    @Transactional
    private static void toOptions(Long id, int level, List<? extends AbstractKod> items, StringBuilder sb,
            boolean haveChildren, List<Long> enableOnlyThese) {
        if (items == null) {
            return;
        }
        for (AbstractKod item : items) {
            sb.append("<option value='");
            sb.append(item.getId().toString());
            sb.append("'");
            if (id.longValue() == item.getId().longValue()) {
                sb.append(" selected='selected'");
            }

            if (enableOnlyThese != null) {
                if (!enableOnlyThese.contains(item.getId())) {
                    sb.append(" disabled='disabled'");
                }
            }

            sb.append(">");
            if (haveChildren) {
                for (int i = 0; i < level; i++) {
                    sb.append("&nbsp;&nbsp;");
                }
            }
            sb.append(item.getLabel());
            sb.append("</option>");
            if (haveChildren) {
                AbstractHirarkiskKod<? extends AbstractKod> ahk = (AbstractHirarkiskKod) item;
                toOptions(id, level + 1, ahk.getChildren(), sb, true, enableOnlyThese);
            }
        }
    }

    public static String toString(Object o) {
        if (o instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            @SuppressWarnings("rawtypes")
            Collection c = (Collection) o;
            for (Object i : c) {
                if (i == null) {
                    continue;
                }
                sb.append("* ");
                sb.append(i);
                sb.append(" \n");
            }
            return sb.toString();
        }
        if (o == null) {
            return "";
        }
        return o + "";
    }

    public static String toStringDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String toUpperCase(String s) {
        if (s == null) {
            return "";
        }
        return s.toUpperCase();
    }

    /**
     * Should be used to format values printed in the result table.
     * 
     * @return
     */
    public static String toCellText(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof Date) {
            Date d = (Date) o;
            return toStringDate(d);
        }
        if (o instanceof Double) {
            Double d = (Double) o;
            return df.format(d);
        }
        if (o instanceof SektorRaad) {
            SektorRaad sr = (SektorRaad) o;
            return toString(sr);
        }
        if (o instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            Collection c = (Collection) o;
            for (Object item : c) {
                sb.append("<div>");
                sb.append(toCellText(item));
                sb.append("</div>");
            }
            return sb.toString();
        } else if (o instanceof AbstractKod) {
            AbstractKod ak = (AbstractKod) o;
            return ak.getKod();
        } else {
            return o.toString();
        }

    }

    private static String toString(SektorRaad sr) {
        if (sr.getParent() == null) {
            return sr.getLabel();
        }
        return toString(sr.getParent());
    }
}
