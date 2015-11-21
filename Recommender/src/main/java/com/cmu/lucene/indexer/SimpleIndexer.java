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

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class SimpleIndexer {

	public static void main(String[] args) throws Exception {
		SimpleIndexer indexer = new SimpleIndexer();
		indexer.index();

	}

	public void index() throws ClassCastException, ClassNotFoundException, IOException {
		Path path = Paths.get(IndexerConstants.directoryPath);

		String serializedClassifier = "english.all.3class.caseless.distsim.crf.ser.gz";

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
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
			int names = 0;
			while (!done) {
				List<Movie> movies = dao.getMovies(limit, offset);

				if (!movies.isEmpty()) {

					for (Movie movie : movies) {
						StringBuffer sb = new StringBuffer();
						if (movie.getSynopsis() != null) {
							List<List<CoreLabel>> out = classifier.classify(movie.getSynopsis());

							for (List<CoreLabel> sentence : out) {
								for (CoreLabel word : sentence) {

									String string = word.get(CoreAnnotations.AnswerAnnotation.class);

									if (string.equals("PERSON")) {
										names++;
										// System.out.println(word);
									} else {
										sb.append(word + " ");
									}
								}
								// System.out.println();
							}
						}

						addDocument(indexWriter, movie.getId().toString(), sb.toString(), movie.getSynopsis(),
								movie.getGenre(),movie.getWriter(),movie.getDirector(),movie.getCast());
					}
					System.out.println("Names count : " + names);
					System.out.println("Indexing done for " + limit + " offset : " + offset);
				} else {
					System.out.println("Done");
					done = true;
				}
				offset += limit;

			}
			indexWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addDocument(IndexWriter index, String id, String title, String desc, String genres,String writer, String director,String stars) {

		if (desc == null)
			desc = "";

		if (title == null)
			title = "";

		if (genres == null)
			genres = "";
		
		if (stars == null)
			desc = "";

		if (writer == null)
			title = "";

		if (director == null)
			genres = "";

		Document document = new Document();

		FieldType type = new FieldType();

		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setStoreTermVectors(true);

		document.add(new StringField(IndexerConstants.MOVIE_ID, id, Field.Store.YES));

		document.add(new Field(IndexerConstants.DESC, desc, type));
		document.add(new Field(IndexerConstants.GENRES, genres, type));
		document.add(new Field(IndexerConstants.TITLE, title, type));
		document.add(new Field(IndexerConstants.WRITER, writer, type));
		document.add(new Field(IndexerConstants.DIRECTOR, director, type));
		document.add(new Field(IndexerConstants.STARS, stars, type));
		try {
			index.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
