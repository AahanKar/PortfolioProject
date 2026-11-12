import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class Portfolio {
    private final TreeMap<String, Queue<String>> sections = new TreeMap<>();

    /**
     * Adds a section called {@code name} holding {@code entries}. Requires that
     * no section called {@code name} exists. Ensures {@code entries} is empty
     * afterward (consumed).
     */

    public void addSection(String name, Queue<String> entries) {
        if (this.sections.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Section already exists: " + name);
        }
        this.sections.put(name, new LinkedList<>(entries));
        entries.clear();
    }

    /**
     * Removes the section called {@code name} and returns its entries. Requires
     * that the section exists.
     */
    public Queue<String> removeSection(String name) {
        if (!this.sections.containsKey(name)) {
            throw new IllegalArgumentException("No such section: " + name);
        }
        return this.sections.remove(name);
    }

    /**
     * Removes and returns an arbitrary section (name and entries). Requires
     * that this portfolio has at least one section.
     */
    public Map.Entry<String, Queue<String>> removeAnySection() {
        if (this.sections.isEmpty()) {
            throw new IllegalStateException("Portfolio has no sections");
        }
        return this.sections.pollFirstEntry();
    }

    /** Reports whether a section called {@code name} exists. */
    public boolean hasSection(String name) {
        return this.sections.containsKey(name);
    }

    /** Reports the number of sections. */
    public int numberOfSections() {
        return this.sections.size();
    }

    /**
     * Adds {@code entry} to the end of section {@code sectionName}, creating it
     * if needed.
     */
    public void addEntry(String sectionName, String entry) {
        Queue<String> entries;
        if (this.hasSection(sectionName)) {
            entries = this.removeSection(sectionName);
        } else {
            entries = new LinkedList<>();
        }
        entries.add(entry);
        this.addSection(sectionName, entries);
    }

    /**
     * Reports how many entries are in section {@code sectionName} (0 if it
     * doesn't exist).
     */
    public int numberOfEntries(String sectionName) {
        int count = 0;
        if (this.hasSection(sectionName)) {
            Queue<String> entries = this.removeSection(sectionName);
            count = entries.size();
            this.addSection(sectionName, entries);
        }
        return count;
    }

    /** Reports the total number of entries across all sections. */
    public int totalEntries() {
        Portfolio temp = new Portfolio();
        int total = 0;
        int n = this.numberOfSections();
        for (int i = 0; i < n; i++) {
            Map.Entry<String, Queue<String>> section = this.removeAnySection();
            total += section.getValue().size();
            temp.addSection(section.getKey(), section.getValue());
        }
        this.merge(temp); // restore
        return total;
    }

    /**
     * Renames section {@code oldName} to {@code newName}. Requires oldName
     * exists and newName doesn't.
     */
    public void renameSection(String oldName, String newName) {
        Queue<String> entries = this.removeSection(oldName);
        this.addSection(newName, entries);
    }

    /**
     * Moves all sections of {@code other} into this portfolio. If both have a
     * section with the same name, the entries are combined. Afterward,
     * {@code other} is empty.
     */
    public void merge(Portfolio other) {
        int n = other.numberOfSections();
        for (int i = 0; i < n; i++) {
            Map.Entry<String, Queue<String>> section = other.removeAnySection();
            for (String entry : section.getValue()) {
                this.addEntry(section.getKey(), entry);
            }
        }
    }

    /**
     * Reports this portfolio as Markdown: one heading per section, one bullet
     * per entry.
     */
    public String toMarkdown() {
        StringBuilder md = new StringBuilder();
        Portfolio temp = new Portfolio();
        int n = this.numberOfSections();
        for (int i = 0; i < n; i++) {
            Map.Entry<String, Queue<String>> section = this.removeAnySection();
            md.append("## ").append(section.getKey()).append("\n\n");
            for (String entry : section.getValue()) {
                md.append("- ").append(entry).append("\n");
            }
            md.append("\n");
            temp.addSection(section.getKey(), section.getValue());
        }
        this.merge(temp); // restore
        return md.toString();
    }

    public static void main(String[] args) {
        // Use case 1: build my own portfolio and print it as Markdown.
        Portfolio me = new Portfolio();
        me.addEntry("About Me",
                "Data Analytics student (Computational Analytics) at The Ohio State University");
        me.addEntry("About Me", "Expected graduation: May 2029");
        me.addEntry("Interests", "Basketball and music");
        me.addEntry("Interests",
                "AI-powered tools and turning messy data into insight");
        me.addEntry("Projects", "House price prediction model (98% R^2)");
        me.addEntry("Projects",
                "Diabetes classification model, presented at the BDAA Galleria");
        me.addEntry("Projects",
                "Agentic GRC platform with a knowledge graph and RAG");
        me.addEntry("Goals", "Land a data science or AI/ML engineering role");
        System.out.println("Sections: " + me.numberOfSections());
        System.out.println("Total entries: " + me.totalEntries());
        System.out
                .println("Project entries: " + me.numberOfEntries("Projects"));
        System.out.println();
        System.out.println(me.toMarkdown());
        // Use case 2: merge a second portfolio (e.g., a draft written for an application).
        Portfolio draft = new Portfolio();
        draft.addEntry("Projects", "Streamlit prototypes at Trigent Software");
        draft.addEntry("Volunteering", "Math session leader with OneInMath");
        me.merge(draft);
        me.renameSection("Goals", "Future Goals");
        System.out.println("---- After merge and rename ----");
        System.out.println("Sections: " + me.numberOfSections());
        System.out.println("Draft sections left: " + draft.numberOfSections());
        System.out.println("Has 'Goals'? " + me.hasSection("Goals"));
        System.out.println(
                "Has 'Future Goals'? " + me.hasSection("Future Goals"));
        System.out.println();
        System.out.println(me.toMarkdown());
    }

}
