package it.uniroma1.lcl.babelarity;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.uniroma1.lcl.babelarity.exception.SimilarityNotFoundException;
import it.uniroma1.lcl.babelarity.utils.Utils;
/**
 * Implementazione della classe SemanticNetwork utilizzando il pattern Singleton
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 *
 */
public class MiniBabelNet extends SemanticNetwork implements Iterable<Synset> {
	private static MiniBabelNet instance;
	/**
	 * Ritorna l'istanza di MiniBabelNet
	 * @return L'istanza di MiniBabelNet
	 */
	public static MiniBabelNet getInstance() {
		if(instance == null) instance = new MiniBabelNet();
		return instance;
	}
	
	private Map<String, Synset> synset;
	private Map<String, List<String>> lemmatization;
	private Set<String> lemmas;
	private Set<Synset> synsetValues;
	private SimilarityObject<Word> lexicalSimilarity;
	private SimilarityObject<Synset> semanticSimilarity;
	private SimilarityObject<Document> documentSimilarity;
	
	private MiniBabelNet() {
		this.lemmatization = new HashMap<String, List<String>>();
		this.synset = new HashMap<String, Synset>();
		this.lemmas = new HashSet<String>();
		this.synsetValues = new HashSet<Synset>();
		
		Utils.forEachFileLine(line -> {
			List<String> l = new ArrayList<>(List.of(line.toLowerCase().split("\t")));
			if(!this.lemmatization.containsKey(l.get(0))) {
				this.lemmatization.put(l.get(0), new ArrayList<String>());
			}
			this.lemmatization.get(l.get(0)).add(l.get(1));
			this.lemmas.add(l.get(1));
		}, Paths.get("./resources/miniBabelNet/lemmatization-en.txt").toFile());
		
		Utils.forEachFileLine(line -> {
			List<String> l = new ArrayList<>(List.of(line.split("\t")));
			if(l.get(0).startsWith("bn:")) {
				Synset s = new BabelSynset(l.remove(0));
				s.addLemmas(l.stream().distinct().collect(Collectors.toList()));
				this.synset.put(s.getId(), s);
				this.synsetValues.add(s);
			}
		}, Paths.get("./resources/miniBabelNet/dictionary.txt").toFile());
		
		Utils.forEachFileLine(line -> {
			List<String> l = new ArrayList<>(List.of(line.split("\t")));
			this.synset.get(l.get(0)).linkTo(synset.get(l.get(1)), l.get(2));
			if(l.get(2).equals("is-a")) this.synset.get(l.get(1)).linkTo(synset.get(l.get(0)), "is-a-r");
		}, Paths.get("./resources/miniBabelNet/relations.txt").toFile());
		
		Utils.forEachFileLine(line -> {
			List<String> l = new ArrayList<>(List.of(line.split("\t")));
			if(l.get(0).startsWith("bn:")) this.synset.get(l.remove(0)).addGlosses(l);
		}, Paths.get("./resources/miniBabelNet/glosses.txt").toFile());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Synset> getSynsets(){
		return this.synsetValues;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getLemmas(){
		return this.lemmas;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getLemmas(String word) {
		return this.lemmatization.get(word);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<Synset> getSynsetsAsStream(String word){
		return synsetValues.stream().filter(s -> s.getLemmas().contains(word));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Synset getSynset(String id) {
		return this.synset.get(id);
	}
	/**
	 * Dato un Synset restituisce la sua rappresentazione sottoforma di stringa 
	 * @param s Synset da utilizzare
	 * @return La rappresentazione sottoforma di stringa del Synset
	 */
	public String getSynsetSummary(Synset s) {
		return s.toString();   
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double computeSimilarity(LinguisticObject o1, LinguisticObject o2) {
		try {
			if(o1 instanceof Word && o2 instanceof Word) {
				if(lexicalSimilarity == null) lexicalSimilarity = new BabelLexicalSimilarity(this);
				return lexicalSimilarity.computeSimilarity((Word)o1, (Word)o2);
			}
			else if(o1 instanceof Synset && o2 instanceof Synset) {
				if(semanticSimilarity == null) semanticSimilarity = new BabelSemanticSimilarity(this);
				return semanticSimilarity.computeSimilarity((Synset)o1, (Synset)o2);
			}
			else if(o1 instanceof Document && o2 instanceof Document) {
				if(documentSimilarity == null) documentSimilarity = new BabelDocumentSimilarity(this);
				return documentSimilarity.computeSimilarity((Document)o1, (Document)o2);
			}
			throw new SimilarityNotFoundException();
		} catch (SimilarityNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return 0;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Synset> iterator() {
		return this.synset.values().iterator();
	}
}
