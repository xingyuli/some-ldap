import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.swordess.ldap.util.ChangeObservedList;


public class ChangeObservedListTest extends TestCase {

	public void testAddOneElementWhenTheGivenListIsEmpty() {
		List<String> given = new ArrayList<String>();
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("item");
		
		assertEquals(1, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(1, added.size());
		assertEquals("item", added.get(0));
	}
	
	public void testAddThreeSameElementsWhenTheGivenListIsEmpty() {
		List<String> given = new ArrayList<String>();
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("item");
		observed.add("item");
		observed.add("item");
		
		assertEquals(3, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(3, added.size());
		for (String addition : added) {
			assertEquals("item", addition);
		}
	}
	
	public void testAddTwoDifferentElementsWhenTheGivenListIsEmpty() {
		List<String> given = new ArrayList<String>();
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("hello");
		observed.add("world");
		
		assertEquals(2, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(2, added.size());
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("world"));
	}
	
	public void testAddOneExistedElementWhenTheGivenListHasOnlyOneElement() {
		List<String> given = new ArrayList<String>();
		given.add("item");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("item");
		
		assertEquals(2, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(1, added.size());
		assertEquals("item", added.get(0));
	}
	
	public void testAddOneExistedElementAndRemoveItWhenTheGivenListHasOnlyOneElement() {
		List<String> given = new ArrayList<String>();
		given.add("item");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("item");
		observed.remove("item");
		
		assertEquals(1, observed.size());
		assertEquals("item", observed.get(0));
		
		assertEquals(0, observed.getAddedElements().size());
		assertEquals(0, observed.getRemovedElements().size());
	}
	
	public void testAddTwoSameExistedElementsAndRemoveOneWhenTheGivenListHasOnlyElement() {
		List<String> given = new ArrayList<String>();
		given.add("item");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.add("item");
		observed.add("item");
		observed.remove("item");
		
		assertEquals(2, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(1, added.size());
		assertEquals("item", added.get(0));
	}
	
	public void testRemoveElementWhenTheGivenListIsEmpty() {
		List<String> given = new ArrayList<String>();
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		assertEquals(0, observed.getRemovedElements().size());
	}
	
	public void testRemoveOneElementOnceWhenTheGivenListHasOnlyOneElement() {
		List<String> given = new ArrayList<String>();
		given.add("item");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		List<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertEquals("item", removed.get(0));
	}
	
	public void testRemoveOneElementTwiceWhenTheGivenListHasOnlyOneElement() {
		List<String> given = new ArrayList<String>();
		given.add("item");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		observed.remove("item");
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		List<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertEquals("item", removed.get(0));
	}
	
	public void testComplex() {
		List<String> given = new ArrayList<String>();
		given.add("one");
		given.add("two");
		given.add("three");
		given.add("four");
		given.add("one");
		given.add("one");
		
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		
		/* ***************************************************************** */
		
		observed.add("five");
		observed.add("five");
		observed.add("hello");
		observed.add("world");
		observed.add("Effective Java");
		
		assertEquals(11, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		List<String> added = observed.getAddedElements();
		assertEquals(5, added.size());
		assertTrue(added.contains("five"));
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("world"));
		assertTrue(added.contains("Effective Java"));
		assertTrue(added.indexOf("five") != added.lastIndexOf("five"));
		
		/* ***************************************************************** */
		
		observed.remove("one");
		observed.remove("one");
		observed.remove("five");
		observed.remove("world");
		observed.remove("__not_exist__");
		
		assertEquals(7, observed.size());
		
		added = observed.getAddedElements();
		assertEquals(3, added.size());
		assertTrue(added.contains("five"));
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("Effective Java"));
		
		List<String> removed = observed.getRemovedElements();
		assertEquals(2, removed.size());
		assertTrue(removed.contains("one"));
		assertTrue(removed.indexOf("one") != removed.lastIndexOf("one"));
	}
	
	/**
	 * ------ -------- --------- --------------------------------- -----------------------------
	 *  given      add    remove    RealTimeComputeChangeImpl(avg)    PostComputeChangeImpl(avg)
	 * ------ -------- --------- --------------------------------- -----------------------------
	 *   5000     2500      2500                    1,1,1,1,1(0ms)              1,2,2,3,4(584ms)
	 *  10000     5000      5000                    6,6,6,6,6(6ms)         8,12,16,20,23(3778ms)
	 *  20000    10000     10000               26,26,26,26,26(6ms)      37,55,74,92,110(18328ms)
	 *  50000    20000     20000          114,114,114,114,114(6ms) 217,320,423,527,630(107825ms)
	 */
	public static void main(String[] args) {
		final String context = "ou=People,ou=example,o=com";
		
		int givenCount = 50000;
		int addCount = 20000;
		int removeCount = 20000;
		int offset = givenCount * 4/10;
		
		System.out.println("givenCount: " + givenCount);
		System.out.println("addCount: " + addCount);
		System.out.println("removeCount: " + removeCount);
		System.out.println("offset: " + offset);
		
		List<String> given = new LinkedList<String>();
		for (int i = 0; i < givenCount; i++) {
			given.add(String.format("uid=%8d,%s", i, context));
		}
		
		final Random rand = new Random(37);
		long start = System.currentTimeMillis();
		ChangeObservedList<String> observed = new ChangeObservedList<String>(given);
		System.out.println("size before: " + observed.size());
		for (int i = 0; i < addCount; i++) {
			observed.add(String.format("uid=%8d,%s", rand.nextInt(givenCount) + offset, context));
		}
		for (int i = 0; i < removeCount; i++) {
			observed.remove(String.format("uid=%8d,%s", rand.nextInt(givenCount) + offset, context));
		}
		
		System.out.println("size after: " + observed.size());
		long loopStart = System.currentTimeMillis();
		int limit = 5;
		for (int i = 0; i < limit; i++) {
			observed.getAddedElements();
			observed.getRemovedElements();
			long end = System.currentTimeMillis();
			System.out.println(String.format("elappse %d: %ds", i+1, (end - start) / 1000));
		}
		long loopEnd = System.currentTimeMillis();
		System.out.println("avg: " + (loopEnd - loopStart) / limit + "ms");
	}
	
}
