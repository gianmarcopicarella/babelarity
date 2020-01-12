package it.uniroma1.lcl.babelarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Implementazione della classe Synset
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class BabelSynset extends Synset {
	
	private Map<String, List<Synset>> relations;
	private Set<Synset> linkedNodes;
	/**
	 * Inizializza il BabelSynset 
	 * @param id Id del BabelSynset
	 */
	public BabelSynset(String id) {
		super(id);
		linkedNodes = new HashSet<Synset>();
		relations = new HashMap<String, List<Synset>>();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLemmas(List<String> lemmas) {
		lemmas.removeIf(s -> s.startsWith("bn:"));
		this.lemmas.addAll(lemmas);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addGlosses(List<String> glosses) {
		this.glosses.addAll(glosses);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void linkTo(Synset s, String type) {
		if(!relations.containsKey(type)) relations.put(type, new ArrayList<Synset>());
		relations.get(type).add(s);
		linkedNodes.add(s);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Synset> getLinked(String... types) {
		if(types.length == 0) return this.linkedNodes;
		return Set.of(types).stream()
		.filter(relations::containsKey)
		.flatMap(k -> relations.get(k).stream())
		.collect(Collectors.toSet());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Synset> getLinkedExcept(String... types) {
		if(types.length == 0) return this.linkedNodes;
		Set<String> keys = this.relations.keySet();
		keys.retainAll(Set.of(types));
		return getLinked(keys.stream().toArray(String[]::new));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getId() + '\t' + getPOS().toString() + '\t' 
				+ lemmas.stream().collect(Collectors.joining(";")) + '\t'
				+ glosses.stream().collect(Collectors.joining(";")) + '\t'
				+ relations.entrySet().stream().filter(e -> !e.getKey().equals("is-a-r"))
				.map(e -> e.getValue().stream().map(s -> s.getId() + "_" + e.getKey()))
				.flatMap(k -> k).collect(Collectors.joining(";"));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<Synset> getLinkedRecursive(Synset root, Set<Synset> visited, int distance) {
		if(distance <= 0) return visited; 
		Set<Synset> linked = root.getLinked();
		visited.add(root);
		visited.addAll(linked);
		for(Synset s : linked) getLinkedRecursive(s, visited, distance-1);
		return visited;
	}
}
