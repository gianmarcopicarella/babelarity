package it.uniroma1.lcl.babelarity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
/**
 * Rappresenta la struttura di un documento serializzabile. Contiene una rappresentazione del contenuto nel documento sottoforma di grafo semantico. 
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class Document implements LinguisticObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title, id, content;
	private Graph<Synset> graph;
	/**
	 * Inizializza il documento
	 * @param title Titolo del documento
	 * @param id Id del documento
	 * @param content Contenuto del documento
	 */
	public Document(String title, String id, String content) {
		this.title = title;
		this.id = id;
		this.content = content;
		
	}
	/**
	 * Restituisce l'id
	 * @return L'id sottoforma di stringa
	 */
	public String getId() { return this.id; }
	/**
	 * Restituisce il titolo
	 * @return Il titolo sottoforma di stringa
	 */
	public String getTitle() { return this.title; }
	/**
	 * Restituisce il contenuto
	 * @return Il contenuto sottoforma di stringa
	 */
	public String getContent() { return this.content; }
	/**
	 * Restituisce il grafo dei Synset associati al contenuto del documento attuale
	 * @return Restituisce il grafo dei Synset associati al contenuto del documento attuale
	 */
	public Graph<Synset> getGraph() {
		if(this.graph == null) {
			this.graph = new Graph<Synset>();
			Arrays.asList(this.content.replaceAll("\\W", " ").toLowerCase().split(" "))
			.parallelStream().map(String::trim).filter(word -> !Word.stopWords.contains(word))
			.map(MiniBabelNet.getInstance()::getSynsetsAsStream)
			.map(syns -> syns.findFirst()).filter(k -> k.isPresent())
			.map(k -> k.get()).forEach(graph::addNode);
			graph.forEach(p -> {
				if(p.getKey().getLinked().contains(p.getValue()) && p.getValue().getLinked().contains(p.getKey())) 
					this.graph.addBiRelation(p.getKey(), p.getValue());
			});
			graph.getNodesByRelations(0).forEach(s -> {
				Set<Synset> nearest = s.getLinked(2);
				nearest.remove(s);
				nearest.retainAll(graph.getNodes());
				nearest.forEach(n -> graph.addBiRelation(n, s));
			});
			graph.removeAll(graph.getNodesByRelations(0));
		}
		return this.graph;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public boolean equals(Object arg0){
		if(this == arg0) return true;
		if(arg0 == null || arg0.getClass() != this.getClass()) return false;
		Document d = (Document)arg0;
		return this.id.equals(d.id);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
