import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import org.swordess.ldap.util.ChangeObservedSet;


public class ChangeObservedSetTest extends TestCase {

	public void testAddOneElementWhenTheGivenSetIsEmpty() {
		Set<String> given = new HashSet<String>();
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("item");
		
		assertEquals(1, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		Set<String> added = observed.getAddedElements();
		assertEquals(1, added.size());
		assertEquals("item", added.iterator().next());
	}
	
	public void testAddThreeSameElementsWhenTheGivenSetIsEmpty() {
		Set<String> given = new HashSet<String>();
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("item");
		observed.add("item");
		observed.add("item");
		
		assertEquals(1, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		Set<String> added = observed.getAddedElements();
		assertEquals(1, added.size());
		assertTrue(added.contains("item"));
	}
	
	public void testAddTwoDifferentElementsWhenTheGivenSetIsEmpty() {
		Set<String> given = new HashSet<String>();
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("hello");
		observed.add("world");
		
		assertEquals(2, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		Set<String> added = observed.getAddedElements();
		assertEquals(2, added.size());
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("world"));
	}
	
	public void testAddOneExistedElementWhenTheGivenSetIsNotEmpty() {
		Set<String> given = new HashSet<String>();
		given.add("item");
		
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("item");
		
		assertEquals(1, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		assertEquals(0, observed.getAddedElements().size());
	}
	
	public void testAddOneExistedElementAndRemoveItWhenTheGivenSetHasOnlyOneElement() {
		Set<String> given = new HashSet<String>();
		given.add("item");
		
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("item");
		System.out.println(observed);
		System.out.println(observed.getAddedElements());
		System.out.println(observed.getRemovedElements());
		
		observed.remove("item");
		System.out.println(observed);
		System.out.println(observed.getAddedElements());
		System.out.println(observed.getRemovedElements());
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		Set<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertTrue(removed.contains("item"));
	}
	
	public void testAddTwoSameExistedElementsAndRemoveOneWhenTheGivenSetHasOnlyElement() {
		Set<String> given = new HashSet<String>();
		given.add("item");
		
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.add("item");
		observed.add("item");
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		Set<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertTrue(removed.contains("item"));
	}
	
	public void testRemoveElementWhenTheGivenSetIsEmpty() {
		Set<String> given = new HashSet<String>();
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		assertEquals(0, observed.getRemovedElements().size());
	}
	
	public void testRemoveOneElementOnceWhenTheGivenSetHasOnlyOneElement() {
		Set<String> given = new HashSet<String>();
		given.add("item");
		
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		Set<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertTrue(removed.contains("item"));
	}
	
	public void testRemoveOneElementTwiceWhenTheGivenSetHasOnlyOneElement() {
		Set<String> given = new HashSet<String>();
		given.add("item");
		
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		observed.remove("item");
		observed.remove("item");
		
		assertEquals(0, observed.size());
		assertEquals(0, observed.getAddedElements().size());
		
		Set<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertTrue(removed.contains("item"));
	}
	
	public void testComplex() {
		Set<String> given = new HashSet<String>();
		given.add("one");
		given.add("two");
		given.add("three");
		given.add("four");
		given.add("one");
		given.add("one");
		
		// Current observed: one, two, three, four
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
		
		/* ***************************************************************** */
		
		// Now: one, two, three, four, five, hello, world, Effective Java
		observed.add("five");
		observed.add("five");
		observed.add("hello");
		observed.add("world");
		observed.add("Effective Java");
		
		assertEquals(8, observed.size());
		assertEquals(0, observed.getRemovedElements().size());
		
		Set<String> added = observed.getAddedElements();
		assertEquals(4, added.size());
		assertTrue(added.contains("five"));
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("world"));
		assertTrue(added.contains("Effective Java"));
		
		/* ***************************************************************** */
		
		// Now: two, three, four, hello, Effective Java
		observed.remove("one");
		observed.remove("one");
		observed.remove("five");
		observed.remove("world");
		observed.remove("__not_exist__");
		
		assertEquals(5, observed.size());
		
		added = observed.getAddedElements();
		assertEquals(2, added.size());
		assertTrue(added.contains("hello"));
		assertTrue(added.contains("Effective Java"));
		
		Set<String> removed = observed.getRemovedElements();
		assertEquals(1, removed.size());
		assertTrue(removed.contains("one"));
	}
	
	/**
	 * ------ -------- --------- --------------------------------- -----------------------------
	 *  given      add    remove    RealTimeComputeChangeImpl(avg)    PostComputeChangeImpl(avg)
	 * ------ -------- --------- --------------------------------- -----------------------------
	 *  50000    20000     20000                    0,0,0,0,0(6ms)               0,0,0,0,0(47ms)
	 * 100000    40000     40000                    1,1,1,1,1(9ms)               1,1,1,1,1(97ms)
	 * 200000    80000     80000                   1,1,1,2,2(25ms)              2,2,2,2,2(200ms)
	 * 400000   160000    160000                   3,3,3,3,3(46ms)              3,4,4,4,5(509ms)
	 * 800000   320000    320000                  8,8,8,8,8(125ms)        12,14,15,17,17(1150ms)
	 */
	public static void main(String[] args) {
		final String context = "ou=People,ou=example,o=com";
		
		int times = 0;
		int givenCount = 50000 * (1 << times);
		int addCount = 20000 * (1 << times);
		int removeCount = 20000 * (1 << times);
		int offset = givenCount * 4/10;
		
		System.out.println("givenCount: " + givenCount);
		System.out.println("addCount: " + addCount);
		System.out.println("removeCount: " + removeCount);
		System.out.println("offset: " + offset);
		
		Set<String> given = new HashSet<String>();
		for (int i = 0; i < givenCount; i++) {
			given.add(String.format("uid=%8d,%s", i, context));
		}
		
		final Random rand = new Random(37);
		long start = System.currentTimeMillis();
		ChangeObservedSet<String> observed = new ChangeObservedSet<String>(given);
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
