package tests;

import org.alessio29.savagebot.internal.Messages;
import org.junit.Test;

import java.util.regex.Pattern;

public class TestRegExp {

	private static final String BLOCK_PATTERN = ".*\\s*```(.+\n*)+```.*\\s*";
	private Pattern blockPattern = Pattern.compile(BLOCK_PATTERN);
	
	
	@Test
	public void test () {
		
		String line1 = "```This is Line```";
		String line2 = "```This is Line\n```";
		String line3 = "```This is Line\n Second line```";
		String s = "����� ��������\n```�� ����� ���� ��� ~r d6\n����� ������� �������```\n�� ����� ~r d4";
		String unclosed = "����� ��������\n```�� ����� ���� ��� ~r d6\n����� ������� �������\n�� ����� ~r d4";
		
//		System.out.println(blockPattern.matcher(line1).matches());
//		System.out.println(blockPattern.matcher(line2).matches());
//		System.out.println(blockPattern.matcher(line3).matches());
//		System.out.println(blockPattern.matcher(s).matches());
		
		System.out.println(Messages.removeBlocks(s));
//		System.out.println(Messages.removeBlocks(unclosed));
		
		
	}
}
