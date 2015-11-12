package com.cmu.learner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.cmu.lucene.indexer.IndexerConstants;

public class ContentBasedLearner {

	Map<String, Float> docFrequencies = new HashMap<String, Float>();

	public static void main(String[] args) {

		ContentBasedLearner learner = new ContentBasedLearner();
		learner.learn();
	}

	private Map<String, Float> getIdfs(IndexReader reader) throws IOException {
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

	private double score(Map<String, Float> map_one, Map<String, Float> map_two) {
		double score = 0.0;

		Iterator<String> iterator = map_one.keySet().iterator();

		while (iterator.hasNext()) {

			String term = iterator.next();

			Float freq2 = map_two.get(term);

			if (freq2 != null) {
				Float freq1 = map_one.get(term);

				score += (freq1 * freq2) / docFrequencies.get(term);

			}

		}

		return score;
	}

	private double calculateScore(Fields fields, int i, int j, IndexReader reader, TFIDFSimilarity tfSimilarity) {
		double score = 0.0;

		for (String f : fields) {
			try {

				Terms vector1 = reader.getTermVector(i, f);
				Terms vector2 = reader.getTermVector(j, f);

				if (vector1 == null || vector2 == null)
					return score;

				HashMap<String, Float> terms1Map = new HashMap<String, Float>();
				HashMap<String, Float> terms2Map = new HashMap<String, Float>();

				TermsEnum termEnum = vector1.iterator();

				BytesRef term = null;

				while ((term = termEnum.next()) != null) {
					String term1 = term.utf8ToString();
					long termFreq = termEnum.totalTermFreq();

					terms1Map.put(term1, tfSimilarity.tf(termFreq));
				}

				termEnum = vector2.iterator();

				while ((term = termEnum.next()) != null) {
					String term1 = term.utf8ToString();
					long termFreq = termEnum.totalTermFreq();
					terms2Map.put(term1, tfSimilarity.tf(termFreq));
				}

				score += score(terms1Map, terms2Map);
				System.out.println();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return score;

	}

	public void learn() {

		Queue<Double> maxHeap = new PriorityQueue<Double>(100);

		Path path = Paths.get(IndexerConstants.directoryPath);

		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
			TFIDFSimilarity tfidfSIM = new DefaultSimilarity();

			docFrequencies = getIdfs(reader);

			Fields fields = MultiFields.getFields(reader);

			for (int i = 0; i < reader.maxDoc(); i++) {

				for (int j = i + 1; j < reader.maxDoc(); j++) {		
					System.out.println("Score between " +i +" j " + j + calculateScore(fields, i, j, reader, tfidfSIM));
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
