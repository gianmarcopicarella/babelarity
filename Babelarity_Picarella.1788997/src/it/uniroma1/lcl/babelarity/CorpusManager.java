package it.uniroma1.lcl.babelarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniroma1.lcl.babelarity.utils.Utils;

/**
 * Rappresenta la classe implementata con pattern Singleton che si occupa del caricamento dei vari documenti e del corpus
 * @author gianpcrx
 * @since 1.0
 * @version 1.0
 */
public class CorpusManager implements Iterable<String> {
	private static CorpusManager instance;
	/**
	 * Restituisce l'istanza dell'oggetto CorpusManager
	 * @return L'istanza dell'oggetto CorpusManager
	 */
	public static CorpusManager getInstance() {
		if(instance == null) instance = new CorpusManager();
		return instance;
	}
	private static Path documentPath = Paths.get("resources", "documents");
	private static Path corpusPath = Paths.get("resources", "corpus");
	private Map<String, Document> documents;
	private Map<String, String> nameToId;
	private List<String> corpusDocuments;
	
	private CorpusManager() {
		documents = new HashMap<String, Document>();
		corpusDocuments = new ArrayList<String>();
		nameToId = new HashMap<String, String>();
	}
	/**
	 * Restituisce una nuova istanza di Document parsando un file di testo di cui è fornito il percorso in input
	 * @param p Path del file da caricare
	 * @return Il documento rappresentante il file caricato
	 */
	public Document parseDocument(Path p) {
		if(nameToId.containsKey(p.getFileName().toString())) {
			return documents.get(nameToId.get(p.getFileName().toString()));
		}
		try {
			List<String> lines = Files.readAllLines(p);
			String[] info = lines.remove(0).split("\t");
			documents.put(info[1], new Document(info[0], info[1], lines.stream().collect(Collectors.joining())));
			nameToId.put(p.getFileName().toString(), info[1]);
			return documents.get(info[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Restituisce una nuova istanza del documento identificato dall'id preso in input caricando il file serializzato .res
	 * @param id Id del file da caricare
	 * @return Il documento rappresentante il file caricato
	 */
	public Document loadDocument(String id) {
		if(documents.containsKey(id)) return documents.get(id);
		try {
			id += ".res";
			for(File f : documentPath.toFile().listFiles()) {
				if(f.getName().equals(id)) {
					FileInputStream file = new FileInputStream(f.getPath()); 
		            ObjectInputStream in = new ObjectInputStream(file); 
		            Document d = (Document)in.readObject(); 
		            in.close(); 
		            file.close(); 
		            return d;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Serializza l'oggetto Document nel formato .res
	 * @param document Documento da serializzare
	 */
	public void saveDocument(Document document) {
		try {
			FileOutputStream fs = new FileOutputStream(documentPath.resolve(document.getId()) + ".res");
			ObjectOutputStream out = new ObjectOutputStream(fs);
			out.writeObject(new Document(document.getId(), document.getTitle(), document.getContent()));
			fs.close();
			out.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<String> iterator() {
		if(corpusDocuments.size() == 0) 
			Utils.forEachFile(corpusDocuments::add, corpusPath.toFile().listFiles());
		return this.corpusDocuments.iterator();
	}
}
