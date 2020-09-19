package uniandes.algorithms.readsanalyzer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ngsep.sequences.RawRead;

/**
 * Represents an overlap graph for a set of reads taken from a sequence to assemble
 * @author Jorge Duitama
 *
 */
public class OverlapGraph implements RawReadProcessor {

	private int minOverlap;
	private Map<String,Integer> readCounts = new HashMap<>();
	private Map<String, ArrayList<ReadOverlap>> overlaps = new HashMap<>();

	/**
	 * Creates a new overlap graph with the given minimum overlap
	 * @param minOverlap Minimum overlap
	 */
	public OverlapGraph(int minOverlap) {
		this.minOverlap = minOverlap;
	}

	/**
	 * Adds a new read to the overlap graph
	 * @param read object with the new read
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		//TODO: Paso 1. Agregar la secuencia al mapa de conteos si no existe.
		//Si ya existe, solo se le suma 1 a su conteo correspondiente y no se deben ejecutar los pasos 2 y 3
		if(readCounts.containsKey(sequence)) {
			readCounts.put(sequence, readCounts.get(sequence) + 1);
		}else {
			//TODO: Paso 2. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea predecesora de una secuencia existente
			//2.1 Crear un ArrayList para guardar las secuencias que tengan como prefijo un sufijo de la nueva secuencia
			//2.2 Recorrer las secuencias existentes para llenar este ArrayList creando los nuevos sobrelapes que se encuentren.
			readCounts.put(sequence, 1);
			Set<String> allSources = overlaps.keySet();
			Set<String> allSuccessors = overlaps.values().stream().flatMap(overlaps ->
				overlaps.stream().map(ReadOverlap::getDestSequence)
			).collect(Collectors.toSet());
			Set<String> existingSequences = new HashSet(allSources);
			existingSequences.addAll(allSuccessors);
			ArrayList<ReadOverlap> newOverlaps = new ArrayList<>();
			for(String s:existingSequences) {
				int forwardOverlap = getOverlapLength(sequence, s);
				if(forwardOverlap >= minOverlap) {
					ReadOverlap newOverlap = new ReadOverlap(sequence, s, forwardOverlap);
					newOverlaps.add(newOverlap);
				}
			}
			overlaps.put(sequence, newOverlaps);

			//2.3 Después del recorrido para llenar la lista, agregar la nueva secuencia con su lista de sucesores al mapa de sobrelapes
			//TODO: Paso 3. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea sucesora de una secuencia existente
			// Recorrer el mapa de sobrelapes. Para cada secuencia existente que tenga como sufijo un prefijo de la nueva secuencia
			//se agrega un nuevo sobrelape a la lista de sobrelapes de la secuencia existente
			for(String s : existingSequences) {
				int backwardsOverlap = getOverlapLength(s, sequence);
				if(backwardsOverlap >= minOverlap) {
					ReadOverlap currentOverlap = new ReadOverlap(s, sequence, backwardsOverlap);
					ArrayList<ReadOverlap> sOverlaps = overlaps.get(s);
					sOverlaps.add(currentOverlap);
					overlaps.put(s, sOverlaps);
				}
			}
		}


	}

	/**
	 * Returns the length of the maximum overlap between a suffix of sequence 1 and a prefix of sequence 2
	 * @param sequence1 Sequence to evaluate suffixes
	 * @param sequence2 Sequence to evaluate prefixes
	 * @return int Maximum overlap between a prefix of sequence2 and a suffix of sequence 1
	 */
	private int getOverlapLength(String sequence1, String sequence2) {
		// TODO Implementar metodo
		int overlap = 0;
		int n = sequence1.length();
		int i = n - 1;
		int j = n - 1;
		while (i >= 0 && j >= 0) {
			if (sequence1.charAt(i) == sequence2.charAt(j)) {
				overlap++;
				i--;
			} else {
				if (overlap > 0) {
					j++;
				}
				overlap = 0;
				i = n - 1;
			}
			j--;
		}
		return overlap;
	}

	/**
	 * Returns a set of the sequences that have been added to this graph
	 * @return Set<String> of the different sequences
	 */
	public Set<String> getDistinctSequences() {
		//TODO: Implementar metodo
		Set<String> distinctSequences = overlaps.keySet();
		return distinctSequences;
	}

