# Clojure Web Application
This application is designed to work as an web image gallery, with
different levels of access, such as guest and registered user. Guest is
only allowed to view galleries and images that have been posted by other
registered users.

On the other hand, registered user has more possibilities. He can post
and delete his own images, erase his account (by doing this - all the
galleries and images created by that user are also being erased), as
well as view images from other users (without being able to delete
them).

Once the registered user posts his first image, gallery where all his
future images are stored is automatically being created.

Images are being stored in project directory, in order for database to
be lighter.
Image related metadata is located in table "images", and based on
that, we can exactly connect image from directory with currently
logged in user. 

## Prerequisites

You will need [Leiningen][1] 1.6.0 or above installed.

You will need [PostgreSQL][2]

	- database name: master_rad
	- user: admin
	- pasword: admin

For developing this application was used [LightTable][3] development environment (not necessary).
	
[1]: https://github.com/technomancy/leiningen
[2]: http://www.postgresql.org/
[3]: http://www.lighttable.com/

## Running

Start a web application:
	in repl:
		
		1.(use 'master.models.schema)
		  (create-users-table)
		  (create-images-table)
		
		2. (use 'master.repl)
		   (start-server)

#Library used:
##Hiccup
Hiccup is a library for representing HTML in Clojure. It uses vectors to represent elements, and maps to represent an element's attributes.

##Domina
Domina is a jQuery inspired DOM manipulation library for ClojureScript. It provides a functional, idiomatic Clojure interface to the DOM manipulation facilities provided by the Google Closure library.

##Compojure
Compojure is a small routing library for Ring that allows web applications to be composed of small, independent parts.

#Literature

[Practical Clojure](http://www.amazon.com/Practical-Clojure-Experts-Voice-Source-ebook/dp/B003VM7G3S)

  This is the first book I have read about the Clojure. It is a good introductory book, and can give you good insight about functional programming, as well as basic Clojure functions and some features. Here you can learn how to set up Clojure environment for some basic real-world task. 


[Clojure Programming](http://www.amazon.com/Clojure-Programming-Chas-Emerick/dp/1449394701/ref=pd_sim_b_1?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

  Clojure programming is definetely the most comprehensive book about the topic. As Practical Clojure and Programming Clojure it contains explanations about functional languages in general, as well as explanations of Clojure functions and other features, but it goes much deeper into the core of the languge and explains all pros of working with CLojure.

[Web Development With Clojure](http://www.amazon.com/Web-Development-Clojure-Build-Bulletproof/dp/1937785645/ref=pd_sim_b_3?ie=UTF8&refRID=0KCSHHVCSA3Z3YCX6JAF)

  As its title says, this book is about developing a web applications using Clojure. It is written in a good, easy to understand manner, and for me it has excellent informations about vastly available libraries, tools and good practices out there, that can be used in bulding your application. This is definetely a must-read book about web development in CLojure, and I have used it a lot during bulding my application. Also, this book is published in 2014, so is definetely up to date, which is very important for a programming book. 

##License

Distributed under the Eclipse Public License, the same as Clojure.


