package com.cmu.lucene.indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

public class SimpleIndexer {

	public static void main(String[] args) {
		SimpleIndexer indexer = new SimpleIndexer();
		indexer.index();

	}

	public void index() {
		Path path = Paths.get(IndexerConstants.directoryPath);
		Directory directory;
		try {
			directory = new SimpleFSDirectory(path);

			StandardAnalyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter indexWriter = new IndexWriter(directory, config);

			addDocument(indexWriter, "1", IndexerConstants.movie1_title, IndexerConstants.movie1_desc,
					IndexerConstants.movie1_actors);
			addDocument(indexWriter, "2", IndexerConstants.movie2_title, IndexerConstants.movie2_desc,
					IndexerConstants.movie2_actors);
			addDocument(indexWriter, "3", IndexerConstants.movie1_title, IndexerConstants.movie1_desc,
					IndexerConstants.movie1_actors);
			indexWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addDocument(IndexWriter index, String id, String title, String desc, String actors) {
		Document document = new Document();

		FieldType type = new FieldType();

		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setStoreTermVectors(true);

		document.add(new StringField("movieid", id, Field.Store.YES));

		document.add(new Field("desc", desc, type));
		document.add(new Field("actors", actors, type));
		document.add(new Field("title", title, type));
		try {
			index.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
