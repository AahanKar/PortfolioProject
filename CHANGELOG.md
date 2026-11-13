# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## 2026.11.12

### Added

- Designed kernel implementation `Portfolio1L` for Portfolio component, represented as a `Map` of section names to `Queue`s of entries
- Added convention and correspondence for `Portfolio1L`
- Designed abstract class `PortfolioSecondary` for Portfolio component
- Implemented all enhanced methods using only kernel and `Standard` methods
- Implemented `toString`, `equals`, and `hashCode` using only kernel methods
- Designed kernel and enhanced interfaces for Portfolio component
- Designed proof of concept for Portfolio component

### Changed

- Decided section names are ordered with `String.compareTo` (uppercase before lowercase)
- No kernel changes were needed for `PortfolioSecondary`, since `hasSection` and `numberOfSections` can check every kernel precondition
- Changed usage of `java.util` types to using OSU `Queue` and `Map.Pair`
- Added `sectionNames` to enhanced interface as kernel can't iterate through sections
- Made `numberOfEntries` require section to exist rather than return 0
- Specified that `toMarkdown` outputs sections in alphabetical order
- Made `addSection` clear the contents of the `entries` argument (as it is consumed)
- Added suggested section name constants to kernel interface`
