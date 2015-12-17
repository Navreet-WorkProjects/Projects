import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MedianOfMedian {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		int requiredPos = 8;

		ArrayList inputList = new ArrayList();

		// For entering values randomly

		Random random = new Random(1);
		int max = 10;
		for (int i = 0; i < max; i++) {
			int var = random.nextInt(256);
			inputList.add(var);
		}

		ArrayList copyInputList = new ArrayList();
		copyInputList.addAll(inputList);
		int m1 = (Integer) MedianOfMedian.medianOfMedian(inputList, requiredPos-1);
		Collections.sort(copyInputList);
		for (Object i : copyInputList) {
			System.out.print((Integer) i + " ");
		}
		int m2 = (Integer) copyInputList.get(requiredPos-1);
		System.out.println("\nElement received using median of median sorting " +m1 
				+ "\nElement received using insertion sorting " + m2);

	}

	/**
	 * Returns element at position 'place' using median of median algorithm.
	 * 
	 * @param place position being searched
	 * @param list list to search, which may be reordered on return
	 * @return element at position 'place' using median of median algorithm.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Comparable medianOfMedian(ArrayList<Comparable> inputList, int place) {
		int size = inputList.size();
		if (size < 1)
			throw new IllegalArgumentException();
		int pos = select(inputList, 0, size, place);
		return inputList.get(pos);
	}

	/**
	 * Returns position of k'th element of sub-list.
	 * 
	 * @param list list to search, whose sub-list may be shuffled before returning
	 * @param lo first element of sub-list in list
	 * @param hi just after last element of sub-list in list
	 * @param k position being searched
	 * @return position of k'th element.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int select(ArrayList<Comparable> inputList, int lo, int hi, int k) {
		if (lo >= hi || k < 0 || lo + k >= hi)
			throw new IllegalArgumentException();
		if (hi - lo < 10) {
			Collections.sort(inputList.subList(lo, hi));
			return lo + k;
		}
		int s = hi - lo;
		int np = s / 5; // Number of partitions
		for (int i = 0; i < np; i++) {
			// For each partition, move its median to front of our sublist
			int lo2 = lo + i * 5;
			int hi2 = (i + 1 == np) ? hi : (lo2 + 5);
			int pos = select(inputList, lo2, hi2, 2);
			Collections.swap(inputList, pos, lo + i);
		}

		// Partition medians were moved to front, so we can recurse without
		// making another list.
		int pos = select(inputList, lo, lo + np, np / 2);

		// Re-partition list to [<pivot][pivot][>pivot]
		int m = sort(inputList, lo, hi, pos);
		int cmp = lo + k - m;
		if (cmp > 0)
			return select(inputList, m + 1, hi, k - (m - lo) - 1);
		else if (cmp < 0)
			return select(inputList, lo, m, k);
		return lo + k;
	}

	/**
	 * Partition sub-list into 3 parts [<pivot][pivot][>pivot].
	 * 
	 * @param list
	 * @param lo
	 * @param hi
	 * @param pos input position of pivot value
	 * @return output position of pivot value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int sort(ArrayList<Comparable> list, int lo, int hi,
			int pos) {
		Comparable pivot = list.get(pos);
		int lo3 = lo;
		int hi3 = hi;
		while (lo3 < hi3) {
			Comparable e = list.get(lo3);
			int cmp = e.compareTo(pivot);
			if (cmp < 0)
				lo3++;
			else if (cmp > 0)
				Collections.swap(list, lo3, --hi3);
			else {
				while (hi3 > lo3 + 1) {
					assert (list.get(lo3).compareTo(pivot) == 0);
					e = list.get(--hi3);
					cmp = e.compareTo(pivot);
					if (cmp <= 0) {
						if (lo3 + 1 == hi3) {
							Collections.swap(list, lo3, lo3 + 1);
							lo3++;
							break;
						}
						Collections.swap(list, lo3, lo3 + 1);
						assert (list.get(lo3 + 1).compareTo(pivot) == 0);
						Collections.swap(list, lo3, hi3);
						lo3++;
						hi3++;
					}
				}
				break;
			}
		}
		assert (list.get(lo3).compareTo(pivot) == 0);
		return lo3;
	}

}
