# Modulith Navigator

![Build](https://github.com/micahhauge/modulith-navigator/workflows/Build/badge.svg)

IntelliJ Project view plugin for Spring Modulith that sorts modules by type, open first and internal second, so your architecture is visible without opening a single file.

A module is considered **internal** if its only subdirectory is named `internal`. All other modules are treated as **open** and sorted to the top.

## Installation

### From the JetBrains Marketplace _(coming soon)_

<kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > search for **Modulith Navigator** > <kbd>Install</kbd>

### Manually from a local build

1. Clone the repo and build the plugin:
   ```bash
   git clone https://github.com/micahhauge/modulith-navigator.git
   cd modulith-navigator
   ./gradlew buildPlugin
   ```
2. In IntelliJ: <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install Plugin from Disk...</kbd>
3. Select `build/distributions/modulith-navigator-0.0.1.zip`
4. Restart IntelliJ

---

Plugin based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template).
