# FilmQueryProject

#description
In this project, I created a program that was able to search a database. The program would allow the user to search a database of movies by either film ID, or by keywords found in the title or description of the movie. The user could enter any id or keyword, and be shown the details about matching movies. If the search returned no results, they would be asked to try again.

#technologies used
Java, SQL, Eclipse, Sublime

#lessons learned
SQL Queries is very good for creating objects. A simple way of turning SQL tables into objects that can be used inside of java, is to make each column represent a different variable of the object. For example, I created an object Films, and added fields for title, realase date, etc. Then when running the SQL query, I would create new film objects and assign the different columns from the query to the different fields in the object. This proved to be a very good way to use information from a database in Javas. 