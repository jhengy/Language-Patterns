# Reference

- https://plantuml.com/

# Dev

- use IDE plugin

# Build

- https://plantuml.com/starting
  - download jar
- https://plantuml.com/command-line

```sh
java -jar plantuml.jar sequenceDiagram.txt

# specify output format
cd src
java -jar ../build/plantuml.jar . -tsgv -x "**/common/**" -o ../build
```
