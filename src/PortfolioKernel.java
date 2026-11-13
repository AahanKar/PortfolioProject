import components.map.Map;
import components.queue.Queue;
import components.standard.Standard;

/**
 * Kernel interface for the {@code Portfolio} component: a personal portfolio
 * made of named sections, where each section holds an ordered sequence of text
 * entries (for example, a "Projects" section with one entry per project).
 *
 * @mathmodel type PortfolioKernel is modeled by finite function from String to
 *            string of String (each section name maps to the entries of that
 *            section, in order)
 * @initially
 *
 *            <pre>
 * ensures
 *  this = {}
 *            </pre>
 */

public interface PortfolioKernel extends Standard<Portfolio> {
    // Suggested section name for personal background.
    String ABOUT_ME = "About Me";
    // Suggested section name for hobbies and interests.
    String INTERESTS = "Interests";
    // Suggested section name for projects.
    String PROJECTS = "Projects";
    // Suggested section name for future goals.
    String GOALS = "Goals";

    /**
     * Adds a new section called {@code name} that holds {@code entries}.
     *
     * @param name
     *            the name of the new section
     * @param entries
     *            the entries of the new section, in order
     * @updates this
     * @clears entries
     * @requires <pre>
     * name is not the name of a section in this
     * </pre>
     * @ensures <pre>
     * this = #this with an added section called name
     *        whose entries are #entries
     * </pre>
     */
    void addSection(String name, Queue<String> entries);

    /**
     * Removes the section called {@code name} and reports its entries.
     *
     * @param name
     *            the name of the section to remove
     * @return the entries of the removed section, in order
     * @updates this
     * @requires <pre>
     * name is the name of a section in this
     * </pre>
     * @ensures <pre>
     * this = #this without the section called name  and
     * removeSection = the entries of the section called name in #this
     * </pre>
     */
    Queue<String> removeSection(String name);

    /**
     * Removes an arbitrary section and reports its name and entries.
     *
     * @return a pair of the removed section's name and its entries, in order
     * @updates this
     * @requires <pre>
     * this has at least one section
     * </pre>
     * @ensures <pre>
     * removeAnySection is the (name, entries) pair of some section in #this  and
     * this = #this without that section
     * </pre>
     */
    Map.Pair<String, Queue<String>> removeAnySection();

    /**
     * Reports whether {@code this} has a section called {@code name}.
     *
     * @param name
     *            the section name to look for
     * @return true if there is a section called {@code name}, false otherwise
     * @ensures <pre>
     * hasSection = (name is the name of a section in this)
     * </pre>
     */
    boolean hasSection(String name);

    /**
     * Reports the number of sections in {@code this}.
     *
     * @return the number of sections
     * @ensures <pre>
     * numberOfSections = the number of sections in this
     * </pre>
     */
    int numberOfSections();

}
