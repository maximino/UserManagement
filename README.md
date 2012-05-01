This application aims to be an introduction to Neo4j and Play! 2.0 with Scala.

## Core Technologies


### Play! 2.0
Being core to everything else, we'll start with [Play](http://www.playframework.org/documentation/2.0/Home).

Go through the topics under the 'Getting started' section.
Next up, find Play!'s [Scala homepage](http://www.playframework.org/documentation/2.0/ScalaHome) and step through the Scala tutorial [provided](http://www.playframework.org/documentation/2.0/ScalaTodoList).

Head back to Play!'s Scala homepage and go through the following concepts (I'd suggest following and absorbing how things are done with the framework rather than implementing the code):

- [Actions, Controllers and Results](http://www.playframework.org/documentation/2.0/ScalaActions)
- [HTTP routing](http://www.playframework.org/documentation/2.0/ScalaRouting)
- [The template engine](http://www.playframework.org/documentation/2.0/ScalaTemplates)
- [Scala templates common use cases](http://www.playframework.org/documentation/2.0/ScalaTemplateUseCases)
- [Handling form submission](http://www.playframework.org/documentation/2.0/ScalaForms)

Hopefully this should be enough to give you a feel for the framework.

__N.B.__
SBT is intentionally overlooked. The Play! documentation provides just the right amount of information on the tool to get you started. If you need to become more familiar, have a look [here](http://www.scala-sbt.org/learn.html).

### Neo4j 1.7
It's worth noting that [Neo4j](http://docs.neo4j.org/chunked/1.7/index.html) runs in two modes: Embedded and Server.
The embedded version tightly couples Neo4j code with you Java/Scala classes.. Not exactly extensible or scalable, it should go without saying that we should primarily use this version for small test projects or learning purposes.

And that we shall. Use these [tutorials](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) and the associated [code examples](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) to get a feel for Neo4j. However, don't get to comfy with the Neo4j APIs, as we won't be using them much longer.

Now that Neo4j isn't as obscure as it once was, we'll continue as we intend to go on. If you haven't yet [download](http://neo4j.org/download-thanks/?edition=community&release=1.7&platform=unix)ed Neo4j, do that now. Follow the instructions for setting up [Neo4j Server](http://docs.neo4j.org/chunked/1.7/server.html), then head over to the [REST API](http://docs.neo4j.org/chunked/1.7/rest-api.html) documentation.

At this stage, simply familiarise yourself with the structure requests and responses sent to and received from the Neo4j Server. We'll go into more depth when we step through the application.

Once you have it running, open tabs for Neo4j's [web admin console](http://localhost:7474/webadmin) and it's [db viewer](http://localhost:7474/db/data/); we'll be needing them soon.

## The App

We'll now step through parts of the application to help solidify your understanding of thses and other technologies.

From UserManagement root, in you Terminal type `play`. Once the fancy ASCII art has loaded, type `~run` (__Protip!__ '~' auto compiles whenever your source changes!!).
Once the server has started, head over to [http://localhost:9000/](http://localhost:9000/).

If you get a `HttpHostConnectException: Connection to http://localhost:7474 refused`, you simply haven't started the Neo4j Server. `Ctrl-D` to stop running the Play! application from the terminal, `sh neo4j start` from the Neo4j installation to start the graph database up. Start your Play! app again.

TODO..
