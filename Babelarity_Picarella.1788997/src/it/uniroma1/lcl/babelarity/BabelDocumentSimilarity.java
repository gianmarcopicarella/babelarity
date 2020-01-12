package it.uniroma1.lcl.babelarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniroma1.lcl.babelarity.math.IntegerVector;
/**
 * Implementazione della similarità tra documenti avanzata estendendo la classe SimilarityObject 
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class BabelDocumentSimilarity extends SimilarityObject<Document> {
	/**
	 * Inizializza la similarità
	 * @param network Rete Semantica da utilizzare
	 */
	public BabelDocumentSimilarity(SemanticNetwork network) {
		super(network);
	}

	private IntegerVector randomWalk(Graph<Synset> graph, Map<Synset, Integer> indexMap) {
		List<Synset> synsets = graph.getNodes().stream().collect(Collectors.toList());
		float restart = 0.1f;
		int iteration = 50000;
		Synset synset = synsets.get(0);
		IntegerVector v = new IntegerVector(indexMap.size());
		while(iteration-- > 0) {
			double alpha = Math.random();
			if(alpha > restart) synset = synsets.get((int)(Math.random() * synsets.size()));
			int a = indexMap.get(synset);
			v.set(a, v.get(a) + 1);
			synset = graph.getRelation(synset).iterator().next();
		}
		return v;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double computeSimilarity(Document d1, Document d2) {
		Map<Synset, Integer> indexMap = new HashMap<>();
		for(Synset s : d1.getGraph().merge(d2.getGraph())) indexMap.put(s, indexMap.size());
		return randomWalk(d1.getGraph(), indexMap).cosineSimilarityWith(randomWalk(d2.getGraph(), indexMap));
	}
}