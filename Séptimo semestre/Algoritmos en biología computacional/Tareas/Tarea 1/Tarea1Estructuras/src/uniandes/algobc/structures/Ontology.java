package uniandes.algobc.structures;

public class Ontology {
	//Attributes
	/**
	 * Id of the ontology
	 */
	private String id;
	/**
	 * Name of the ontology
	 */
	private String name;
	/**
	 * Creates a new ontology term with the given data
	 * @param id of the new ontology
	 * @param name of the new ontology
	 */
	public Ontology(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	/**
	 * @return String ontology id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return String ontology name
	 */
	public String getName() {
		return name;
	}
}
