package it.uniroma1.lcl.babelarity.utils;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;

import it.uniroma1.lcl.babelarity.LinkableObject;
/**
 * La classe Utils continene funzioni di utilità necessari al progetto.
 * 
 * @since 1.0
 * @author gianpcrx
 * @version 1.0
 */
public final class Utils {
	/**
	 * Trova la distanza minima tra due LinkableObject in un grafo
	 * @param <T> Tipo che estende l'interfaccia LinkableObject con lo stesso T
	 * @param start Nodo di partenza
	 * @param arrive Nodo di arrivo
	 * @param relations Relazioni da prendere in considerazione per il pathfinding
	 * @return La distanza tra due LinkableObject sotto forma di numero intero.
	 */
	public static <T extends LinkableObject<T>> int DijkstraDistance(LinkableObject<T> start, LinkableObject<T> arrive, String... relations) {
		Map<LinkableObject<T>, Integer> meta = new HashMap<>();
		PriorityQueue<LinkableObject<T>> queue = new PriorityQueue<>(5, (e1, e2) -> meta.get(e1).compareTo(meta.get(e2)));
		Set<LinkableObject<T>> visited = new HashSet<>();
		
		int k = -1;
		meta.put(start, 0);
		queue.add(start);
		
		while(!queue.isEmpty()) {
			LinkableObject<T> current = queue.remove();
			visited.add(current);
			if(current.equals(arrive)) return meta.get(current);
			for(LinkableObject<T> s : current.getLinked(relations)) {
				if(!visited.contains(s) && meta.get(current) + 1 < meta.getOrDefault(s, Integer.MAX_VALUE)) {
					meta.put(s, meta.get(current) + 1);
					queue.add(s);
				}
			}
		}
		return k;
	}
	/**
	 * Esegue un Consumer per ogni riga dei File
	 * @param o Consumer che definisce un'azione da essere eseguita ad ogni riga dei vari file.
	 * @param files Array di File da utilizzare
	 */
	public static void forEachFileLine(Consumer<String> o, File... files) {
		forEachFileLine(o, new ArrayList<>(List.of(files)));
	}
	
	/**
	 * Esegue un Consumer per ogni riga dei File
	 * @param o Consumer che definisce un'azione da essere eseguita ad ogni riga dei vari file.
	 * @param files Lista di File da utilizzare
	 */
	public static void forEachFileLine(Consumer<String> o, List<File> files) {
		while(files.size() > 0) {
			try(BufferedReader s = Files.newBufferedReader(files.remove(0).toPath())) {
				while(s.ready()) o.accept(s.readLine());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Esegue un Consumer per ogni riga di un File
	 * @param o Consumer che definisce un'azione da essere eseguita su ogni File.
	 * @param files Array di File da utilizzare
	 */
	public static void forEachFile(Consumer<String> o, File... files) {
		forEachFile(o, new ArrayList<>(List.of(files)));
	}
	
	/**
	 * Esegue un Consumer per ogni riga di un File
	 * @param o Consumer che definisce un'azione da essere eseguita su ogni File.
	 * @param files Lista di File da utilizzare
	 */
	public static void forEachFile(Consumer<String> o, List<File> files) {
		while(files.size() > 0) {
			try {
				o.accept(new String(Files.readAllBytes(files.remove(0).toPath())));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
