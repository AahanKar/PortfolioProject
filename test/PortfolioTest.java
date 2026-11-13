import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.queue.Queue;
import components.queue.Queue1L;

/**
 * JUnit tests for the secondary methods and common {@code Object} methods of
 * {@code PortfolioSecondary}, exercised through {@code Portfolio1L}.
 *
 * Without a reference implementation, each test builds a portfolio with
 * {@code addEntry} and checks results against other portfolios built the same
 * way, so state changes are caught by {@code equals}.
 */
public class PortfolioTest {

    /**
     * Creates a portfolio with the given section and entries.
     *
     * @param section
     *            the section name
     * @param entries
     *            the entries to add to that section, in order
     * @return a new portfolio
     */
    private static Portfolio portfolioWith(String section, String... entries) {
        Portfolio p = new Portfolio1L();
        for (String entry : entries) {
            p.addEntry(section, entry);
        }
        return p;
    }

    /**
     * Creates a portfolio used by several tests.
     *
     * @return a portfolio with three sections and four entries
     */
    private static Portfolio sample() {
        Portfolio p = new Portfolio1L();
        p.addEntry("Projects", "House price model");
        p.addEntry("Projects", "Diabetes model");
        p.addEntry("Goals", "ML engineering role");
        p.addEntry("About Me", "OSU student");
        return p;
    }

    /*
     * addEntry
     */

    @Test
    public void testAddEntryCreatesSection() {
        Portfolio p = new Portfolio1L();
        p.addEntry("Goals", "A");
        assertTrue(p.hasSection("Goals"));
        assertEquals(1, p.numberOfEntries("Goals"));
    }

    @Test
    public void testAddEntryAppendsToEnd() {
        Portfolio p = portfolioWith("Goals", "A", "B");
        p.addEntry("Goals", "C");
        Queue<String> expected = new Queue1L<String>();
        expected.enqueue("A");
        expected.enqueue("B");
        expected.enqueue("C");
        assertEquals(expected, p.removeSection("Goals"));
    }

    @Test
    public void testAddEntryLeavesOtherSectionsAlone() {
        Portfolio p = sample();
        p.addEntry("Goals", "extra");
        assertEquals(3, p.numberOfSections());
        assertEquals(2, p.numberOfEntries("Projects"));
        assertEquals(1, p.numberOfEntries("About Me"));
    }

    /*
     * numberOfEntries and totalEntries
     */

    @Test
    public void testNumberOfEntries() {
        Portfolio p = sample();
        assertEquals(2, p.numberOfEntries("Projects"));
        assertEquals(1, p.numberOfEntries("Goals"));
    }

    @Test
    public void testNumberOfEntriesRestoresPortfolio() {
        Portfolio p = sample();
        p.numberOfEntries("Projects");
        assertEquals(sample(), p);
    }

    @Test
    public void testTotalEntriesEmpty() {
        assertEquals(0, new Portfolio1L().totalEntries());
    }

    @Test
    public void testTotalEntries() {
        assertEquals(4, sample().totalEntries());
    }

    @Test
    public void testTotalEntriesRestoresPortfolio() {
        Portfolio p = sample();
        p.totalEntries();
        assertEquals(sample(), p);
    }

    @Test
    public void testTotalEntriesCountsEmptySectionAsZero() {
        Portfolio p = sample();
        p.addSection("Empty", new Queue1L<String>());
        assertEquals(4, p.totalEntries());
    }

    /*
     * sectionNames
     */

    @Test
    public void testSectionNamesEmpty() {
        assertEquals(0, new Portfolio1L().sectionNames().length());
    }

    @Test
    public void testSectionNamesAreSorted() {
        Queue<String> names = sample().sectionNames();
        assertEquals(3, names.length());
        assertEquals("About Me", names.dequeue());
        assertEquals("Goals", names.dequeue());
        assertEquals("Projects", names.dequeue());
    }

    @Test
    public void testSectionNamesRestoresPortfolio() {
        Portfolio p = sample();
        p.sectionNames();
        assertEquals(sample(), p);
    }

    @Test
    public void testSectionNamesUppercaseBeforeLowercase() {
        Portfolio p = new Portfolio1L();
        p.addEntry("apple", "a");
        p.addEntry("Zebra", "z");
        Queue<String> names = p.sectionNames();
        assertEquals("Zebra", names.dequeue());
        assertEquals("apple", names.dequeue());
    }

    /*
     * renameSection
     */

    @Test
    public void testRenameSectionMovesEntries() {
        Portfolio p = portfolioWith("Goals", "A", "B");
        p.renameSection("Goals", "Future Goals");
        assertFalse(p.hasSection("Goals"));
        assertEquals(portfolioWith("Future Goals", "A", "B"), p);
    }

    @Test
    public void testRenameSectionLeavesOthersAlone() {
        Portfolio p = sample();
        p.renameSection("Goals", "Future Goals");
        assertEquals(3, p.numberOfSections());
        assertEquals(2, p.numberOfEntries("Projects"));
    }

    /*
     * merge
     */

