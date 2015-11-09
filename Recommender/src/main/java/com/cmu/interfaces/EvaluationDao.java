package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.EvaluationStatistics;
import com.cmu.model.UserFeedback;

public interface EvaluationDao {
	
	public void submitFeedback(UserFeedback feedback);
	public List<EvaluationStatistics> retrieveStatistics();

}
