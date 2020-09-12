package uniandes.algobc.structures;

import java.util.ArrayList;
import java.util.TreeMap;

public class Gene {
	
	//Attributes
	
	/**
	 * Id of the gene
	 */
	private String id;
	/**
	 * Sequence name (usually chromosome) where the gene is located
	 */
	private String sequenceName;
	/**
	 * First position within the sequence name
	 */
	private int first;
	/**
	 * Last position within the sequence name
	 */
	private int last;
	/**
	 * List of ontology terms associated with this gene
	 */
	private TreeMap<String, Ontology> ontologies;
	
	//Methods
	
	/**
	 * Creates a new Gene object using the given data
	 * @param id of the new gene
	 * @param sequenceName where the gene is located
	 * @param first position in the DNA of the given sequence
	 * @param last position in the DNA of the given sequence
	 */
	public Gene(String id, String sequenceName, int first, int last) {
		this.id = id;
		this.sequenceName = sequenceName;
		this.first = first;
		this.last = last;
		ontologies = new TreeMap<String, Ontology>();
	}
	
	/**
	 * @return String id of the gene
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return String sequence name where the gene is located
	 */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * @return int first position in the DNA of the given sequence
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * @return int last position in the DNA of the given sequence
	 */
	public int getLast() {
		return last;
	}

	/**
	 * @return ArrayList<Ontology> List of ontology terms associated with this gene
	 */
	public ArrayList<Ontology> getOntologies() {
		ArrayList<Ontology> ontologiesList = new ArrayList<>(ontologies.values());
		return ontologiesList;
	}

	/**
	 * Associates an ontology term to this gene
	 * @param ontology New ontology term
	 */
	public void addOntology (Ontology ontology) {
		String ontologyKey = ontology.getId();
		ontologies.put(ontologyKey, ontology);
	}
	
	/**
	 * Tells if this gene is associated with the ontology term having the given id
	 * @param ontologyId Id of the ontology to search
	 * @return boolean true if this gene is associated with an ontology term having the given id, false otherwise
	 */
	public boolean hasOntology(String ontologyId) {
		Ontology geneOntology = ontologies.get(ontologyId);
		if(geneOntology != null) {
			return true;
		}
		return false;
	}
}