    @Test
    public void testMergeEmptyIntoPortfolio() {
        Portfolio p = sample();
        p.merge(new Portfolio1L());
        assertEquals(sample(), p);
    }

    @Test
    public void testMergeIntoEmptyPortfolio() {
        Portfolio p = new Portfolio1L();
        p.merge(sample());
        assertEquals(sample(), p);
    }

    @Test
    public void testMergeClearsOther() {
        Portfolio p = sample();
        Portfolio other = portfolioWith("Volunteering", "OneInMath");
        p.merge(other);
        assertEquals(0, other.numberOfSections());
    }

    @Test
    public void testMergeDifferentSections() {
        Portfolio p = portfolioWith("Goals", "A");
        p.merge(portfolioWith("Projects", "B"));
        assertEquals(2, p.numberOfSections());
        assertEquals(1, p.numberOfEntries("Goals"));
        assertEquals(1, p.numberOfEntries("Projects"));
    }

    @Test
    public void testMergeSameSectionKeepsThisEntriesFirst() {
        Portfolio p = portfolioWith("Projects", "A", "B");
        p.merge(portfolioWith("Projects", "C"));
        assertEquals(portfolioWith("Projects", "A", "B", "C"), p);
    }

    /*
     * toMarkdown
     */

    @Test
    public void testToMarkdownEmpty() {
        assertEquals("", new Portfolio1L().toMarkdown());
    }

    @Test
    public void testToMarkdownOneSection() {
        Portfolio p = portfolioWith("Goals", "A", "B");
        assertEquals("## Goals\n\n- A\n- B\n\n", p.toMarkdown());
    }

    @Test
    public void testToMarkdownSortsSections() {
        String md = sample().toMarkdown();
        assertTrue(md.indexOf("## About Me") < md.indexOf("## Goals"));
        assertTrue(md.indexOf("## Goals") < md.indexOf("## Projects"));
    }

    @Test
    public void testToMarkdownKeepsEntryOrderAndRestoresPortfolio() {
        Portfolio p = sample();
        String md = p.toMarkdown();
        assertTrue(md.contains("- House price model\n- Diabetes model\n"));
        assertEquals(sample(), p);
    }

    /*
     * toString
     */

    @Test
    public void testToStringEmpty() {
        assertEquals("{}", new Portfolio1L().toString());
    }

    @Test
    public void testToStringOneSection() {
        assertEquals("{Goals=[A, B]}",
                portfolioWith("Goals", "A", "B").toString());
    }

    @Test
    public void testToStringSortedAndRestoresPortfolio() {
        Portfolio p = sample();
        assertEquals(
                "{About Me=[OSU student], Goals=[ML engineering role], "
                        + "Projects=[House price model, Diabetes model]}",
                p.toString());
        assertEquals(sample(), p);
    }

    /*
     * equals and hashCode
     */

    @Test
    public void testEqualsSameObject() {
        Portfolio p = sample();
        assertTrue(p.equals(p));
    }

    @Test
    public void testEqualsTwoEmptyPortfolios() {
        assertEquals(new Portfolio1L(), new Portfolio1L());
    }

    @Test
    public void testEqualsSameContents() {
        assertEquals(sample(), sample());
    }

    @Test
    public void testEqualsIgnoresInsertionOrderOfSections() {
        Portfolio a = new Portfolio1L();
        a.addEntry("Goals", "A");
        a.addEntry("Projects", "B");
        Portfolio b = new Portfolio1L();
        b.addEntry("Projects", "B");
        b.addEntry("Goals", "A");
        assertEquals(a, b);
    }

    @Test
    public void testNotEqualDifferentEntries() {
        assertFalse(portfolioWith("Goals", "A")
                .equals(portfolioWith("Goals", "B")));
    }

    @Test
    public void testNotEqualDifferentEntryOrder() {
        assertFalse(portfolioWith("Goals", "A", "B")
                .equals(portfolioWith("Goals", "B", "A")));
    }

    @Test
    public void testNotEqualDifferentSectionNames() {
        assertFalse(
                portfolioWith("Goals", "A").equals(portfolioWith("Aims", "A")));
    }

    @Test
    public void testNotEqualDifferentNumberOfSections() {
        Portfolio bigger = portfolioWith("Goals", "A");
        bigger.addEntry("Projects", "B");
        assertFalse(portfolioWith("Goals", "A").equals(bigger));
    }

    @Test
    public void testNotEqualNullOrOtherType() {
        Portfolio p = sample();
        assertFalse(p.equals(null));
        assertFalse(p.equals("not a portfolio"));
    }

    @Test
    public void testEqualsRestoresBothPortfolios() {
        Portfolio a = sample();
        Portfolio b = portfolioWith("Goals", "different");
        a.equals(b);
        assertEquals(sample(), a);
        assertEquals(portfolioWith("Goals", "different"), b);
    }

    @Test
    public void testHashCodeMatchesForEqualPortfolios() {
        assertEquals(sample().hashCode(), sample().hashCode());
    }

    @Test
    public void testHashCodeDoesNotChangeState() {
        Portfolio p = sample();
        p.hashCode();
        assertEquals(sample(), p);
    }

}
