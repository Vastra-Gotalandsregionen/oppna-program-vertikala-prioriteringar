package se.vgregion.verticalprio;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class Util {

    /**
     * Copies all properties from source object to target object.
     * 
     * @param source
     * @param target
     */
    public static void copyValuesFromBeanToBean(Object source, Object target) {
        new BeanMap(target).putAllWriteable(new BeanMap(source));
    }

    /**
     * Copies all properties from source object to target object. When a Set object is encountered, though, a new
     * instance of the type {@link HashSet} is created and the content from the target object is moved to that one
     * before being set into target.
     * 
     * @param source
     * @param target
     */
    public static void copyValuesAndSetsFromBeanToBean(Object source, Object target) {
        BeanMap sourceMap = new BeanMap(source);
        BeanMap targetMap = new BeanMap(target);

        new BeanMap(target).putAllWriteable(new BeanMap(source));

        for (Object key : targetMap.keySet()) {
            Object value = sourceMap.get(key);
            if (targetMap.getWriteMethod((String) key) != null) {
                if (value instanceof Set) {
                    Set newSet = new HashSet();
                    Set oldSet = (Set) value;
                    newSet.addAll(oldSet);
                    value = newSet;
                }

                targetMap.put(key, value);
            }
        }
    }

}
