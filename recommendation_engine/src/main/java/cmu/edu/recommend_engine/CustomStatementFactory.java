package cmu.edu.recommend_engine;

import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.sql.SQLStatementFactory;

public class CustomStatementFactory implements SQLStatementFactory{

	public String prepareEvents(SortOrder arg0) {
		String eventStatement = "Select * from ratings";
		
		if(arg0.equals(SortOrder.ITEM))
			return eventStatement +" order by itemId";
		
		if(arg0.equals(SortOrder.USER))
			return eventStatement +" order by userId";
		
		if(arg0.equals(SortOrder.TIMESTAMP))
			return eventStatement +" order by timestamp";
		
		return eventStatement;
	}

	public String prepareItemEvents() {
		
		return "Select * from ratings where itemId = ? order by userId, timestamp";
	}

	public String prepareItemUsers() {
			return "Select userId, itemId from ratings where itemId = ? order by userId, timestamp";
	}

	public String prepareItems() {
		return "Select movieId from ratings";
	}

	public String prepareUserEvents() {
		return "Select * from ratings where userId = ? order timestamp";
	}

	public String prepareUsers() {
		return "Select userId from ratings";
	}
	
	

}
