# Changelog

All major changes to this project will be recorded here.

This changelog follows the format outlined in [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

The project uses [Calendar Versioning](https://calver.org/) in the format YYYY.0M.0D.

## 2026.11.12

### Added

- Designed the abstract class `PortfolioSecondary` for the Portfolio component.
- Every improved method was implemented using just kernel and `Standard` methods.
- `toString`, `equals` and `hashCode` were all implemented using just kernel methods.

### Changed

- Section names were decided to be ordered using `String.compareTo` (uppercase comes before lowercase).
- No changes were required to the kernel as `hasSection` and `numberOfSections` can be used to check all preconditions in the kernel.

## 2026.11.12

### Added

- Designed kernel and improved interfaces for the Portfolio component.
- Designed a proof of concept for the Portfolio component.

### Changed

- Changed to use the OSU `Queue` and `Map.Pair` instead of `java.util` types.
- Added a `sectionNames` method to the improved interface as the kernel doesn't allow you to iterate through sections.
- Changed `numberOfEntries` to require the section to exist.
- Specified that `toMarkdown` will output sections in alphabetical order.
- Made `addSection`
