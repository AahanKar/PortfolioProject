import java.util.Comparator;

import components.map.Map;
import components.queue.Queue;
import components.queue.Queue1L;

/**
 * Layered implementations of the secondary methods (and the common
 * {@code Object} methods) for {@code Portfolio}.
 *
 * Every method here is built ONLY from kernel methods ({@code addSection},
 * {@code removeSection}, {@code removeAnySection}, {@code hasSection},
 * {@code numberOfSections}) and {@code Standard} methods ({@code newInstance},
 * {@code transferFrom}, {@code clear}). Nothing here touches a representation.
 *
 * Whenever a kernel method has a precondition, the calling code either checks
 * it with an {@code assert} or explains in a comment why it must hold.
 */
public abstract class PortfolioSecondary implements Portfolio {

    /**
     * Orders section names alphabetically using {@code String.compareTo} (so
     * uppercase letters sort before lowercase letters).
     */
    private static final class SectionNameOrder implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    /*
     * Secondary methods
     */

    @Override
    public final void addEntry(String sectionName, String entry) {
        assert sectionName != null : "Violation of: sectionName is not null";
        assert entry != null : "Violation of: entry is not null";

        Queue<String> entries;
        if (this.hasSection(sectionName)) {
            // removeSection requires the section to exist: checked just above
            entries = this.removeSection(sectionName);
        } else {
            entries = new Queue1L<String>();
        }
        entries.enqueue(entry);
        // addSection requires the name to be unused: either it was never in
        // this, or we just removed it
        this.addSection(sectionName, entries);
    }

    @Override
    public final int numberOfEntries(String sectionName) {
        assert sectionName != null : "Violation of: sectionName is not null";
        assert this.hasSection(sectionName) : ""
                + "Violation of: sectionName is the name of a section in this";

        // removeSection requires the section to exist: asserted above
        Queue<String> entries = this.removeSection(sectionName);
        int count = entries.length();
        // addSection requires the name to be unused: we just removed it
        this.addSection(sectionName, entries);
        return count;
    }

    @Override
    public final int totalEntries() {
        Portfolio temp = this.newInstance();
        int total = 0;
        int sections = this.numberOfSections();
        for (int i = 0; i < sections; i++) {
            // removeAnySection requires at least one section: the loop runs
            // exactly numberOfSections() times, removing one each time
            Map.Pair<String, Queue<String>> section = this.removeAnySection();
            total += section.value().length();
            // addSection requires an unused name: section names are distinct
            temp.addSection(section.key(), section.value());
        }
        this.transferFrom(temp); // restore this
        return total;
    }

    @Override
    public final Queue<String> sectionNames() {
        Queue<String> names = new Queue1L<String>();
        Portfolio temp = this.newInstance();
        int sections = this.numberOfSections();
        for (int i = 0; i < sections; i++) {
            // requires at least one section: loop runs numberOfSections() times
            Map.Pair<String, Queue<String>> section = this.removeAnySection();
            names.enqueue(section.key());
            // requires an unused name: section names are distinct
            temp.addSection(section.key(), section.value());
        }
        this.transferFrom(temp); // restore this
        names.sort(new SectionNameOrder());
        return names;
    }

    @Override
    public final void renameSection(String oldName, String newName) {
        assert oldName != null : "Violation of: oldName is not null";
        assert newName != null : "Violation of: newName is not null";
        assert this.hasSection(oldName) : ""
                + "Violation of: oldName is the name of a section in this";
        assert !this.hasSection(newName) : ""
                + "Violation of: newName is not the name of a section in this";

        // both kernel preconditions are asserted above
        Queue<String> entries = this.removeSection(oldName);
        this.addSection(newName, entries);
    }

    @Override
    public final void merge(Portfolio other) {
        assert other != null : "Violation of: other is not null";
        assert other != this : "Violation of: other is not this";

        int sections = other.numberOfSections();
        for (int i = 0; i < sections; i++) {
            // requires at least one section: loop runs numberOfSections() times
            Map.Pair<String, Queue<String>> section = other.removeAnySection();
            String name = section.key();
            Queue<String> incoming = section.value();
            if (this.hasSection(name)) {
                // removeSection requires the section to exist: checked above
                Queue<String> existing = this.removeSection(name);
                existing.append(incoming); // existing first, then incoming
                // addSection requires an unused name: we just removed it
                this.addSection(name, existing);
            } else {
                // addSection requires an unused name: checked above
                this.addSection(name, incoming);
            }
        }
        // other is now empty because every section was removed
    }

