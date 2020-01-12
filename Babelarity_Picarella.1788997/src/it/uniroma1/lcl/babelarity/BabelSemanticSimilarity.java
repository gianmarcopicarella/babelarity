package it.uniroma1.lcl.babelarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.uniroma1.lcl.babelarity.exception.DifferentPosException;
import it.uniroma1.lcl.babelarity.utils.Utils;
/**
 * Implementazione della similarità semantica avanzata estendendo la classe SimilarityObject 
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class BabelSemanticSimilarity extends SimilarityObject<Synset> {
	private Map<POS, Double> taxonomyDepth;
	/**
	 * Inizializza la similarità
	 * @param network Rete Semantica da utilizzare
	 */
	public BabelSemanticSimilarity(SemanticNetwork network) {
		super(network);
		taxonomyDepth = new HashMap<POS, Double>();
	}
	
	private int maxDepth(int d, LinkableObject<Synset> current, Set<LinkableObject<Synset>> visited) {
		if(visited.contains(current)) return 0;
		visited.add(current);
		for(LinkableObject<Synset> s : current.getLinked("is-a-r")) 
			d = Math.max(d, maxDepth(d + 1, s, visited));
		return d;
	}
	
	private double averageDepth(POS taxonomy) {
		return network.getSynsets().stream()
				.filter(s -> s.getPOS().equals(taxonomy) && s.getLinked("is-a").size() == 0 && s.getLinked("has-kind").size() > 0)
				.mapToInt(k -> maxDepth(0, k, new HashSet<LinkableObject<Synset>>())).average().getAsDouble();
	}

	private int findLCS(Synset current, Synset arrive, int rd, Set<Synset> visited) {			
		if(current.equals(arrive)) return rd;
		visited.add(current);
		int min = Integer.MAX_VALUE;
		for(Synset s : current.getLinked("is-a")) {
			int dist =  Utils.DijkstraDistance(s, arrive, "is-a-r", "has-kind");
			if(dist > -1) min = Math.min(min, dist + rd + 1);
			if(!visited.contains(s)) min = Math.min(min, findLCS(s, arrive, rd + 1, visited));
		}
		return min;
	}
	
	private double getLCH(int distance, POS pos) {
		double result = -Math.log((distance + 1) / (2*this.taxonomyDepth.get(pos)));
		double max = -Math.log(1 / (2*this.taxonomyDepth.get(pos)));
		double min = -Math.log(this.taxonomyDepth.get(pos)/ (2*this.taxonomyDepth.get(pos)));
		return (result - min) / (max - min);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double computeSimilarity(Synset s1, Synset s2) {
		try {
			if(!s1.getPOS().equals(s2.getPOS())) 
				throw new DifferentPosException();
		} catch (DifferentPosException e) { 
			e.printStackTrace(); 
			return 0;
		}
		if(!this.taxonomyDepth.containsKey(s1.getPOS())) {
			this.taxonomyDepth.put(s1.getPOS(), averageDepth(s1.getPOS()));
		}
		
		// advanced similarity
		int distance = Utils.DijkstraDistance(s1, s2, "is-a-r", "has-kind");
		if(distance > -1) return getLCH(distance, s1.getPOS());

		distance = findLCS(s1, s2, 0, new HashSet<Synset>());
		if(distance != Integer.MAX_VALUE) return getLCH(distance, s1.getPOS());
		
		// basic similarity
		distance = Utils.DijkstraDistance(s1, s2);
		if(distance > -1) return 1.0 / ((double)distance + 1.0);
		return 0;
	}
}
