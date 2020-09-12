package uniandes.algobc.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayListGeneCatalog implements GeneCatalog {
	private ArrayList<Gene> genes = new ArrayList<>();
	/**
	 * Adds a gene to the catalog
	 * @param gene Data of the new gene
	 */
	@Override
	public void addGene(Gene gene) {
		//TODO: Implement method
		genes.add(gene);
	}

	/**
	 * Finds a gene given the id
	 * @param geneId Id of the gene to search
	 * @return Gene object with the gene information or null if it is not found
	 */
	@Override
	public Gene getGene(String geneId) {
		//TODO: Implement method
		for(Gene gene:genes) {
			String currentId = gene.getId();
			if(currentId.equals(geneId)) {
				return gene;
			}
		}
		return null;
		
	}

	/**
	 * Finds the genes associated with the given ontology term
	 * @param ontologyId Id of the ontology to search
	 * @return ArrayList<Gene> Listo of genes associated with the given ontology 
	 */
	@Override
	public ArrayList<Gene> getGenes(String ontologyId) {
		//TODO: Implement method
		ArrayList<Gene> ontologyGenes = new ArrayList<Gene>();
		for(Gene currentGene:genes) {
			boolean hasOntology = currentGene.hasOntology(ontologyId);
			if(hasOntology) {
				ontologyGenes.add(currentGene);
			}
		}
		return ontologyGenes;
	}

}
