# Changelog

All major changes to this project will be documented in this file.

This file follows the [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) standard and this project uses date-based versioning (CalVer). The format will be YYYY.MM.DD.

## 2026.11.13

### Added

JUnit 4 test class called `Portfolio1LTest` to test the kernel and `Standard` methods: `addSection`, `removeSection`, `removeAnySection`, `hasSection`, `numberOfSections`, `clear` and `newInstance` and `transferFrom`.

JUnit 4 test class called `PortfolioTest` to test the secondary methods: `addEntry`, `numberOfEntries`, `totalEntries`, `sectionNames`, `renameSection`, `merge`, `toMarkdown` and also `toString`, `equals` and `hashCode`.

2 use case classes in the `src` folder to demonstrate different ways to use the component.

Reflection complete in `06-component-finishing-touches.md`.

### Changed

Added Javadoc above `PortfolioKernel` and `Portfolio` interfaces.

Improved README and folder structure for publishing.

## 2026.11.12

### Added

Brainstorming document for component. 3 potential designs were thought of. The chosen design was Portfolio (sections of entries). (Part 1)

Proof of concept for component. `Portfolio` class with `main` method to store sections of entries. (Part 2)

Interface `PortfolioKernel` that extends from `Standard`. It contains the methods `addSection`, `removeSection`, `removeAnySection`, `hasSection`, `numberOfSections`. It also contains the constants `ABOUT_ME`, `INTERESTS`, `PROJECTS`, and `GOALS`. (Part 3)

Enhanced interface `Portfolio` containing the methods `addEntry`, `numberOfEntries`, `totalEntries`, `sectionNames`, `renameSection`, `merge`, and `toMarkdown`. (Part 3)

Class diagram created using Mermaid. (Part 3)

Abstract class `PortfolioSecondary` to implement the secondary methods using just methods from the kernel. Also has `toString`, `equals` and `hashCode`. (Part 4)

Implementation of kernel using Map. `Portfolio1L` contains a `Map>` and has a documented convention and mapping. (Part 5)

### Changed

Renamed the proof of concept class.

Renamed the file from `PortfolioInterface` to `Portfolio.java`. This allows `extends Standard` to work.
