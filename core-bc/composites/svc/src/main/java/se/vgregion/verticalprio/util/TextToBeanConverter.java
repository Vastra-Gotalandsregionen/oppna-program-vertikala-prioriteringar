package se.vgregion.verticalprio.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.Transformer;

/**
 * Utility to read semicolon (or other sign/pattern) text mass or file and convert this to a list of pojo beans.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class TextToBeanConverter {

    private String valueDelimiterExpr = Pattern.quote(";");

    private String rowDelimiterExpr = Pattern.quote("\n");

    public TextToBeanConverter() {
    }

    /**
     * Creates a list of objects from a text. The text must start with a row of columns, property names, matching
     * the names of the properties in the bean class that is to be created. The number of columns must match the
     * number of values of each row.
     * 
     * @param <T>
     *            Generic type inferred by the provided klass argument.
     * @param text
     *            Text containing the values of each bean.
     * @param klass
     *            Type of beans to be created.
     * @return Bunch of objects, beans, created from the text mass.
     */
    public <T> List<T> load(String text, Class<T> klass) {
        return loadImplTurnExcemptionToRuntime(text, klass);
    }

    private String toString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c = is.read();
        while (c != -1) {
            sb.append((char) c);
            c = is.read();
        }
        return sb.toString();
    }

    /**
     * Same as the
     * {@link se.vgregion.verticalprio.util.TextToBeanConverter#load(java.lang.String, java.lang.Class)} method but
     * with an inputstream to the text.
     * 
     * @param <T>
     *            See the other method mentioned.
     * @param is
     *            stream containing the text to be used as datasource for the beans.
     * @param klass
     *            See the other method mentioned.
     * @return See the other method mentioned.
     */
    public <T> List<T> load(InputStream is, Class<T> klass) {
        return loadImplTurnExcemptionToRuntime(is, klass);
    }

    private <T> List<T> loadImplTurnExcemptionToRuntime(InputStream is, Class<T> klass) {
        try {
            String text = toString(is);
            return loadImpl(text, klass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> loadImplTurnExcemptionToRuntime(String text, Class<T> klass) {
        try {
            return loadImpl(text, klass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> loadImpl(String text, Class<T> klass) throws InstantiationException,
            IllegalAccessException {
        List<T> result = new ArrayList<T>();

        List<String> rows = splitWithNoEmptyValues(text, rowDelimiterExpr);
        if (rows.isEmpty()) {
            return result;
        }
        List<String> propertyNames = splitWithNoEmptyValues(rows.remove(0), valueDelimiterExpr);

        int rowCount = 0;

        for (String row : rows) {
            T bean = klass.newInstance();

            List<String> values = splitWithNoEmptyValues(row, valueDelimiterExpr);
            if (values.size() != propertyNames.size()) {
                String msg = "Row have " + values.size() + " values, but the table have " + propertyNames.size()
                        + " columns.";
                msg += "\n Columns " + propertyNames + " \n Values " + values;
                throw new RuntimeException(msg);
            }

            BeanMap bm = new BeanMap(bean);

            int columnCount = 0;
            Object value = null;
            for (String key : propertyNames) {
                try {
                    value = values.get(columnCount++);
                    if ("null".equals(value)) {
                        bm.put(key, null);
                    } else {
                        value = convert(bm.getType(key), value);
                        bm.put(key, value);
                    }
                } catch (Exception ncdfe) {
                    System.out.println("Error for property " + key + " of " + klass.getCanonicalName()
                            + " value was " + value);
                    ncdfe.printStackTrace();
                }
            }

            result.add(bean);
            rowCount++;
        }

        return result;
    }

    private List<String> splitWithNoEmptyValues(String text, String expr) {
        List<String> result = new ArrayList<String>();
        for (String item : text.split(expr)) {
            if (item != null && !"".equals(item.trim())) {
                result.add(item.trim());
            }
        }
        return result;
    }

    public void setValueDelimiterExpr(String valueDelimiterExpr) {
        this.valueDelimiterExpr = valueDelimiterExpr;
    }

    public String getValueDelimiterExpr() {
        return valueDelimiterExpr;
    }

    public void setRowDelimiterExpr(String rowDelimiterExpr) {
        this.rowDelimiterExpr = rowDelimiterExpr;
    }

    public String getRowDelimiterExpr() {
        return rowDelimiterExpr;
    }

    private Object convert(Class<?> klass, Object value) {
        if (value == null) {
            return null;
        }
        Transformer transformer = transformers.get(klass);
        if (transformer == null) {
            return value;
        }
        return transformer.transform(value);
    }

    /**
     * Transformers for the Object wrapper types of the primitives.
     */
    private static final Map<Class<?>, Transformer> transformers = createTypeTransformers();

    private static Map<Class<?>, Transformer> createTypeTransformers() {
        Map<Class<?>, Transformer> defaultTransformers = new HashMap<Class<?>, Transformer>();
        defaultTransformers.put(Boolean.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Boolean.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return new Character(input.toString().charAt(0));
            }
        });
        defaultTransformers.put(Byte.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Byte.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Short.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Short.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Integer.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Integer.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Long.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Long.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Float.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Float.valueOf(input.toString());
            }
        });
        defaultTransformers.put(Double.class, new Transformer() {
            @Override
            public Object transform(Object input) {
                return Double.valueOf(input.toString());
            }
        });
        return defaultTransformers;
    }

}
