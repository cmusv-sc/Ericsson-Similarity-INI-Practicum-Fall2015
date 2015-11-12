package com.cmu.lucene.indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class LuceneIndexReader {

	public static void main(String[] args) {
		
		LuceneIndexReader reader = new LuceneIndexReader();
		
		reader.indexReader();
		
	}

	public void indexReader() {
		Path path = Paths.get(IndexerConstants.directoryPath);
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(path));

			getIdfs(reader, "title");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*** GET ALL THE IDFs ***/
	private Map<String, Float> getIdfs(IndexReader reader, String field) throws IOException {
		Map<String, Float> docFrequencies = new HashMap<String, Float>();
		/** GET FIELDS **/
		Fields fields = MultiFields.getFields(reader); // Get the Fields of the
														// index

		TFIDFSimilarity tfidfSIM = new DefaultSimilarity();

		for (String f : fields) {
			TermsEnum termEnum = MultiFields.getTerms(reader, f).iterator();
			BytesRef bytesRef;
			while ((bytesRef = termEnum.next()) != null) {
				if (termEnum.seekExact(bytesRef)) {
					String term = bytesRef.utf8ToString();
					float idf = tfidfSIM.idf(termEnum.docFreq(), reader.numDocs());
					docFrequencies.put(term, idf);
				}
			}
		}

		return docFrequencies;
	}

}
