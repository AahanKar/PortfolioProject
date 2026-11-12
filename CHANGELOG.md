# Changelog

All significant changes to this project will be recorded here.

This project uses the format described in [Keep a Changelog](https://keepachangelog.com/en/1.1.0/). This project follows Calendar Versioning ([https://calver.org/](https://calver.org/)) with the format of YYYY.0M.0D.

## 2026.11.12

### Added

- Designed kernel and improved interfaces for Portfolio component.
- Designed proof of concept for Portfolio component.

### Changed

- Changed usage of `java.util` types to using OSU `Queue` and `Map.Pair`.
- Added `sectionNames` to improved interface as kernel can't iterate through sections.
- Made `numberOfEntries` require section to exist rather than return 0.
- Specified that `toMarkdown` outputs sections in alphabetical order.
- Made `addSection` clear the contents of the `entries` argument (as it is consumed).
- Added suggested section name constants to kernel interface.
