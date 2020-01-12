package it.uniroma1.lcl.babelarity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.util.Pair;
/**
 * Classe rappresentante la struttura di un Grafo con relazioni non pesate
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 * @param <T> Un tipo qualsiasi
 */
public class Graph<T> implements Iterable<Pair<T, T>> {
	private Set<T> nodes;
	private Map<T, Set<T>> relations;
	/**
	 * Inizializza il grafo
	 */
	public Graph() {
		relations = new HashMap<T, Set<T>>();
		nodes = new HashSet<T>();
	}
	/**
	 * Aggiunge un nodo al grafo
	 * @param s Nodo da aggiungere
	 */
	public void addNode(T s) {
		relations.putIfAbsent(s, new HashSet<T>());
		nodes.add(s);
	}
	/**
	 * Rimuove un nodo al grafo
	 * @param s Nodo da rimuovere
	 */
	public void removeNode(T s) {
		relations.remove(s);
		nodes.remove(s);
	}
	/**
	 * Ritorna tutti i Nodi con un numero di relazioni uguale a quello specificato
	 * @param n Numero di relazioni da utilizzare nella ricerca
	 * @return Una lista cotenente tutti i Nodi con un numero di relazioni uguale a quello specificato
	 */
	public List<T> getNodesByRelations(int n){
		return relations.entrySet().stream().filter(e -> e.getValue().size() == n)
				.map(e -> e.getKey()).collect(Collectors.toList());
	}
	/**
	 * Rimuove tutti i nodi presenti nella collezione dal grafo
	 * @param c Nodi da rimuovere
	 */
	public void removeAll(Collection<T> c) {
		nodes.removeAll(c);
		c.forEach(n -> relations.remove(n));
	}
	/**
	 * Aggiunge una relazione unidirezionale tra i due nodi specificati
	 * @param source Nodo sorgente
	 * @param target Nodo target
	 */
	public void addRelation(T source, T target) {
		if(relations.containsKey(source)) {
			relations.get(source).add(target);
		}
	}
	/**
	 * Aggiunge una relazione bidirezionale tra i due nodi specificati
	 * @param source Nodo sorgente
	 * @param target Nodo target
	 */
	public void addBiRelation(T source, T target) {
		addRelation(source, target);
		addRelation(target, source);
	}
	/**
	 * Restituisce tutti i nodi sottoforma di Set
	 * @return Un Set contenente tutti i nodi del grafo
	 */
	public Set<T> getNodes(){
		return nodes;
	}
	/**
	 * Restituisce tutti i nodi connessi al nodo specificato
	 * @param n Nodo da utilizzare nella ricerca
	 * @return Un Set contentente tutti i nodi connessi al nodo specificato
	 */
	public Set<T> getRelation(T n){
		return relations.get(n);
	}
	/**
	 * Itera su tutte le possibili coppie di nodi presenti nel grafo
	 */
	@Override
	public Iterator<Pair<T, T>> iterator() {
		return new Iterator<Pair<T, T>>() {
			private int i = 0, k = 0;
			private List<T> cpy = nodes.stream().collect(Collectors.toList());
			
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				if(k >= cpy.size()) {
					k = 0;
					i++;
				}
				return i < cpy.size();
			}

			@Override
			public Pair<T, T> next() {
				// TODO Auto-generated method stub
				return hasNext() ? new Pair<T, T>(cpy.get(i), cpy.get(k++)) : null;
			}
		};
	}
	/**
	 * Ritorna un set rappresentante l'unione dei nodi contenuti nell'oggetto corrente ed il grafo specificato
	 * @param d Grafo da utilizzare nell'unione
	 * @return Ritorna un set rappresentante l'unione dei nodi contenuti nell'oggetto corrente ed il grafo specificato
	 */
	public Set<T> merge(Graph<T> d){
		Set<T> set = new HashSet<T>(this.nodes);
		set.addAll(d.getNodes());
		return set;
	}
}
