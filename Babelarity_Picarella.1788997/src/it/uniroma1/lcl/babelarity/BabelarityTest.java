package it.uniroma1.lcl.babelarity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BabelarityTest {

	private static final Path DOCUMENTS = Paths.get("resources/documents");
	private MiniBabelNet miniBabelNet;
	private CorpusManager documentManager;

	@BeforeAll
	public void setup() {
		try {
			miniBabelNet = MiniBabelNet.getInstance();
			documentManager = CorpusManager.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NO");
		}
	}

	@Test
	public void testMiniBabelNet() {
		Synset synset = miniBabelNet.getSynset("bn:00081546n");
		String summary = miniBabelNet.getSynsetSummary(synset);
		String[] split = summary.split("\t");
		String id = split[0];
		String pos = split[1];
		String[] lemmi = split[2].split(";");
		String[] glosse = split[3].split(";");
		String[] relations = split[4].split(";");
		Arrays.sort(relations);
		assertTrue(lemmi[0].equals("word") && lemmi.length == 1);
		assertEquals(id, "bn:00081546n");
		assertEquals(pos, "NOUN");
		assertTrue(glosse.length == 6);
		assertTrue(relations.length == 749);
		assertEquals(relations[2], "bn:00000239n_has-kind");
	}

	@Test
	public void testDocumentManager() {
		Document document = documentManager.parseDocument(DOCUMENTS.resolve("Cultural_tourism.txt"));
		assertEquals(document.getTitle(), "Cultural tourism");
		assertEquals(document.getContent().substring(0, 107),
				"Cultural tourism (or culture tourism) is the subset of tourism concerned with a country or region's culture");
		Document document1 = documentManager.parseDocument(DOCUMENTS.resolve("programming_language.txt"));
		assertTrue(!document.getId().equals(document1.getId()));
		documentManager.saveDocument(document);
		assertEquals(document, documentManager.loadDocument(document.getId()));
	}
	
	@Test
	public void testLexicalSimilarityIdentity()
	{
		testSimilarityIdentity(Word.fromString("test"));
	}

	@Test
	public void testLexicalSimilarity1() {
		String word1 = "test";
		String word2 = "exam";
		String word3 = "pop";
		String word4 = "rock";
		similarityTest(Word.fromString(word1), Word.fromString(word2), Word.fromString(word3), Word.fromString(word4));
	}
	
	@Test
	public void testLexicalSimilarity2() {
		String word1 = "port";
		String word2 = "ship";
		String word3 = "fear";
		String word4 = "emotion";
		similarityTest(Word.fromString(word1), Word.fromString(word2), Word.fromString(word3), Word.fromString(word4));
	}
	
	@Test
	public void testSemanticSimilarityIdentity()
	{
		Synset synset1 = miniBabelNet.getSynset("bn:00036821n");
		testSimilarityIdentity(synset1);
	}

	@Test
	public void testSemanticSimilarity1() {
		Synset s1 = miniBabelNet.getSynset("bn:00034472n");
		Synset s2 = miniBabelNet.getSynset("bn:00015008n");
		Synset s3 = miniBabelNet.getSynset("bn:00081546n");
		Synset s4 = miniBabelNet.getSynset("bn:00070528n");
		similarityTest(s1, s2, s3, s4);
	}
	
	@Test
	public void testSemanticSimilarity2() {
		Synset s1 = miniBabelNet.getSynset("bn:00024712n");
		Synset s2 = miniBabelNet.getSynset("bn:00029345n");
		Synset s3 = miniBabelNet.getSynset("bn:00035023n");
		Synset s4 = miniBabelNet.getSynset("bn:00010605n");
		similarityTest(s1, s2, s3, s4);
	}
	
	@Test
	public void testDocumentSimilarity1() {
		Document d1 = documentManager.parseDocument(DOCUMENTS.resolve("C_programming_language.txt"));
		Document d2 = documentManager.parseDocument(DOCUMENTS.resolve("Java_programming_language.txt"));
		Document d3 = documentManager.parseDocument(DOCUMENTS.resolve("Cristiano_Ronaldo.txt"));
		Document d4 = documentManager.parseDocument(DOCUMENTS.resolve("Thomas_Muller.txt"));
		similarityTest(d1, d2, d3, d4);
	}

	@Test
	public void testDocumentSimilarity2() {
		Document d1 = documentManager.parseDocument(DOCUMENTS.resolve("Eugenio_Montale.txt"));
		Document d2 = documentManager.parseDocument(DOCUMENTS.resolve("Umberto_Eco.txt"));
		Document d3 = documentManager.parseDocument(DOCUMENTS.resolve("Tourism_in_the_Netherlands.txt"));
		Document d4 = documentManager.parseDocument(DOCUMENTS.resolve("Cultural_tourism.txt"));
		similarityTest(d1, d2, d3, d4);
	}

	@Test
	public void testDocumentSimilarity3() {
		Document d1 = documentManager.parseDocument(DOCUMENTS.resolve("Council_of_the_European_Union.txt"));
		Document d2 = documentManager.parseDocument(DOCUMENTS.resolve("European_Union_law.txt"));
		Document d3 = documentManager.parseDocument(DOCUMENTS.resolve("Java_virtual_machine.txt"));
		Document d4 = documentManager.parseDocument(DOCUMENTS.resolve("Java_programming_language.txt"));
		similarityTest(d1, d2, d3, d4);
	}
	
	public void testSimilarityIdentity(LinguisticObject o1) {
		double sim0 = miniBabelNet.computeSimilarity(o1, o1);
		assertTrue(Double.compare(sim0, 1.0) == 0);
	}
	
	public void similarityTest(LinguisticObject o1, LinguisticObject o2, LinguisticObject o3, LinguisticObject o4) {
		double sim1 = miniBabelNet.computeSimilarity(o1, o2);
		double sim2 = miniBabelNet.computeSimilarity(o3, o4);
		double sim3 = miniBabelNet.computeSimilarity(o1, o3);
		double sim4 = miniBabelNet.computeSimilarity(o2, o4);
		assertTrue(sim1 > sim3 && sim2 > sim4);
	}

}
