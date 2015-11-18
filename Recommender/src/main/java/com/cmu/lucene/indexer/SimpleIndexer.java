package com.cmu.lucene.indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import com.cmu.dao.RecommendationDaoImpl;
import com.cmu.model.Movie;

public class SimpleIndexer {

	public static void main(String[] args) {
		SimpleIndexer indexer = new SimpleIndexer();
		indexer.index();

	}

	public void index() {
		Path path = Paths.get(IndexerConstants.directoryPath);

		Directory directory;

		RecommendationDaoImpl dao = new RecommendationDaoImpl();
		try {
			directory = new SimpleFSDirectory(path);

			StandardAnalyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, config);

			int limit = 1000;
			int offset = 0;
			boolean done = false;
			while (!done) {
				List<Movie> movies = dao.getMovies(limit, offset);

				if (!movies.isEmpty()) {

					for (Movie movie : movies) {
						addDocument(indexWriter, movie.getId().toString(), movie.getTitle(), movie.getSynopsis(),
								movie.getGenre());
					}
					
					System.out.println("Indexing done for " + limit + " offset : " +offset);
				}
				else
				{
					System.out.println("Done");
					done=true;
				}
				offset+=limit;
				
			}
			indexWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addDocument(IndexWriter index, String id, String title, String desc, String genres) {
		
		if(desc ==null)
			desc= "N/A";
		
		if(title ==null)
			title= "N/A";
		
		if(genres ==null)
			genres= "N/A";
		
		
		Document document = new Document();

		FieldType type = new FieldType();

		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setStoreTermVectors(true);
		
		document.add(new StringField("movieid", id, Field.Store.YES));

		document.add(new Field("desc", desc, type));
		document.add(new Field("genres", genres, type));
		document.add(new Field("title", title, type));
		try {
			index.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
