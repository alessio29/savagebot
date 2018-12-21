package tests;

import java.util.regex.Pattern;

import org.junit.Test;

import com.github.alessio29.savagebot.internal.Messages;

public class TestRegExp {

	private static final String BLOCK_PATTERN = ".*\\s*```(.+\n*)+```.*\\s*";
	private Pattern blockPattern = Pattern.compile(BLOCK_PATTERN);
	
	
	@Test
	public void test () {
		
		String line1 = "```This is Line```";
		String line2 = "```This is Line\n```";
		String line3 = "```This is Line\n Second line```";
		String s = "перед зеркалом\n```по щачлу себя бей ~r d6\nперед матерью подруги```\nне робей ~r d4";
		String unclosed = "перед зеркалом\n```по щачлу себя бей ~r d6\nперед матерью подруги\nне робей ~r d4";
		
//		System.out.println(blockPattern.matcher(line1).matches());
//		System.out.println(blockPattern.matcher(line2).matches());
//		System.out.println(blockPattern.matcher(line3).matches());
//		System.out.println(blockPattern.matcher(s).matches());
		
		System.out.println(Messages.removeBlocks(s));
//		System.out.println(Messages.removeBlocks(unclosed));
		
		
	}
}
