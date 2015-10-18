package com.cmu.interfaces;

import com.cmu.model.UserFeedback;

public interface EvaluationDao {
	
	public void submitFeedback(UserFeedback feedback);

}
