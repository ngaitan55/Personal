package uniandes.algobc.structures;

import java.util.ArrayList;

public interface GeneCatalog {
	/**
	 * Adds a gene to the catalog
	 * @param gene Data of the new gene
	 */
	public void addGene (Gene gene);
	/**
	 * Finds a gene given the id
	 * @param geneId Id of the gene to search
	 * @return Gene object with the gene information or null if it is not found
	 */
	public Gene getGene(String geneId);
	/**
	 * Finds the genes associated with the given ontology term
	 * @param ontologyId Id of the ontology to search
	 * @return ArrayList<Gene> Listo of genes associated with the given ontology 
	 */
	public ArrayList<Gene> getGenes (String ontologyId);
}
