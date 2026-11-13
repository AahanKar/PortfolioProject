import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;

/**
 * {@code Portfolio} kernel implementation, layered on a {@code Map} that sends
 * each section name to the {@code Queue} of entries in that section.
 *
 * @convention <pre>
 * $this.sections is not null  and
 * no key of $this.sections is null  and
 * no value of $this.sections is null  and
 * no entry in any queue in $this.sections is null
 * </pre>
 * @correspondence <pre>
 * this = $this.sections
 * (each key of $this.sections is the name of a section of this, and its value
 *  is the entries of that section, in order from first to last)
 * </pre>
 */
public class Portfolio1L extends PortfolioSecondary {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Representation: section name -> entries of that section, in order.
     */
    private Map<String, Queue<String>> sections;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.sections = new Map1L<String, Queue<String>>();
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor: creates an empty portfolio.
     */
    public Portfolio1L() {
        this.createNewRep();
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final Portfolio newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Portfolio source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Portfolio1L : ""
                + "Violation of: source is of dynamic type Portfolio1L";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Portfolio1L.
         */
        Portfolio1L localSource = (Portfolio1L) source;
        this.sections = localSource.sections;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void addSection(String name, Queue<String> entries) {
        assert name != null : "Violation of: name is not null";
        assert entries != null : "Violation of: entries is not null";
        assert !this.sections.hasKey(name) : ""
                + "Violation of: name is not the name of a section in this";

        // take over the entries, leaving the caller's queue empty (cleared)
        Queue<String> mine = new Queue1L<String>();
        mine.transferFrom(entries);
        this.sections.add(name, mine);
    }

    @Override
    public final Queue<String> removeSection(String name) {
        assert name != null : "Violation of: name is not null";
        assert this.sections.hasKey(name) : ""
                + "Violation of: name is the name of a section in this";

        Map.Pair<String, Queue<String>> removed = this.sections.remove(name);
        return removed.value();
    }

    @Override
    public final Map.Pair<String, Queue<String>> removeAnySection() {
        assert this.sections.size() > 0 : ""
                + "Violation of: this has at least one section";

        return this.sections.removeAny();
    }

    @Override
    public final boolean hasSection(String name) {
        assert name != null : "Violation of: name is not null";

        return this.sections.hasKey(name);
    }

    @Override
    public final int numberOfSections() {
        return this.sections.size();
    }

}
