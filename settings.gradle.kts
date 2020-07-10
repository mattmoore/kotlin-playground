rootProject.name = "kotlin-playground"
include(
  "plugins:annotation-processor:compiler-plugin",
  "plugins:annotation-processor:use-plugin"
)
include(
  "plugins:ir-transform:compiler-plugin",
  "plugins:ir-transform:use-plugin"
)
