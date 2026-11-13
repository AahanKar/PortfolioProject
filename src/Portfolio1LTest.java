import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import components.map.Map;
import components.queue.Queue;
import components.queue.Queue1L;

/**
 * JUnit tests for the kernel and {@code Standard} methods of
 * {@code Portfolio1L}.
 *
 * There is no reference implementation to compare against, so each test builds
 * its portfolio with kernel methods only and then checks the state with other
 * kernel methods.
 */
public class Portfolio1LTest {

    /**
     * Creates a queue holding {@code entries} in order.
     *
     * @param entries
     *            the entries to put in the queue
     * @return a new queue with the given entries
     */
    private static Queue<String> queueOf(String... entries) {
        Queue<String> queue = new Queue1L<String>();
        for (String entry : entries) {
            queue.enqueue(entry);
        }
        return queue;
    }

    /*
     * Constructor
     */

    @Test
    public void testConstructorCreatesEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        assertEquals(0, p.numberOfSections());
    }

    /*
     * addSection
     */

    @Test
    public void testAddSectionToEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B"));
        assertEquals(1, p.numberOfSections());
        assertTrue(p.hasSection("Projects"));
    }

    @Test
    public void testAddSectionClearsEntriesArgument() {
        Portfolio p = new Portfolio1L();
        Queue<String> entries = queueOf("A", "B");
        p.addSection("Projects", entries);
        assertEquals(0, entries.length());
    }

    @Test
    public void testAddSectionKeepsEntriesInOrder() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B", "C"));
        assertEquals(queueOf("A", "B", "C"), p.removeSection("Projects"));
    }

    @Test
    public void testAddSectionWithNoEntries() {
        Portfolio p = new Portfolio1L();
        p.addSection("Empty", queueOf());
        assertTrue(p.hasSection("Empty"));
        assertEquals(0, p.removeSection("Empty").length());
    }

    @Test
    public void testAddTwoSections() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.addSection("Goals", queueOf("B", "C"));
        assertEquals(2, p.numberOfSections());
        assertTrue(p.hasSection("Projects"));
        assertTrue(p.hasSection("Goals"));
    }

    @Test
    public void testAddSectionAfterRemovingSameName() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.removeSection("Projects");
        p.addSection("Projects", queueOf("B"));
        assertEquals(queueOf("B"), p.removeSection("Projects"));
    }

    /*
     * removeSection
     */

    @Test
    public void testRemoveOnlySection() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B"));
        Queue<String> removed = p.removeSection("Projects");
        assertEquals(queueOf("A", "B"), removed);
        assertEquals(0, p.numberOfSections());
        assertFalse(p.hasSection("Projects"));
    }

    @Test
    public void testRemoveSectionLeavesOtherSectionsAlone() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.addSection("Goals", queueOf("B", "C"));
        p.removeSection("Projects");
        assertEquals(1, p.numberOfSections());
        assertFalse(p.hasSection("Projects"));
        assertEquals(queueOf("B", "C"), p.removeSection("Goals"));
    }

    /*
     * removeAnySection
     */

    @Test
    public void testRemoveAnySectionWithOneSection() {
        Portfolio p = new Portfolio1L();
        p.addSection("Goals", queueOf("X", "Y"));
        Map.Pair<String, Queue<String>> pair = p.removeAnySection();
        assertEquals("Goals", pair.key());
        assertEquals(queueOf("X", "Y"), pair.value());
        assertEquals(0, p.numberOfSections());
    }

    @Test
    public void testRemoveAnySectionEventuallyRemovesEverything() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.addSection("Goals", queueOf("B"));
        p.addSection("About Me", queueOf("C"));
        Set<String> removedNames = new HashSet<String>();
        while (p.numberOfSections() > 0) {
            removedNames.add(p.removeAnySection().key());
        }
        Set<String> expected = new HashSet<String>();
        expected.add("Projects");
        expected.add("Goals");
        expected.add("About Me");
        assertEquals(expected, removedNames);
    }

    @Test
    public void testRemoveAnySectionReducesCountByOne() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.addSection("Goals", queueOf("B"));
        p.removeAnySection();
        assertEquals(1, p.numberOfSections());
    }

    /*
     * hasSection and numberOfSections
     */

    @Test
    public void testHasSectionOnEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        assertFalse(p.hasSection("Projects"));
    }

    @Test
    public void testHasSectionIsCaseSensitive() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        assertTrue(p.hasSection("Projects"));
        assertFalse(p.hasSection("projects"));
    }

    @Test
    public void testHasSectionDoesNotChangeState() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B"));
        p.hasSection("Projects");
        p.hasSection("Goals");
        assertEquals(1, p.numberOfSections());
        assertEquals(queueOf("A", "B"), p.removeSection("Projects"));
    }

    @Test
    public void testNumberOfSectionsDoesNotChangeState() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B"));
        assertEquals(1, p.numberOfSections());
        assertEquals(1, p.numberOfSections());
        assertEquals(queueOf("A", "B"), p.removeSection("Projects"));
    }

    /*
     * Standard: clear
     */

    @Test
    public void testClearOnEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        p.clear();
        assertEquals(0, p.numberOfSections());
    }

    @Test
    public void testClearOnNonEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.addSection("Goals", queueOf("B"));
        p.clear();
        assertEquals(0, p.numberOfSections());
        assertFalse(p.hasSection("Projects"));
    }

    @Test
    public void testPortfolioIsUsableAfterClear() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        p.clear();
        p.addSection("Projects", queueOf("B"));
        assertEquals(queueOf("B"), p.removeSection("Projects"));
    }

    /*
     * Standard: newInstance
     */

    @Test
    public void testNewInstanceIsEmpty() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        Portfolio fresh = p.newInstance();
        assertEquals(0, fresh.numberOfSections());
    }

    @Test
    public void testNewInstanceHasSameTypeAndDoesNotAlias() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A"));
        Portfolio fresh = p.newInstance();
        assertTrue(fresh instanceof Portfolio1L);
        assertNotSame(p, fresh);
        fresh.addSection("Goals", queueOf("B"));
        assertEquals(1, p.numberOfSections());
        assertFalse(p.hasSection("Goals"));
    }

    @Test
    public void testNewInstanceLeavesOriginalUnchanged() {
        Portfolio p = new Portfolio1L();
        p.addSection("Projects", queueOf("A", "B"));
        p.newInstance();
        assertEquals(1, p.numberOfSections());
        assertEquals(queueOf("A", "B"), p.removeSection("Projects"));
    }

    /*
     * Standard: transferFrom
     */

    @Test
    public void testTransferFromIntoEmptyPortfolio() {
        Portfolio source = new Portfolio1L();
        source.addSection("Projects", queueOf("A", "B"));
        source.addSection("Goals", queueOf("C"));
        Portfolio target = new Portfolio1L();
        target.transferFrom(source);
        assertEquals(2, target.numberOfSections());
        assertEquals(queueOf("A", "B"), target.removeSection("Projects"));
        assertEquals(queueOf("C"), target.removeSection("Goals"));
    }

    @Test
    public void testTransferFromClearsSource() {
        Portfolio source = new Portfolio1L();
        source.addSection("Projects", queueOf("A"));
        Portfolio target = new Portfolio1L();
        target.transferFrom(source);
        assertEquals(0, source.numberOfSections());
    }

    @Test
    public void testTransferFromReplacesExistingContents() {
        Portfolio source = new Portfolio1L();
        source.addSection("Goals", queueOf("X"));
        Portfolio target = new Portfolio1L();
        target.addSection("Projects", queueOf("A"));
        target.transferFrom(source);
        assertEquals(1, target.numberOfSections());
        assertFalse(target.hasSection("Projects"));
        assertTrue(target.hasSection("Goals"));
    }

    @Test
    public void testSourceIsUsableAfterTransferFrom() {
        Portfolio source = new Portfolio1L();
        source.addSection("Projects", queueOf("A"));
        Portfolio target = new Portfolio1L();
        target.transferFrom(source);
        source.addSection("Goals", queueOf("B"));
        assertEquals(1, source.numberOfSections());
        assertEquals(1, target.numberOfSections());
        assertTrue(target.hasSection("Projects"));
        assertFalse(target.hasSection("Goals"));
    }

    @Test
    public void testTransferFromEmptySource() {
        Portfolio source = new Portfolio1L();
        Portfolio target = new Portfolio1L();
        target.addSection("Projects", queueOf("A"));
        target.transferFrom(source);
        assertEquals(0, target.numberOfSections());
    }

}
