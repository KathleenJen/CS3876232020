# Final Project for CS 623
Jesse Hill & Kathleen Jennings

* This is a Java project that interacts with a local postgres database
* There is a init condition when set to true will (1) Create a database titled "final_proj_table" (2) Creates tables for product, depot, and stock (3) Non-acid population of those three tables. Setting init to false will not execute these functions.
* The function acid_transaction takes a SQL command as a parameter and executes it in an atomic way
* Below is the ERD for the tables
![20200709_172356 (2)](https://user-images.githubusercontent.com/49496260/87095077-16702380-c20e-11ea-9629-36454b6e3018.jpg)
