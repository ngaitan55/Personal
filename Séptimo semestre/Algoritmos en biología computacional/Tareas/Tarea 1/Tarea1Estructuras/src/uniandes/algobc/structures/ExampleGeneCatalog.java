package uniandes.algobc.structures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ExampleGeneCatalog {
	//Attributes
	/**
	 * General map of ontology terms indexed by id 
	 */
	private HashMap<String,Ontology> ontologies = new HashMap<>();
	/**
	 * Catalog of genes annotated in a reference genome 
	 */
	private GeneCatalog geneCatalog;
	/**
	 * Random number generator
	 */
	private Random random = new Random();
	/**
	 * Main clas that executes the program
	 * @param args Array of arguments:
	 * args[0]: File with ontology terms
	 * args[1]: GFF3 file with gene annotations
	 * args[2]: Structure to load the gene catalog. It can be ArrayList, TreeMap or HashMap
	 * args[3]: Process to run. 
	 * 1 for a simulated search of gene ids
	 * 2 for a simulated search of ontology terms
	 * 3 to find the information of a specific gene
	 * args[4]: Optional. For process 3, id of the gene to search
	 * @throws Exception If the files can not be loaded
	 */
	public static void main(String[] args) throws Exception {
		
		String ontologiesFile = args[0];
		String genesFile = args[1];
		String catalogStructure = args[2];
		int process = Integer.parseInt(args[3]);
		ExampleGeneCatalog exampleCatalog = new ExampleGeneCatalog();
		exampleCatalog.loadOntologies(ontologiesFile);
		
		exampleCatalog.loadGenes(genesFile,catalogStructure);
		if(process == 1) {
			exampleCatalog.runSimulationGeneNames ();
		} else if (process == 2) {
			exampleCatalog.runSimulationOntologies ();
		} else if(args.length<5) {
			throw new Exception ("A gene name should be provided for process: "+process);
		} else {
			exampleCatalog.printGene(args[4]);
		}
		
		
	}
	/**
	 * Loads a list of ontologies from the given file.
	 * It loads only the id and the name of each ontology
	 * @param ontologiesFile Path to the file with ontology terms
	 * @throws IOException If the file can not be loaded
	 */
	public void loadOntologies(String ontologiesFile) throws IOException {
		try (FileReader reader = new FileReader(ontologiesFile);
			 BufferedReader in = new BufferedReader(reader)){
			String lastId = null;
			String lastName = null;
			String line = in.readLine();
			while (line != null) {
				if(line.startsWith("[Term]")) {
					if(lastId!=null) {
						Ontology o = new Ontology(lastId, lastName);
						ontologies.put(o.getId(), o);
					}
					
				} else if (line.startsWith("id: ")) {
					lastId = line.substring(4);
				} else if (line.startsWith("name: ")) {
					lastName = line.substring(6);
				}
				line = in.readLine();
			}
			if(lastId!=null) {
				Ontology o = new Ontology(lastId, lastName);
				ontologies.put(o.getId(), o);
			}
		}
		System.out.println("Loaded: "+ontologies.size()+" ontologies");
	}
	/**
	 * Loads a catalog of genes with ontology annotations from a GFF3 file
	 * @param genesFile Path to the GFF3 file having gene annotations on a genome.
	 * The "gene" feature should have an attribute called "Ontology_term=" with comma-separated
	 * ontology IDs
	 * @param catalogStructure Type of structure to load the gene catalog. It can be ArrayList, TreeMap or HashMap
	 * @throws IOException If the file can not be loaded
	 */
	public void loadGenes(String genesFile, String catalogStructure) throws IOException {
		String catalogClassname = "uniandes.algobc.structures."+catalogStructure+"GeneCatalog";
		try {
			Constructor<?> constructor = Class.forName(catalogClassname).getConstructors()[0]; 
			geneCatalog = (GeneCatalog) constructor.newInstance();
		} catch (Exception e) {
			throw new IOException("Error loading structure with id: "+catalogStructure+". Possible values are ArrayList, TreeMap and HashMap");
		}
		int n=0;
		try (FileReader reader = new FileReader(genesFile);
			 BufferedReader in = new BufferedReader(reader)){
			String line = in.readLine();
			while (line != null) {
				String [] items = line.split("\t");
				if(items.length>=9 && items[2].equals("gene")) {
					Gene gene = loadGene(items[8],items[0],Integer.parseInt(items[3]),Integer.parseInt(items[4]));
					if(gene!=null) geneCatalog.addGene(gene);
					n++;
				}
				line = in.readLine();
			}
		}
		System.out.println("Loaded: "+n+" genes");
		
	}
	/**
	 * Loads an specific gene in the catalog given the annotations of an specific feature
	 * @param ann Annotations of a feature in GFF3 format separated by semicolon
	 * @param sequenceName where the gene is located
	 * @param first position in the DNA of the given sequence
	 * @param last position in the DNA of the given sequence
	 * @return Gene object with the information of the new gene. Null if the gene can not be loaded
	 */
	private Gene loadGene(String ann, String sequenceName, int first, int last) {
		String [] items = ann.split(";");
		Gene answer = null;
		if(items.length==0 || !items[0].startsWith("ID=")) {
			System.err.println("Annotation for gene at "+sequenceName+":"+first+"-"+last+" should start with ID=. Current annotation: "+ann);
			return answer;
		}
		answer = new Gene (items[0].substring(3),sequenceName,first,last);
		for(int i=1;i<items.length;i++) {
			if(items[i].startsWith("Ontology_term=")) {
				String [] items2 = items[i].substring(14).split(",");
				for(int j=0;j<items2.length;j++) {
					Ontology o = ontologies.get(items2[j]);
					if(o!=null) answer.addOntology(o);
					else System.err.println("Ontology with id: "+items2[j]+" not found for gene: "+answer.getId());
				}
			}
		}
		return answer;
	}
	/**
	 * Runs a simulation of 100000 random ids to query the gene catalog
	 * Records the running time of the simulation
	 */
	public void runSimulationGeneNames() {
		long time = System.currentTimeMillis();
		for(int i=0;i<100000;i++) {
			String geneId = createRandomString();
			Gene gene = geneCatalog.getGene(geneId);
			if(gene!=null) System.out.println("Found gene with random id: "+geneId);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Time spent in the simulation: "+time);
	}
	/**
	 * Runs a simulation of 100000 random ontology terms to query the gene catalog
	 * Records the running time of the simulation
	 */
	public void runSimulationOntologies() {
		long time = System.currentTimeMillis();
		for(int i=0;i<100000;i++) {
			String ontologyId = createRandomString();
			List<Gene> genesOntology = geneCatalog.getGenes(ontologyId);
			if(genesOntology.size()>0) System.out.println("Found genes with random ontology: "+ontologyId);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Time spent in the simulation: "+time);
		
	}
	
	/**
	 * Prints the information of the gene with the given id
	 * @param geneId Id of the gene to search
	 */
	public void printGene(String geneId) {
		Gene gene = geneCatalog.getGene(geneId);
		if(gene!=null) {
			System.out.println("Id: "+gene.getId());
			System.out.println("Chromosome: "+gene.getSequenceName());
			System.out.println("First: "+gene.getFirst());
			System.out.println("Last: "+gene.getLast());
			System.out.println("Ontologies: ");
			ArrayList<Ontology> ontologies = gene.getOntologies();
			for(Ontology o:ontologies) System.out.println(""+o.getId()+"\t"+o.getName());
		} else {
			System.out.println("Gene "+geneId+" not found");
		}
	}
	/**
	 * Creates a random string between 5 and 14 characters
	 * @return String random string
	 */
	private String createRandomString() {
		StringBuilder answer = new StringBuilder();
		int length = random.nextInt(10)+5;
		for(int i=0;i<length;i++) {
			answer.append((char)(random.nextInt(40)+50));
		}
		
		return answer.toString();
	}
}
