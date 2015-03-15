# JSnip
JSnip is a desktop Java application that makes it easy to store, manage and execute snippets of Java code. The snippets can be stored in a hierarchy, saved to a file and loaded from the file. Nodes can be added to the tree and removed from the tree at will. JSnip uses the [Beanshell](http://www.beanshell.org/) library to execute the snippets.

![JSnip](http://argonium.github.io/jsnip1.png)

The repository includes a data file with code from the [Java Almanac](http://exampledepot.8waytrips.com/). It's an excellent reference for doing a lot of common activities in Java but are not always easily remembered, such as reading data from a file.

![JSnip](http://argonium.github.io/jsnip2.png)

The application requires Java 5 or later to build and execute.

There is currently no help file, but there is tooltip text for most of the controls, so the interface should be easy to understand.

To run the appication, build it via Ant ('ant clean dist'), and then open via 'java -jar jsnip.jar' (or double-click jsnip.jar).

Part of the code is copyright JGoodies Karsten Lentzsch. This is limited to portions of the GUI. The toolbar buttons came from the Java Look and Feel Graphics Repository.

The source code is released under the MIT license (other than the JGoodies code).
