ToyLib - Thinking about refactorings we may apply to VyLib.


Images and volumes need to hadle:

Different internal types (float, int, etc).
Different dimensions (2D vs 3D vs 4D).
Algorithms that care about dimensionality vs algorithms that dont.
Layout and ownership of data.


Dimensionality: Be explicit everywhere, passed as template argument where
appropriate. Reason: we rarely write something which is completely 2D vs 3D
agnostic. Algorithms which are genuinely agnostic can be written as templates
(and instantiated explicity in cpp if they are large).

Read-only vs writable: Hopefully const will just take care of it.

Pixel types: Interface class with accessors to extract / insert blocks of a vew
basic types. Use template concrete classes to implement the interface.


ToDo:

Interface class for N-D images, with sensible accessors.

