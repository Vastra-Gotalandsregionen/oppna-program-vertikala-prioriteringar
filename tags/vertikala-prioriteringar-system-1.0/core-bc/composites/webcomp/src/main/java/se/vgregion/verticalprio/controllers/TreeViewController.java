package se.vgregion.verticalprio.controllers;

import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TreeViewController {

	@RequestMapping(value = { "treeView" })
	public String render(@RequestParam Integer id, @RequestParam String allObjects) {

		return "treeView";
	}

	private String toByteText(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (byte b : bytes) {
			sb.append(b + "b");
		}
		return sb.toString();
	}

	private byte[] toBytes(String byteText) {
		String[] frags = byteText.split(Pattern.quote("b"));
		byte[] result = new byte[frags.length];
		int i = 0;
		for (String frag : frags) {
			result[i] = Byte.parseByte(frag);
			i++;
		}
		return result;
	}

}