	/**
	 * Calculates the abundance of the given sequence
	 * @param sequence to search
	 * @return int Times that the given sequence has been added to this graph
	 */
	public int getSequenceAbundance(String sequence) {
		//TODO: Implementar metodo
		int abundance = readCounts.get(sequence);
		return abundance;
	}

	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of sequences
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		Collection<Integer> countValues = readCounts.values();
		Map<Integer, Integer> countOcurrences = new HashMap<>();
		for(int c:countValues) {
			if(countOcurrences.containsKey(c)) {
				countOcurrences.put(c, countOcurrences.get(c)+1);
			}else {
				countOcurrences.put(c, 1);
			}
		}
		int n = Collections.max(countOcurrences.values());
		int[] abundancesDistribution = new int[n + 1];
		for(int o:countOcurrences.keySet()) {
			abundancesDistribution[countOcurrences.get(o)] = o;
		}
		return abundancesDistribution;
	}
	/**
	 * Calculates the distribution of number of successors
	 * @return int [] array where the indexes are number of successors and the values are the number of
	 * sequences having as many successors as the corresponding array index.
	 */
	public int[] calculateOverlapDistribution() {
		// TODO: Implementar metodo
		Collection<ArrayList<ReadOverlap>> countValues = overlaps.values();
		Map<Integer, Integer> countOcurrences = new HashMap<>();
		for(ArrayList<ReadOverlap> c:countValues) {
			int currentAbundance = c.size();
			if(countOcurrences.containsKey(currentAbundance)) {
				countOcurrences.put(currentAbundance, countOcurrences.get(currentAbundance)+1);
			}else {
				countOcurrences.put(currentAbundance, 1);
			}
		}
		int n = Collections.max(countOcurrences.values());
		int[] abundancesDistribution = new int[n + 1];
		for(int o:countOcurrences.keySet()) {
			abundancesDistribution[countOcurrences.get(o)] = o;
		}
		return abundancesDistribution;
	}

	/**
	 * Predicts the leftmost sequence of the final assembly for this overlap graph
	 * @return String Source sequence for the layout path that will be the left most subsequence in the assembly
	 */
	public Optional<String> getSourceSequence () {
		// TODO Implementar metodo recorriendo las secuencias existentes y buscando una secuencia que no tenga predecesores
		Set<String> potentialSources = overlaps.keySet();
		Set<String> allSuccessors = overlaps.values().stream().flatMap(children ->
				children.stream().map(ReadOverlap::getDestSequence)
		).collect(Collectors.toSet());

		// Difference between sources and list of successors gives a
		// set of nodes that  aren't in the successors (they have no predecessors)
		Set<String> difference = new HashSet(potentialSources);
		difference.removeAll(allSuccessors);

		Iterator<String> sources = difference.iterator();

		return sources.hasNext() ? Optional.of(sources.next()) : Optional.empty();
	}

	private ArrayList<ReadOverlap> buildLayoutPath (String src) {
		ArrayList<ReadOverlap> layout = new ArrayList<>();
		HashSet<String> visitedSequences = new HashSet<>();
		// TODO Implementar metodo. Comenzar por la secuencia fuente que calcula el método anterior
		// Luego, hacer un ciclo en el que en cada paso se busca la secuencia no visitada que tenga mayor sobrelape con la secuencia actual.
		// Agregar el sobrelape a la lista de respuesta y la secuencia destino al conjunto de secuencias visitadas. Parar cuando no se encuentre una secuencia nueva
		String current = src;

		while (!current.equals("$")) {
			ReadOverlap currentOverlap = overlaps.get(current).stream()
					.filter(overlap -> !visitedSequences.contains(overlap.getDestSequence()))
					.max(Comparator.comparingInt(ReadOverlap::getOverlap))
					.orElse(new ReadOverlap("", "$", 0));

			current = currentOverlap.getDestSequence();
			if (!current.equals("$")) {
				visitedSequences.add(current);
				layout.add(currentOverlap);
			}
		}

		return layout;
	}

	/**
	 * Calculates a layout path for this overlap graph
	 * @return ArrayList<ReadOverlap> List of adjacent overlaps. The destination sequence of the overlap in 
	 * position i must be the source sequence of the overlap in position i+1. 
	 */
	public ArrayList<ReadOverlap> getLayoutPath() {
		return getSourceSequence()
				.map(this::buildLayoutPath)
				.orElse(new ArrayList<>());
	}

	private String buildAssembly (ArrayList<ReadOverlap> layout) {
		// TODO Recorrer el layout y ensamblar la secuencia agregando al objeto assembly las bases adicionales que aporta la región de cada secuencia destino que está a la derecha del sobrelape
		Stream<StringBuilder> segmentEnds = layout.stream().map(overlap -> {
			int n = overlap.getOverlap();
			return new StringBuilder(overlap.getDestSequence().substring(n));
		});

		StringBuilder src = new StringBuilder(layout.get(0).getSourceSequence());
		StringBuilder assembly = segmentEnds.reduce(src, StringBuilder::append);

		return assembly.toString();
	}

	/**
	 * Predicts an assembly consistent with this overlap graph
	 * @return String assembly explaining the reads and the overlaps in this graph
	 */
	public String getAssembly () {
		ArrayList<ReadOverlap> layout = getLayoutPath();
		return layout.isEmpty() ? "" : buildAssembly(layout);
	}
}