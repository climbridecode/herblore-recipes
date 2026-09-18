package com.herblorerecipes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Compares output against a checked-in file. Run the tests with the environment variable
 * UPDATE_GOLDEN=1 to (re)write the files instead of comparing, then review the diff in git.
 */
final class GoldenFile
{

	private static final Path DIR = Paths.get("src/test/resources/golden");

	private GoldenFile()
	{
	}

	static void check(String name, List<String> actual) throws IOException
	{
		Path file = DIR.resolve(name + ".txt");
		if ("1".equals(System.getenv("UPDATE_GOLDEN")))
		{
			Files.createDirectories(DIR);
			Files.write(file, actual);
			return;
		}
		if (!Files.exists(file))
		{
			fail("Missing golden file " + file + ". Run the tests with UPDATE_GOLDEN=1 to create it.");
		}

		List<String> expected = Files.readAllLines(file);
		for (int i = 0; i < Math.min(expected.size(), actual.size()); i++)
		{
			if (!expected.get(i).equals(actual.get(i)))
			{
				fail(String.format("%s differs at line %d (run with UPDATE_GOLDEN=1 to accept the change):%n  expected: %s%n  actual:   %s",
					name, i + 1, expected.get(i), actual.get(i)));
			}
		}
		assertEquals(name + ": number of lines", expected.size(), actual.size());
	}
}
