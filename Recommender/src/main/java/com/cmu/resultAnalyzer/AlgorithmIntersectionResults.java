package com.cmu.resultAnalyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cmu.dao.RecommendationDaoImpl;
import com.cmu.enums.Algorithm;
import com.cmu.model.Recommendation;

public class AlgorithmIntersectionResults {
    public static void main(String [] args) {
        String fileName = "/Users/LucasColucci/Downloads/similarityv2.csv";

        String line = null;

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //first line is header
            line = bufferedReader.readLine();
            HashMap<List<String>,Integer> intersections = new HashMap<List<String>,Integer>();
            ArrayList<String> file = new ArrayList<String>();
            while((line = bufferedReader.readLine()) != null)
            	file.add(line);
            int i = 0;
            while(true) {
            	ArrayList<String> pearson = new ArrayList<String>();
            	ArrayList<String> tmdb = new ArrayList<String>();
            	ArrayList<String> content631 = new ArrayList<String>();
            	ArrayList<String> contentAll = new ArrayList<String>();
            	ArrayList<String> cosine = new ArrayList<String>();
            	
            	//CONTENT_631
            	if(i >= file.size())
            		break;
            	line = file.get(i++);
            	String evaluatedId = line.split(",")[0];
            	if(line == null)
            		break;
            	if (line.split(",")[1].equalsIgnoreCase("CONTENT_631"))
            		content631 = getIds(line.split(","));
            	
            	//CONTENT_ALL
            	if(i >= file.size())
            		break;
            	line = file.get(i++);
            	if(line == null)
            		break;
            	if(!line.split(",")[0].equalsIgnoreCase(evaluatedId))
            		i--;
            	if (line.split(",")[1].equalsIgnoreCase("CONTENT_ALLFIELDS"))
            		contentAll = getIds(line.split(","));
            	
            	//COSINE_SIMILARITY
            	if(i >= file.size())
            		break;
            	line = file.get(i++);
            	if(line == null)
            		break;
            	if(!line.split(",")[0].equalsIgnoreCase(evaluatedId))
            		i--;
            	if (line.split(",")[1].equalsIgnoreCase("COSINE_SIMILARITY"))
            		cosine = getIds(line.split(","));
            	
            	//PEARSON_COEFFICIENT
            	if(i >= file.size())
            		break;
            	line = file.get(i++);
            	if(line == null)
            		break;
            	if(!line.split(",")[0].equalsIgnoreCase(evaluatedId))
            		i--;
            	if (line.split(",")[1].equalsIgnoreCase("PEARSON_COEFFICIENT"))
            		pearson = getIds(line.split(","));
            	
            	//TMDB_SIMILARITY
            	if(i >= file.size())
            		break;
            	line = file.get(i++);
            	if(line == null)
            		break;
            	if(!line.split(",")[0].equalsIgnoreCase(evaluatedId))
            		i--;
            	if (line.split(",")[1].equalsIgnoreCase("TMDB_SIMILARITY"))
            		tmdb = getIds(line.split(","));
            	
            	
            	for(String id : content631){
            		int intersection[] = {1,0,0,0,0};
            		if(arrayContains(contentAll, id))
            			intersection[1] = 1;
            		if(arrayContains(cosine, id))
            			intersection[2] = 1;
            		if(arrayContains(pearson, id))
            			intersection[3] = 1;
            		if(arrayContains(tmdb, id))
            			intersection[4] = 1;

            		if(intersections.get(toAlgorithmList(intersection)) == null)
            			intersections.put(toAlgorithmList(intersection), 1);
            		else
            			intersections.put(toAlgorithmList(intersection), intersections.get(toAlgorithmList(intersection))+1);
            	}
            	
            	for(String id : contentAll){
            		int intersection[] = {0,1,0,0,0};
            		if(arrayContains(content631, id))
            			intersection[0] = 1;
            		if(arrayContains(cosine, id))
            			intersection[2] = 1;
            		if(arrayContains(pearson, id))
            			intersection[3] = 1;
            		if(arrayContains(tmdb, id))
            			intersection[4] = 1;

            		if(intersections.get(toAlgorithmList(intersection)) == null)
            			intersections.put(toAlgorithmList(intersection), 1);
            		else
            			intersections.put(toAlgorithmList(intersection), intersections.get(toAlgorithmList(intersection))+1);
            	}
            	for(String id : cosine){
            		int intersection[] = {0,0,1,0,0};
            		if(arrayContains(content631, id))
            			intersection[0] = 1;
            		if(arrayContains(contentAll, id))
            			intersection[1] = 1;
            		if(arrayContains(pearson, id))
            			intersection[3] = 1;
            		if(arrayContains(tmdb, id))
            			intersection[4] = 1;
 
            		if(intersections.get(toAlgorithmList(intersection)) == null)
            			intersections.put(toAlgorithmList(intersection), 1);
            		else
            			intersections.put(toAlgorithmList(intersection), intersections.get(toAlgorithmList(intersection))+1);
            	}
            	for(String id : pearson){
            		int intersection[] = {0,0,0,1,0};
            		if(arrayContains(content631, id))
            			intersection[0] = 1;
            		if(arrayContains(contentAll, id))
            			intersection[1] = 1;
            		if(arrayContains(cosine, id))
            			intersection[2] = 1;
            		if(arrayContains(tmdb, id))
            			intersection[4] = 1;

            		if(intersections.get(toAlgorithmList(intersection)) == null)
            			intersections.put(toAlgorithmList(intersection), 1);
            		else
            			intersections.put(toAlgorithmList(intersection), intersections.get(toAlgorithmList(intersection))+1);
            	}
            	for(String id : tmdb){
            		int intersection[] = {0,0,0,0,1};
            		if(arrayContains(content631, id))
            			intersection[0] = 1;
            		if(arrayContains(contentAll, id))
            			intersection[1] = 1;
            		if(arrayContains(cosine, id))
            			intersection[2] = 1;
            		if(arrayContains(pearson, id))
            			intersection[3] = 1;

            		if(intersections.get(toAlgorithmList(intersection)) == null)
            			intersections.put(toAlgorithmList(intersection), 1);
            		else
            			intersections.put(toAlgorithmList(intersection), intersections.get(toAlgorithmList(intersection))+1);
            	}
            	
            	
            	
            }   
            printIntersections(intersections);

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
    }