    @Override
    public final String toMarkdown() {
        StringBuilder markdown = new StringBuilder();
        Queue<String> names = this.sectionNames();
        int sections = names.length();
        for (int i = 0; i < sections; i++) {
            String name = names.dequeue();
            // removeSection requires the section to exist: the name came
            // from sectionNames() and nothing else has been removed
            Queue<String> entries = this.removeSection(name);
            markdown.append("## ").append(name).append("\n\n");
            int count = entries.length();
            for (int j = 0; j < count; j++) {
                String entry = entries.dequeue();
                markdown.append("- ").append(entry).append("\n");
                entries.enqueue(entry); // rotate so the order is preserved
            }
            markdown.append("\n");
            // addSection requires an unused name: we just removed it
            this.addSection(name, entries);
        }
        return markdown.toString();
    }
    /*
     * Common Object methods
     */

    /**
     * Reports this portfolio in a compact form such as
     * {@code {Goals=[a, b], Projects=[c]}}, with sections in alphabetical
     * order. Use {@link #toMarkdown()} for a readable document.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        Queue<String> names = this.sectionNames();
        int sections = names.length();
        for (int i = 0; i < sections; i++) {
            String name = names.dequeue();
            // removeSection requires the section to exist: name came from
            // sectionNames()
            Queue<String> entries = this.removeSection(name);
            if (i > 0) {
                result.append(", ");
            }
            result.append(name).append("=[");
            int count = entries.length();
            for (int j = 0; j < count; j++) {
                String entry = entries.dequeue();
                if (j > 0) {
                    result.append(", ");
                }
                result.append(entry);
                entries.enqueue(entry); // rotate so the order is preserved
            }
            result.append("]");
            // addSection requires an unused name: we just removed it
            this.addSection(name, entries);
        }
        result.append("}");
        return result.toString();
    }

    /**
     * Two portfolios are equal when they have the same section names and each
     * section holds the same entries in the same order. Both portfolios are
     * restored to their original values before returning.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Portfolio)) {
            return false;
        }
        Portfolio other = (Portfolio) obj;
        if (this.numberOfSections() != other.numberOfSections()) {
            return false;
        }
        // Same number of sections, so if every section of this is in other
        // with equal entries, the portfolios are equal. We keep looping
        // after a mismatch so that both portfolios are fully restored.
        boolean result = true;
        Queue<String> names = this.sectionNames();
        int sections = names.length();
        for (int i = 0; i < sections; i++) {
            String name = names.dequeue();
            // removeSection requires the section to exist: name came from
            // sectionNames()
            Queue<String> mine = this.removeSection(name);
            if (other.hasSection(name)) {
                Queue<String> theirs = other.removeSection(name);
                if (!mine.equals(theirs)) {
                    result = false;
                }
                // addSection requires an unused name: we just removed it
                other.addSection(name, theirs);
            } else {
                result = false;
            }
            this.addSection(name, mine);
        }
        return result;
    }

    /**
     * Consistent with {@link #equals(Object)}: does not depend on the order of
     * sections, but does depend on the order of entries within a section.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int total = 0;
        Queue<String> names = this.sectionNames();
        int sections = names.length();
        for (int i = 0; i < sections; i++) {
            String name = names.dequeue();
            // removeSection requires the section to exist: name came from
            // sectionNames()
            Queue<String> entries = this.removeSection(name);
            int sectionHash = name.hashCode();
            int count = entries.length();
            for (int j = 0; j < count; j++) {
                String entry = entries.dequeue();
                sectionHash = prime * sectionHash + entry.hashCode();
                entries.enqueue(entry); // rotate so the order is preserved
            }
            total += sectionHash;
            // addSection requires an unused name: we just removed it
            this.addSection(name, entries);
        }
        return total;
    }

}
