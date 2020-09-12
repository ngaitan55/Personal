package uniandes.algobc.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class TreeMapGeneCatalog implements GeneCatalog {
	private TreeMap<String, Gene> genes = new TreeMap<String, Gene>();
	/**
	 * Adds a gene to the catalog
	 * @param gene Data of the new gene
	 */
	@Override
	public void addGene(Gene gene) {
		// TODO Auto-generated method stub
		String geneKey = gene.getId();
		genes.put(geneKey, gene);
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
		ArrayList<Gene> ontologyGenes = new ArrayList<Gene>();
		Collection<Gene> allGenes = genes.values();
		for(Gene gene:allGenes) {
			boolean hasOntology = gene.hasOntology(ontologyId);
			if(hasOntology) {
				ontologyGenes.add(gene);
			}
		}
		return ontologyGenes;
	}

}
