This application aims to be an introduction to Neo4j and Play! 2.0 with Scala.

## Quick Start

### Setup
- [Download](http://download.playframework.org/releases/play-2.0.1.zip) and [install](http://www.playframework.org/documentation/2.0.1/Installing) Play 2.0.1.
- [Download](http://neo4j.org/download-thanks/?edition=community&release=1.7&platform=unix) and [install](http://docs.neo4j.org/chunked/stable/server-installation.html) Neo4j 1.7. (I prefer to not use Hommebrew.)
- Start Neo4j with `sh /<Neo4j root>/bin/neo4j start`

### Get going!

#### Neo4j 101
- Run `play` from the UserManagement root
- Once sbt has done it's thing, enter `run` in the resulting window
- Head over to [http://localhost:7474/webadmin/](http://localhost:7474/webadmin/) and take note of how many nodes exist.
- In your browser once more, open up (http://localhost:9000/)[http://localhost:9000/]. If you briefly look at the play console, you'll see a number of GET and POST requests have been sent to the neo4j server.
- Looking back at the Neo4j web console, you'll see that new nodes and relationships have been created by the application on it's first request. Click on the 'Data broswer' link in the neo4j web console and push the 'Show relationships' button. Information appears, but not the most visually appealing.. Beside the '+ Relationship button' there's a button that switches to View Mode. Pretty pictures!! Mess around with this interface but avoid creating new nodes or relationships. Neo4j provides another another web at [http://localhost:7474/db/data/](http://localhost:7474/db/data/).

#### Play 101
- Back over at [http://localhost:9000/](http://localhost:9000/), it's clear we're at the root of the application. Lets see how this works.
- Open up /UserManagement/conf/routes. This file determines the flow of our application. You should see a line like this: `GET     /         controllers.Application.index`. This means a GET request from / will be handled by the code in controllers.Application.index.
- You've probably leapt ahead and already looked at the Application class in the controllers package. The index method produces a view called index, which takes in a String. Note that the entire views package has been imported at the top of the class.
- From here, you should be able to follow the code, stepping from browser, to routes file, to code and back to browser.

## Slow Burn
You've grasped a basic understanding of the application.. you'll probably want to look at these topics to broaden you knowledge.

#### Play
- [Actions, Controllers and Results](http://www.playframework.org/documentation/2.0.1/ScalaActions)
- [HTTP routing](http://www.playframework.org/documentation/2.0.1/ScalaRouting)
- [The template engine](http://www.playframework.org/documentation/2.0.1/ScalaTemplates)
- [Scala templates common use cases](http://www.playframework.org/documentation/2.0.1/ScalaTemplateUseCases)
- [Handling form submission](http://www.playframework.org/documentation/2.0.1/ScalaForms)

- For examples look in the offical Play! [samples](https://github.com/playframework/Play20/tree/master/samples).

#### Neo4J
It's worth noting that [Neo4j](http://docs.neo4j.org/chunked/1.7/index.html) runs in two modes: Embedded and Server.
The embedded version tightly couples Neo4j code with your Java/Scala classes.. Not exactly extensible or scalable, it should go without saying that we should primarily use this version for small test projects or learning purposes.

If at some point, for some reason, you need to use the embedded version, these [tutorials](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) and the associated [code examples](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) will come on handy.

Lets take a look at the [REST API](http://docs.neo4j.org/chunked/1.7/rest-api.html) in conjunction with how we send requests to the Neo4j server in our Neo4jRestService.scala trait.

We're using the [Dispatch library](http://dispatch.databinder.net/Dispatch.html) for HTTP interaction. Compare the val's at the top of the trait with any of the request/response bodies in the REST API.