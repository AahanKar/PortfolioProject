import components.queue.Queue;

/**
 * Enhanced interface for the {@code Portfolio} component. Every method here can
 * be implemented using only the kernel methods of {@link PortfolioKernel} and
 * the methods of {@code Standard}.
 */
public interface Portfolio extends PortfolioKernel {
    /**
     * Adds {@code entry} to the end of the section called {@code sectionName},
     * creating the section if it does not exist.
     *
     * @param sectionName
     *            the name of the section to add to
     * @param entry
     *            the entry to add
     * @updates this
     * @ensures <pre>
     * if sectionName is the name of a section in #this then
     *   this = #this with entry added to the end of the entries
     *          of the section called sectionName
     * else
     *   this = #this with a new section called sectionName
     *          whose only entry is entry
     * </pre>
     */
    void addEntry(String sectionName, String entry);

    /**
     * Reports the number of entries in the section called {@code sectionName}.
     *
     * @param sectionName
     *            the name of the section to count
     * @return the number of entries in that section
     * @requires <pre>
     * sectionName is the name of a section in this
     * </pre>
     * @ensures <pre>
     * numberOfEntries = the number of entries in the section
     *                   called sectionName
     * </pre>
     */
    int numberOfEntries(String sectionName);

    /**
     * Reports the total number of entries across all sections of {@code this}.
     *
     * @return the total number of entries
     * @ensures <pre>
     * totalEntries = the sum of the number of entries in each section of this
     * </pre>
     */
    int totalEntries();

    /**
     * Reports the names of all sections in {@code this}, in alphabetical order.
     *
     * @return the section names in alphabetical order
     * @ensures <pre>
     * sectionNames = the names of the sections in this,
     *                in alphabetical order
     * </pre>
     */
    Queue<String> sectionNames();

    /**
     * Renames the section called {@code oldName} to {@code newName}.
     *
     * @param oldName
     *            the current name of the section
     * @param newName
     *            the new name for the section
     * @updates this
     * @requires <pre>
     * oldName is the name of a section in this  and
     * newName is not the name of a section in this
     * </pre>
     * @ensures <pre>
     * this = #this with the section called oldName renamed to newName
     *        and its entries unchanged
     * </pre>
     */
    void renameSection(String oldName, String newName);

    /**
     * Moves all sections and entries of {@code other} into {@code this}. If
     * both portfolios have a section with the same name, the entries from
     * {@code other} are added after the entries already in {@code this}.
     *
     * @param other
     *            the portfolio to merge into this one
     * @updates this
     * @clears other
     * @requires <pre>
     * other is not the same object as this
     * </pre>
     * @ensures <pre>
     * this has every section name that is in #this or in #other  and
     * for each section name, its entries are the entries from #this
     *   (if any) followed by the entries from #other (if any)  and
     * other = {}
     * </pre>
     */
    void merge(Portfolio other);

    /**
     * Reports {@code this} as Markdown text.
     *
     * @return the Markdown text for this portfolio
     * @ensures <pre>
     * toMarkdown = for each section of this, in alphabetical order by name:
     *   a heading line "## " followed by the section name, a blank line,
     *   one bullet line "- " followed by each entry (in order),
     *   and a blank line
     * </pre>
     */
    String toMarkdown();

}
