package com.cmu.model;

public class Movie {

	
	private String title;
	
	private Long id;
	
	private String genre;
	
	private String synopsis;

	public Movie(String title, Long id, String genre) {
		super();
		this.title = title;
		this.id = id;
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	
	
}