	private static void printIntersections(HashMap<List<String>, Integer> intersections) {
		for(List<String> l : intersections.keySet())
			System.out.println(l + ";" + intersections.get(l)/l.size());
		
	}

	private static boolean hasIntersection(int[] intersection) {
		int numberOfIntersections = 0;
		for(int i : intersection)
			if(i == 1)
				numberOfIntersections++;
			
		if(numberOfIntersections > 1)
			return true;
		return false;
	}

	private static List<String> toAlgorithmList(int[] intersection) {
		ArrayList<String> algorithms = new ArrayList<String>(); 
		if(intersection[0] == 1)
			algorithms.add("CONTENT631");
		if(intersection[1] == 1)
			algorithms.add("CONTENTALL");
		if(intersection[2] == 1)
			algorithms.add("COSINE");
		if(intersection[3] == 1)
			algorithms.add("PEARSON");
		if(intersection[4] == 1)
			algorithms.add("TMDB");
		
		return algorithms;
	}

	private static boolean arrayContains(ArrayList<String> array, String id) {
		if(array == null)
			return false;
		for(int i = 0; i < array.size(); i++)
			if(id.contentEquals(array.get(i)))
				return true;
		return false;
	}

	private static ArrayList<String> getIds(String[] strings) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 2; i < strings.length; i++){
			result.add(strings[i]);
		}
		
		
		return result;
	}
}
