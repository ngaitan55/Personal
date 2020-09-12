package uniandes.algobc.structures;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class HashMapGeneCatalog implements GeneCatalog {
	/**
	 * Collection of the genes indexed by their id
	 */
	private HashMap<String, Gene> genes = new HashMap<String, Gene>();
	
	/**
	 * Collection of the genes indexed by their ontologyId
	 */
	private HashMap<String, ArrayList<Gene>> genesByOntology = new HashMap<String, ArrayList<Gene>>();
	
	/**
	 * Adds a gene to the catalog
	 * @param gene Data of the new gene
	 */
	@Override
	public void addGene(Gene gene) {
		// TODO Auto-generated method stub
		String geneKey = gene.getId();
		genes.put(geneKey, gene);
		ArrayList<Ontology> listOntologies = gene.getOntologies();
		for(Ontology ontology:listOntologies) {
			String ontologyKey = ontology.getId();
			if(!genesByOntology.containsKey(ontologyKey)) {
				ArrayList<Gene> currentGenes = new ArrayList<Gene>();
				currentGenes.add(gene);
				genesByOntology.put(ontologyKey, currentGenes);
			}
			else {
				ArrayList<Gene> currentGenes = genesByOntology.get(ontologyKey);
				currentGenes.add(gene);
				genesByOntology.put(ontologyKey, currentGenes);
			}
		}
	}
	/**
	 * Finds a gene given the id
	 * @param geneId Id of the gene to search
	 * @return Gene object with the gene information or null if it is not found
	 */
	@Override
	public Gene getGene(String geneId) {
		// TODO Auto-generated method stub
		Gene searchedGene = genes.get(geneId);
		return searchedGene;
	}
	/**
	 * Finds the genes associated with the given ontology term
	 * @param ontologyId Id of the ontology to search
	 * @return ArrayList<Gene> List of genes associated with the given ontology 
	 */
	@Override
	public ArrayList<Gene> getGenes(String ontologyId) {
		// TODO Auto-generated method stub
		ArrayList<Gene> ontologyGenes = genesByOntology.get(ontologyId);
		if(ontologyGenes == null) {
			ontologyGenes = new ArrayList<Gene>();
		}
		return ontologyGenes;
	}

}
