# minikanren4j

An unofficial miniKanren implementation for java.

Please see http://minikanren.org/ for miniKanren details.

Note - This is a work in progress.

### todo, in order of importance

* Support sequences and logical variables embedded inside sequences.
* On failure return specific reason of failure in SubstMap.
* Implement most Minikanren operators 
    * soft cut operators such as condA and condU will not be implemented
* Implement constraint propagation

### API
The MinKan and Run classes are the main entry points. Please see TestRun class for sample usage.
