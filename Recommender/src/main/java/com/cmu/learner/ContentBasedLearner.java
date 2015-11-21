package com.cmu.learner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
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

import com.cmu.enums.Algorithm;
import com.cmu.lucene.indexer.IndexerConstants;
import com.cmu.model.IDScoreTuple;

public class ContentBasedLearner {

	Map<String, Float> docFrequencies = new HashMap<String, Float>();

	Map<String, Double> fieldWeights = new HashMap<String, Double>();

	public static void main(String[] args) {

		ContentBasedLearner learner = new ContentBasedLearner();
		int start = Integer.parseInt(args[0]);
		int end = Integer.parseInt(args[1]);
		learner.learn(start, end);

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

			if (f.equals("movieid"))
				break;

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

				score += fieldWeights.get(f) * score(terms1Map, terms2Map);
				// System.out.println();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return score;

	}

	public void learn(int start, int end) {

		fieldWeights.put(IndexerConstants.DESC, 0.1);
		fieldWeights.put(IndexerConstants.GENRES, 0.2);
		fieldWeights.put(IndexerConstants.TITLE, 0.25);
		fieldWeights.put(IndexerConstants.WRITER, 0.15);
		fieldWeights.put(IndexerConstants.DIRECTOR, 0.10);
		fieldWeights.put(IndexerConstants.STARS, 0.2);

		Path path = Paths.get(IndexerConstants.directoryPath);
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
			TFIDFSimilarity tfidfSIM = new DefaultSimilarity();

			docFrequencies = getIdfs(reader);

			Fields fields = MultiFields.getFields(reader);
			System.out.println("Number of indexed documents are : " + reader.maxDoc());
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(new File("/home/ubuntu/content_filtering" + start + "_" + end + ".csv")), 8192 * 50);

			for (int i = 0; i < reader.maxDoc(); i++) {
				Queue<IDScoreTuple> minHeap = new PriorityQueue<IDScoreTuple>(20);

				Document doc = reader.document(i);
				String imovId = doc.getField("movieid").stringValue();

				for (int j = start; j < end; j++) {

					if (i != j) {
						Document document = reader.document(j);
						String movId = document.getField("movieid").stringValue();

						Double tfidfScore = calculateScore(fields, i, j, reader, tfidfSIM);

						if (minHeap.size() < 20) {
							minHeap.add(new IDScoreTuple(movId, tfidfScore));
						} else {
							if (minHeap.peek().score < tfidfScore) {
								minHeap.remove();
								minHeap.add(new IDScoreTuple(movId, tfidfScore));
							}

						}

					}
				}

				StringBuilder s = new StringBuilder();

				IDScoreTuple tuple;
				while ((tuple = minHeap.poll()) != null) {
					s.insert(0, ",");
					s.insert(0, df.format(tuple.score));
					s.insert(0, ",");
					s.insert(0, tuple.id);
				}

				writer.write(imovId + "\t" + Algorithm.CONTENT_ALLFIELDS.toString() + "\t" + s.toString() + "\n");
				System.out.println("Done" + i);

			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
