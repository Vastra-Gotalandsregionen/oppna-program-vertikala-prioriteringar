package se.vgregion.verticalprio.util;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class TextToBeanConverterTest {

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.util.TextToBeanConverter#load(java.lang.String, java.lang.Class)}.
     */
    @Test
    public final void loadWithStringClassArgs() {
        String testData = "name;age;weight;income" + "\nJohn;44;88.5;1001" + "\nSahra;101;66;null"
                + "\nKarl;22;77;24000";
        load(testData);
    }

    /**
     * Test method for
     * {@link se.vgregion.verticalprio.util.TextToBeanConverter#load(java.io.InputStream, java.lang.Class)}.
     */
    @Test
    public final void loadWithInputStreamClassArgs() {
        InputStream is = getClass().getResourceAsStream("/Person.data");
        load(is);
    }

    private final void load(InputStream is) {
        TextToBeanConverter converter = new TextToBeanConverter();
        List<Person> persons = converter.load(is, Person.class);
        assertResult(persons);
    }

    private final void load(String testData) {
        TextToBeanConverter converter = new TextToBeanConverter();
        List<Person> persons = converter.load(testData, Person.class);
        assertResult(persons);
    }

    private void assertResult(List<Person> persons) {
        Assert.assertNotNull(persons);
        Assert.assertEquals(3, persons.size());
        Person john = persons.get(0), sahra = persons.get(1), karl = persons.get(2);
        assertPerson(john, "John", 44, (float) 88.5, (long) 1001);
        assertPerson(sahra, "Sahra", 101, (float) 66, null);
        assertPerson(karl, "Karl", 22, (float) 77, (long) 24000);
    }

    private void assertPerson(Person person, String name, Integer age, Float weight, Long income) {
        Assert.assertEquals(age, person.getAge());
        Assert.assertEquals(weight, person.getWeight());
        Assert.assertEquals(income, person.getIncome());
    }

    public static class Person {
        String name;
        Integer age;
        Float weight;
        Long income;
        Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Float getWeight() {
            return weight;
        }

        public void setWeight(Float weight) {
            this.weight = weight;
        }

        public Long getIncome() {
            return income;
        }

        public void setIncome(Long income) {
            this.income = income;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

}
