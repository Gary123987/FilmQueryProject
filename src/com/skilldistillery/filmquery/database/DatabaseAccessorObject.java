package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String user = "student";
	private static final String password = "student";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Connection con = DriverManager.getConnection(URL, user, password);
		Film film = null;
		// ...
		String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE id = ?;";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			int id = rs.getInt(1);
			String title = rs.getString(2);
			String desc = rs.getString(3);
			int releaseYear = rs.getShort(4);
			int langId = rs.getInt(5);
			int rentDur = rs.getInt(6);
			double rate = rs.getDouble(7);
			int length = rs.getInt(8);
			double repCost = rs.getDouble(9);
			String rating = rs.getString(10);
			String features = rs.getString(11);

			film = new Film(id, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating, features);
			// ...
			con.close();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Connection con = DriverManager.getConnection(URL, user, password);
		Actor actor = null;
		// ...
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor(); // Create the object
			// Here is our mapping of query columns to our object fields:
			actor.setId(actorResult.getInt(1));
			actor.setFirstName(actorResult.getString(2));
			actor.setLastName(actorResult.getString(3));

		}
		// ...
		con.close();
		return actor;
	}

	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, password);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
			sql += " rental_rate, length, replacement_cost, rating, special_features "
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmId = rs.getInt(1);
				String title = rs.getString(2);
				String desc = rs.getString(3);
				short releaseYear = rs.getShort(4);
				int langId = rs.getInt(5);
				int rentDur = rs.getInt(6);
				double rate = rs.getDouble(7);
				int length = rs.getInt(8);
				double repCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String features = rs.getString(11);
				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	public List<Film> searchByKeyword(String input) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, password);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features  "
					+ "FROM film WHERE title like ? or description like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			input = "%" + input + "%";
			stmt.setString(1, input);
			stmt.setString(2, input);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmId = rs.getInt(1);
				String title = rs.getString(2);
				String desc = rs.getString(3);
				short releaseYear = rs.getShort(4);
				int langId = rs.getInt(5);
				int rentDur = rs.getInt(6);
				double rate = rs.getDouble(7);
				int length = rs.getInt(8);
				double repCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String features = rs.getString(11);
				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Actor> findActorsByFilmId(Film film) {
		List<Actor> actors = new ArrayList<>();
		int id = film.getId();
		try {
			Connection conn = DriverManager.getConnection(URL, user, password);
			String sql = "select first_name, last_name, actor.id from actor "
					+ "join film_actor on actor.id = film_actor.actor_id "
					+ "join film on film.id = film_actor.actor_id "
					+ "where film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String fName = rs.getString(1);
				String lName = rs.getString(2);
				int actorid = rs.getInt(3);
				
				
				Actor actor = new Actor(actorid, fName, lName);
				actors.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return actors;
	}
			
	

	public String getFilmLang(Film film) {
		String title = film.getTitle();
		String lang = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, password);
			String sql = "select language.name " + "from language " + "join film on language.id = film.language_id "
					+ "where film.title = ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
			lang = rs.getString(1);
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lang;
	}

}
