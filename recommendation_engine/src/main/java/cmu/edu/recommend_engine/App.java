package cmu.edu.recommend_engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselineScorer;
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer;
import org.grouplens.lenskit.baseline.UserMeanBaseline;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	LenskitConfiguration config = new LenskitConfiguration();
    			
    	// Use item-item CF to score items
    		config.bind(ItemScorer.class).to(ItemItemScorer.class);
    			// let's use personalized mean rating as the baseline/fallback predictor.
    			// 2-step process:
    			// First, use the user mean rating as the baseline scorer
    			config.bind(GlobalItemScorer.class)
    			      .to(ItemItemGlobalScorer.class);
    			// Second, use the item mean rating as the base for user means
//    			config.bind(UserMeanBaseline.class, ItemScorer.class)
//    			      .to(ItemMeanRatingItemScorer.class);
    			// and normalize ratings by baseline prior to computing similarities
//    			config.bind(UserVectorNormalizer.class)
//    			      .to(BaselineSubtractingUserVectorNormalizer.class);
    			
    			Connection conn = null;
    			
    		    Properties connectionProps = new Properties();
    		    connectionProps.put("user", "ini");
    		    connectionProps.put("password", "a12345");
    		    
    		    Statement stmt = null;
    		    String query = "select * from `ratings` limit 10";
    		                   
    		    
    		    try {
					conn = DriverManager.getConnection(
					               "jdbc:" + "mysql" + "://" +
					               "52.10.143.249" +
					               ":" + "3306" + "/Ericsson-small",
					               connectionProps);
//					 conn.setAutoCommit(false);
					
//					 stmt = conn.createStatement();
//				        ResultSet rs = stmt.executeQuery(query);
//				        while (rs.next()) {
//				            int userID = rs.getInt("userId");
//				            int movieID = rs.getInt("movieId");
//				            System.out.println(userID + " " + movieID);
//				        }
//					System.out.println();
					
					 JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
					 jdbcDaoBuilder.setTableName("test_ratings");
					 jdbcDaoBuilder.setItemColumn("movieId");
					 jdbcDaoBuilder.setUserColumn("userId");
					 jdbcDaoBuilder.setTimestampColumn("timestamp");
					 jdbcDaoBuilder.setRatingColumn("rating");
					 
					
					 JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);
					 
					 config.addComponent(dao);
					 LenskitRecommender rec = LenskitRecommender.build(config);
					 GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();
					  
					 Set<Long> items = new HashSet<Long>();
					 items.add(67l);
					 List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items,20);
					
					 for(ScoredId scoreId : recommendations)
					 {
						 System.out.println(scoreId.getId() + " : " + scoreId.getScore() );
					 }
					 
					 System.out.println("###############################");
					 
					 
					 
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RecommenderBuildException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    }
}
