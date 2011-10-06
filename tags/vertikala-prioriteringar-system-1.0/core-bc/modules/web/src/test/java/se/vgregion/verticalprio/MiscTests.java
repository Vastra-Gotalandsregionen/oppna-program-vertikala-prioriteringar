package se.vgregion.verticalprio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import org.junit.Test;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

public class MiscTests {

	@Test
	public void main() throws Exception {

		Prioriteringsobjekt prio = new Prioriteringsobjekt();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(prio);
		oos.flush();

		String text = toString(baos.toByteArray());

		byte[] bytes = toBytes(text);

		System.out.println("The String " + new String(bytes));

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		prio = (Prioriteringsobjekt) ois.readObject();
		System.out.println("Resultat " + prio);
	}

	private String toString(byte[] bytes) throws IOException {
		// ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		System.out.println("toString bytes.length " + bytes.length);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");

		int count = 0;
		for (byte b : bytes) {
			osw.write(b);
			count++;
		}
		System.out.println("toString " + count);

		return new String(baos.toByteArray());
	}

	private byte[] toBytes(String s) throws IOException {

		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());

		InputStreamReader isr = new InputStreamReader(bais, "UTF8");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int c = isr.read();
		int count = 0;
		while (c != -1) {
			baos.write((char) c);
			c = isr.read();
			count++;
		}
		System.out.println("Längden på " + count);

		return baos.toByteArray();
	}

}
