package uniandes.algorithms.readsanalyzer;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ngsep.sequences.RawRead;
/**
 * Stores abundances information on a list of subsequences of a fixed length k (k-mers)
 * @author Jorge Duitama
 */
public class KmersTable implements RawReadProcessor {

	private final Map<String, Integer> table = new HashMap<>();

	private int kmerSize;

	/**
	 * Creates a new table with the given k-mer size
	 * @param kmerSize length of k-mers stored in this table
	 */
	public KmersTable(int kmerSize) {
		this.kmerSize = kmerSize;
	}

	private Stream<String> getKmers (String sequence) {
		int n = sequence.length();
		int numKmers = n - kmerSize + 1;
		return IntStream.range(0, numKmers).mapToObj(
			i -> sequence.substring(i, i + kmerSize)
		);
	}

	/**
	 * Identifies k-mers in the given read
	 * @param read object to extract new k-mers
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		Stream<String> kmers = getKmers(sequence);
		kmers.forEach(
			kmer -> table.compute(kmer, (k, v) -> v == null ? 1 : v + 1)
		);
	}
	
	/**
	 * List with the different k-mers found up to this point
	 * @return Set<String> set of k-mers
	 */
	public Set<String> getDistinctKmers() {
		return table.keySet();
	}
	
	/**
	 * Calculates the current abundance of the given k-mer 
	 * @param kmer sequence of length k
	 * @return int times that the given k-mer have been extracted from given reads
	 */
	public int getAbundance(String kmer) {
		return table.getOrDefault(kmer, 0);
	}

	private int[] makeHistogram (int maxAbundance) {
		int[] histogram = new int[maxAbundance];
		table.forEach(
			(kmer, abundance) -> histogram[abundance] += 1
		);
		return histogram;
	}
	
	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of k-mers
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		Stream<Integer> abundances = table.values().stream();
		Optional<Integer> maxAbundance = abundances.max(Comparator.comparingInt(x -> x));
		Optional<int[]> histogram = maxAbundance.map(this::makeHistogram);

		return histogram.orElse(new int[0]);
	}
}
