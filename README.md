This application aims to be an introduction to Neo4j and Play! 2.0 with Scala.

## Quick Start

### Setup
- [Download](http://download.playframework.org/releases/play-2.0.1.zip) and [install](http://www.playframework.org/documentation/2.0.1/Installing) Play 2.0.1.
- [Download](http://neo4j.org/download-thanks/?edition=community&release=1.7&platform=unix) and [install](http://docs.neo4j.org/chunked/stable/server-installation.html) Neo4j 1.7. (I prefer to not use Hommebrew.)
- Start Neo4j with `sh /<Neo4j root>/bin/neo4j start`

### Get going!

#### Neo4j 101
- Run `play` from the UserManagement root.
- Once sbt has done it's thing, enter `run` in the resulting window.
- Head over to [http://localhost:7474/webadmin/](http://localhost:7474/webadmin/) and take note of how many nodes exist.
- In your browser once more, open up (http://localhost:9000/)[http://localhost:9000/]. If you briefly look at the play console, you'll see a number of GET and POST requests have been sent to the neo4j server.
- Looking back at the Neo4j web console, you'll see that new nodes and relationships have been created by the application on it's setup (courtesy of Global.scala). Click on the 'Data broswer' link in the neo4j web console and push the 'Show relationships' button. Information appears, but not the most visually appealing.. Beside the '+ Relationship button' there's a button that switches to View Mode. Pretty pictures!! Mess around with this interface but avoid creating new nodes or relationships. Neo4j provides another another web interface at [http://localhost:7474/db/data/](http://localhost:7474/db/data/) for viewing the data in a slightly different way.

#### Play 101
- Back over at [http://localhost:9000/](http://localhost:9000/), it's clear we're at the root of the application. Lets see how this works.
- Open up /UserManagement/conf/routes. This file determines the flow of our application. You should see a line like this: `GET     /         controllers.Application.index`. This means a GET request from / will be handled by the method named controllers.Application.index.
- You've probably leapt ahead and already looked at the Application class in the controllers package. The index method produces a view called index, which takes in a String. Note that the entire views package has been imported at the top of the class.
- From here, you should be able to follow the code, stepping from browser, to routes file, to code and back to browser.

## Slow Burn
You've grasped a basic understanding of the application.. you'll probably want to look at these topics to broaden you knowledge.

### Play
The official Play! documentation is quite extensive; as rewording it here would be something of a time sink, I'll instead refer you to the necessary sections.

- [Actions, Controllers and Results](http://www.playframework.org/documentation/2.0.1/ScalaActions)
- [HTTP routing](http://www.playframework.org/documentation/2.0.1/ScalaRouting): 
- [The template engine](http://www.playframework.org/documentation/2.0.1/ScalaTemplates)
- [Scala templates common use cases](http://www.playframework.org/documentation/2.0.1/ScalaTemplateUseCases)
- [Handling form submission](http://www.playframework.org/documentation/2.0.1/ScalaForms)

- For examples look in the offical Play! [samples](https://github.com/playframework/Play20/tree/master/samples).

### Neo4J
It's worth noting that [Neo4j](http://docs.neo4j.org/chunked/1.7/index.html) runs in two modes: Embedded and Server.
The embedded version tightly couples Neo4j code with your Java/Scala classes.. Not exactly extensible or scalable, it should go without saying that we should primarily use this version for small test projects or learning purposes.

If at some point, for some reason, you need to use the embedded version, these [tutorials](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) and the associated [code examples](http://docs.neo4j.org/chunked/1.7/tutorials-java-embedded.html) will come on handy.

Lets take a look at the [REST API](http://docs.neo4j.org/chunked/1.7/rest-api.html) in conjunction with how we send requests to the Neo4j server in our Neo4jRestService.scala trait.

We're using the [Dispatch library](http://dispatch.databinder.net/Dispatch.html) for HTTP interaction.

###Altogether Now!
We're going to step through the process of adding a new user.

From the web apps admin page, click on the Users link in the side pane. This takes you to the Users index page, providing a link to create a new user and listing any existing users.

The `Create new user` link points to `/admin/users/newUser`. Checking our routes file, a GET request with this request path is handled by controllers.Users.create. The controllers.Users.create method returns a Result consisting of the views.html.users.create view, which has been passed a `userByNameForm`.

Inspecting the Form, it expects a name, email and password. The email address and passwords are verified in different ways; emails must be unique and passwords must match and be greater that 6 characters long. We bind these fields to a new User instance, setting the id as an instance of null. As required for `mapping` we also provide a way to break the object into it constituent parts. Read section 26.2 in Programming in Scala to apprciate extrators a little more. As a potential extension to the application, see if you can improve the time taken for email verification. One potential way would be to use Websockets (or AJAX) to begin verification of email addresses as early as possible..

Open the `views.users.create.scala.html` file. The first line indicates that a Form[User] is expected. In the form body, we can see how all the fields we declared earlier are being used. The form's submit and cancel buttons are declared in the standard way. The action of the submit button is defined at the head of the helper form, using an import defined at the top of the file and pointing to `routes.Users.submit` (Reverse routing is at play here).

In the controllers.Users.submit method, we bind the data from the, returning the form if there are errors, creating and saving a new user if there are not. `detailResult` is simply a method that takes a User, and generates presents the user of the application with a view detailing that User's details.

Lets inspect the User.save method a little closer. It calls the `save` method of its parent abstract class, Model, passing along the current User instance, the User reference node and the relationship these two entities have as arguments. Model.save passes these same arguements to `utils.neo4j.Neo4jRestService.saveNodeAndCreateRelationship`via a barebones graph object which mixes in the Neo4jRestService trait.

In the first block of this `saveNodeAndCreateRelationship` method, by sending [this](http://docs.neo4j.org/chunked/stable/rest-api-nodes.html) request to the Neo4j server. We then process the response, pulling out the node id Neo4j gave this node. Look [here](http://www.flotsam.nl/dispatch-periodic-table.html), [here](http://dispatch.databinder.net/Dispatch.html) and [here](http://databinder.net/dispatch-doc/#package) for more info on how Dispatch works.

In the next block, we send another request to the server, this time updating the id property of the actual object.

Finally, we get a reference to the model object, create the relationship between the object and it's reference node and return the object.

That's it! Play, Neo4j and Dispatch don't get any more complex than that!

Step through the rest of the code and ensure you understand what's going on.