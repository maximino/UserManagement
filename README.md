#Neo4j and Play! 2.0 with Scala

This application aims to be an introduction to Neo4j and Play! 2.0 with Scala.

#Quick Start

##Setup
- [Download](http://download.playframework.org/releases/play-2.0.1.zip) and [install](http://www.playframework.org/documentation/2.0.1/Installing) Play 2.0.1.
- [Download](http://neo4j.org/download-thanks/?edition=community&release=1.7&platform=unix) and [install](http://docs.neo4j.org/chunked/stable/server-installation.html) Neo4j 1.7. (I prefer to not use Homebrew.)
- Start Neo4j with ``sh /$NEO4J_HOME/bin/neo4j start``

##Get going!

###Play! & Neo4j 101
- Run ``play`` from the project root. This launches the Play console. If you're familiar with sbt, you should see a number of similarities between the two. Despite the visual similarities, the Play console additionally has it's own set of commands which perform certain tasks in slightly different ways. Don't worry though, we'll focus on the core commands to get us up and running ASAP!
- Once sbt has done its thing, enter ``run`` in the resulting window.
- Head over to [http://localhost:7474/webadmin/](http://localhost:7474/webadmin/). There should be only one existing node. This is the root node
- In your browser once more, open up (http://localhost:9000/)[http://localhost:9000/]. If you briefly look at the play console, you'll see a number of GET and POST requests have been sent to the neo4j server.
- Looking back at the Neo4j web administration tool, you'll see that new nodes and relationships have been created by the application on its setup (courtesy of ``Global.scala``). Click on the 'Data browser' link in the Neo4j web administration tool and push the 'Show relationships' button. We are presented with a list of relationships this node has with other nodes. However, it is difficult to grasp a feel for the relationships of the entire application.
- Beside the '+ Relationship' button there's a button with an image of a 3 node graph. Pushing it switches us to view mode.
- GET requests to Neo4j server go to [http://localhost:7474/db/data/](http://localhost:7474/db/data/). Via a browser, we can also inspect nodes and relationships using this URL. This interface isn't as pleasant as the web administration tool, however it does allow you to search by relationships quite quickly. I tend to use a blend of the two.
- Lets head back over to the application homepage at [http://localhost:9000/](http://localhost:9000/).
- Open up ``/$PROJECT_ROOT/conf/routes``. This file determines the flow of our application. You should see a line like this: ``GET     /         controllers.Application.index``. This means a ``GET`` request from ``/`` will be handled by the method named ``index`` in class called ``Application`` in a package called ``controllers``.
- If you haven't already, open up the ``Application`` class in the ``controllers`` package. Inspect the ``index`` method closely and you see it returns a Play-ified HTML file, ``views.index.scala.html``. In this instance, ``views`` is the package name the file is ``index.scala.html``.
- Views can take arguments of any type. In our case, this view takes a ``String`` which will be rendered in the web page. Open ``index.scala.html``. The first line, ``@(message: String)``, is preceded by the ``@`` character. This character indicates the beginning of a Scala statement and because there'll be plenty of Scala statements in our ``scala.html`` files, you'll be seeing much more of it later on.

And that's pretty much it for a basic understanding of the basic flow of the application. From here, it's a question of adding small units of reasonable complexity; the process of creating a new node for example.

Before that, try to follow the code for a GET request to ``localhost:9000/admin/users/``. Refer back to the steps above if you need to.

#Slow Burn
You've grasped a basic understanding of the application. This section builds upon certain concepts we've used, but haven't directly discussed. At certain points I'll refer you to official documentation to help you truly appreciate what is going on.

##Play
- In the previous section, the GET request was handled by a [``play.api.mvc.Action``](http://www.playframework.org/documentation/api/2.0.1/scala/index.html#play.api.mvc.Action). As defined by the API: 'An action is essentially a ``(play.api.mvc.Request => play.api.mvc.Result)`` function that handles a request and generates a result to be sent to the client'. If you're using this guide, it's very likely you are familiar with HTTP and have come across Request and Response messages. It is precisely these concepts we are once more dealing with here, the main difference being that ``play.api.mvc.Result`` isn't exactly the same as a HTTP Response, however it does represent it. Check out the [Actions, Controllers and Results](http://www.playframework.org/documentation/2.0.1/ScalaActions) section of the official Play documentation for more detail.
- We have lightly covered how the ``routes`` file manages HTTP routing. Read the [HTTP routing](http://www.playframework.org/documentation/2.0.1/ScalaRouting) page of the official documentation to discover some advanced routing techniques. You'll also be introduced to a concept called 'reverse routing', which simply allows you to generate a URL from within a Scala call.
- If you remember, the ``@`` character was used in what appeared to be a Play-ified HTML file. The correct name for this file is a Scala Template. In addition to being able to pass Scala code to this file, we can do a number of Scala-like things inside the template itself, including but not limited to pattern matching, iterating and declaring reusable values. Read more about [the template engine](http://www.playframework.org/documentation/2.0.1/ScalaTemplates) and [common use cases](http://www.playframework.org/documentation/2.0.1/ScalaTemplateUseCases) in the official documentation.
- It goes without saying that forms are useful for capturing data from an end user. You are strongly advised to read the [handling form submission](http://www.playframework.org/documentation/2.0.1/ScalaForms) section of the official documentation.

- For examples look in the official Play! [samples](https://github.com/playframework/Play20/tree/master/samples).

##Neo4J
It's worth noting that [Neo4j](http://docs.neo4j.org/chunked/1.7/index.html) runs in two modes: embedded and server. In a production environment, the version you decide to use really depends on your own use-case. The advantage to the standalone server version is that it can be used from any language and multiple clients that can 'talk' REST. Another advantage is the web administration tool for easy visualisation. The advantage embedded Neo4j has is that performance is better.

Examples for how to create indexes, traverse the database and work with domain objects can be found in the Neo4j [tutorials](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) and the associated [code samples](https://github.com/neo4j/community/tree/1.7/embedded-examples/src/main/java/org/neo4j/examples). These examples are Java-centric and focus on the embedded version of Neo4j. A brief tutorial for interacting with the Neo4j server via the REST API using Jersey can be found [here](http://docs.neo4j.org/chunked/1.7/server-java-rest-client-example.html), and the associated code samples [here](https://github.com/neo4j/community/tree/master/server-examples/src/main/java/org/neo4j/examples/server).

Lets take a look at the [REST API](http://docs.neo4j.org/chunked/1.7/rest-api.html) in conjunction with how we send requests to the Neo4j server in our ``Neo4jRestService`` trait.

We're using the [Dispatch library](http://dispatch.databinder.net/Dispatch.html) for HTTP interaction.

>TODO: Need to go into more detail 

##Altogether Now!
We're going to step through the process of adding a new user.

- From the our web application's admin page, click on the ``Users`` link in the side pane. This takes you to the users index page, providing a link to create a new user and listing any existing users.
- The ``Create new user`` link points to ``/admin/users/newUser``. Checking our routes file, a ``GET`` request with this request path is handled by ``controllers.Users.create``. The ``controllers.Users.create`` method returns a ``play.api.mvc.Action``. This ``Action`` requires a Scala template which takes a ``play.api.data.Form`` object.
- Inspecting the ``Form`` we pass, it expects a name, an email and a password. The email address and passwords are verified in different ways: emails must be unique and passwords must match and be greater that 6 characters long. We bind these fields to a new ``User`` instance, setting the id to null, yet casting it to ``Long``.

- If you read the [handling form submission](http://www.playframework.org/documentation/2.0.1/ScalaForms) you would have come across a section entitled 'Constructing complex objects'. Here you would have been introduced to how binding can be done with the ``tuple`` and ``mapping`` methods of ``play.api.data.Forms``. Quickly check it out now if you didn't then. As required for the ``play.api.data.Forms.mapping`` method, we need to provided a way to construct and deconstruct an object from and to the parameters we specified.
- Open the ``views.users.create.scala.html`` file. The first line indicates that a ``Form[User]`` is expected. In the form body, we can see how all the fields we declared earlier are being used. The action of the submit button is defined at the head of the helper form, using an import defined at the top of the file and pointing to ``routes.Users.submit``. Reverse routing is at play here; remember it is simply a way to generate a URL from within a Scala call.

- In the ``controllers.Users.submit`` method, we bind the data from the form, returning the form (with any data already inputted) if there are errors, creating and saving a new ``User`` if there are not. ``detailResult`` is simply a refactored method that takes a ``User`` object and presents a detailed html view of the ``User``.

- Let's inspect the ``User.save`` method a little closer. It calls the ``save``` method of its parent abstract class, ``Model``, passing along the current ``User`` instance, the reference node for ``User``'s and the relationship these two entities have as arguments. ``Model.save`` passes these same arguments to ``utils.neo4j.Neo4jRestService.saveNodeAndCreateRelationship`` via a barebones graph object which mixes in the ``Neo4jRestService`` trait.

- In the first block of the ``saveNodeAndCreateRelationship`` method, by sending [this](http://docs.neo4j.org/chunked/stable/rest-api-nodes.html) request to the Neo4j server. We then process the response, pulling out the node id Neo4j gave this node. Look [here](http://www.flotsam.nl/dispatch-periodic-table.html) for a snapshot of Dispatch's operators, [here](http://dispatch.databinder.net/Dispatch.html) for the Dispatch documentation and [here](http://databinder.net/dispatch-doc/#package) for the  Dispatch API.

- If you recall, back when we did the binding, we set the id to ``null``. In the next block, we send another request to the server, this time setting the id property of the persisted entity to the same value as it's node id.

- Finally, we get a reference to the model object, create the relationship between the object and it's reference node and return the object.

That's it! Play, Neo4j and Dispatch don't get any more complex than that!
Step through the rest of the code and ensure you understand what's going on.
