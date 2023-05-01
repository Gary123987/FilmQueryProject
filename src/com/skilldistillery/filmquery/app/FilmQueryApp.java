package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	private DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.launch();
	}



	private void launch() {
		boolean keepGoing = true;
		while (keepGoing) {
			Scanner sc = new Scanner(System.in);
			String choice = null;
			String input = null;

			System.out.println("What would you like to do? \n" + "1. Find a film by its ID \n"
					+ "2. Look up a film by a search keyword \n" + "3. Quit");
			choice = sc.next();

			if (choice.equals("1")) {
				System.out.println("Please enter the films id");
				input = sc.next();
				findFilmById(input);
			}
			else if (choice.equals("2")) {
				System.out.println("Please enter the keyword you want to search");
				input = sc.next();
				findFilmByKeyword(input);
			}
			else if (choice.equals("3")) {
				keepGoing = false;
			}
			else {
				System.out.println("invalid input, please try again");
			}
		}
	}

	private void findFilmByKeyword(String input) {
		List<Film> films = db.searchByKeyword(input);
		
		
		if (films.isEmpty()) {
			System.out.println("No results found");
		}
		for (Film film : films) {
			String title = film.getTitle();
			int year = film.getRealeaseYear();
			String rating = film.getRating();
			String description = film.getDescription();
			String lang = db.getFilmLang(film);
			List<Actor> actors = db.findActorsByFilmId(film);
			
			if (title != null) {
				System.out.println(title + " released in " + year + " with a rating of " + rating +" the language is " + lang + "\nDescription: "+ description 
						+ "\nThe actors are:");
				for (Actor a : actors) {
					System.out.println(a.getFirstName() + " " + a.getLastName());
				}
			}
			else {
				System.out.println("no results found");
			}
		}
		
		

	}

	private void findFilmById(String input) {
		try {
			int id = Integer.parseInt(input);
			Film film = db.findFilmById(id);
			String title = film.getTitle();
			int year = film.getRealeaseYear();
			String rating = film.getRating();
			String description = film.getDescription();
			String lang = db.getFilmLang(film);
			List<Actor> actors = db.findActorsByFilmId(film);
			
			if (title != null) {
				System.out.println(title + " released in " + year + " with a rating of " + rating +" the language is " + lang + "\nDescription: "+ description 
						+ "\nThe actors are:");
				for (Actor a : actors) {
					System.out.println(a.getFirstName() + " " + a.getLastName());
				}
			}
			else {
				System.out.println("Invalid id");
			}
			
			
		}
		catch (Exception e) {
			System.out.println("Invalid input, please try again");
		}

	}

}
