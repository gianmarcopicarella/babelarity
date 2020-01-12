package it.uniroma1.lcl.babelarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import it.uniroma1.lcl.babelarity.exception.LemmaNotFoundException;
import it.uniroma1.lcl.babelarity.math.Matrix;
import it.uniroma1.lcl.babelarity.math.MirrorFloatMatrix;
import it.uniroma1.lcl.babelarity.math.MirrorIntegerMatrix;
import it.uniroma1.lcl.babelarity.math.SparseFloatMatrix;

import static java.util.stream.Collectors.toList;
/**
 * Implementazione della similarità lessicale avanzata estendendo la classe SimilarityObject 
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class BabelLexicalSimilarity extends SimilarityObject<Word> {
	private Set<String> vocabulary;
	private Map<String, Integer> indexMap;
	private SparseFloatMatrix pmi;
	private int documentCounter;
	/**
	 * Inizializza la similarità
	 * @param network Rete Semantica da utilizzare
	 */
	public BabelLexicalSimilarity(SemanticNetwork network) {
		super(network);
		vocabulary = new HashSet<String>();
		indexMap = new HashMap<String, Integer>();
	}
	
	private Matrix<Integer> computeCooccurrenceMatrix(Stream<List<Integer>> filteredDocs) {
		Matrix<Integer> m = new MirrorIntegerMatrix(vocabulary.size(), vocabulary.size());
		filteredDocs.forEach(lst -> {
			documentCounter++;
			for(int i = 0; i < lst.size(); i++) {
				int k = i;
				while(++k < lst.size()) m.increment(lst.get(k),  lst.get(i), 1);
			}	
		});
		return m;
	}
	
	private Matrix<Float> computePmiMatrix(Matrix<Integer> cooccurrences, List<Integer> indices, Map<Integer, Integer> occ) {
		Matrix<Float> mat = new MirrorFloatMatrix(indices.size(), indices.size());
		for(int i = 0; i < indices.size(); i++) {
			int k = i;
			float dena = (float)occ.get(i) / documentCounter;
			while(++k < indices.size()) {
				mat.set(k, i, (float) Math.max(0, Math.log(((float)cooccurrences.get(k, i) / documentCounter) / (dena * ((float)occ.get(k) / documentCounter)))));
			}
		}
		return mat;
	}
	
	private void wordChecker(String word, Set<String> documentWords, Map<String, Integer> occurrences) {
		if(Word.stopWords.contains(word)) return;
		List<String> lemma = network.getLemmas(word);
		String data = null;
		if(lemma != null) data = lemma.get(0);
		else if(network.getLemmas().contains(word)) data = word;
		if(data != null && !documentWords.contains(data)) {
			occurrences.put(data, 1 + occurrences.getOrDefault(data, 0));
			documentWords.add(data);
		}
	}
	
	private List<Set<String>> filterCorpus(Map<String, Integer> occurrences){
		List<Set<String>> filteredDocs = new ArrayList<Set<String>>();
		for(String arr : CorpusManager.getInstance()) {
			HashSet<String> documentWords = new HashSet<>();
			for(String word : arr.replaceAll("\\W", " ").toLowerCase().split(" "))
				wordChecker(word.trim(), documentWords, occurrences);
			vocabulary.addAll(documentWords);
			filteredDocs.add(documentWords);
		}
		vocabulary.removeIf(s -> occurrences.get(s) < 10);
		for(String s : vocabulary) indexMap.put(s, indexMap.size());
		return filteredDocs;
	}
	/**
	 * Restituisce il vocabolario dei lemmi presenti nel corpus
	 * @return Set rappresentante il vocabolario dei lemmi presenti nel corpus
	 */
	public Set<String> getVocabulary(){
		return this.vocabulary;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double computeSimilarity(Word w1, Word w2) {
		if(this.pmi == null) {
			Map<String, Integer> occurrences = new HashMap<>();
			Matrix<Integer> cooccurrences = computeCooccurrenceMatrix(filterCorpus(occurrences).parallelStream()
					.map(s -> s.stream().filter(v -> vocabulary.contains(v)).map(indexMap::get).collect(toList())));
			Map<Integer, Integer> indexOccurrences = new HashMap<Integer, Integer>();
			occurrences.entrySet().forEach(e -> indexOccurrences.put(indexMap.get(e.getKey()), e.getValue()));
			this.pmi = new SparseFloatMatrix(computePmiMatrix(cooccurrences, 
					vocabulary.stream().map(indexMap::get).collect(toList()), indexOccurrences));
			cooccurrences = null;
		}
		
		List<String> s1 = w1.getLemmas(), s2 = w2.getLemmas();
		try {
			if(s1 == null || s2 == null)
				throw new LemmaNotFoundException();
		} catch (LemmaNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return pmi.get(indexMap.get(s1.get(0))).cosineSimilarityWith(pmi.get(indexMap.get(s2.get(0))));
	}	
}