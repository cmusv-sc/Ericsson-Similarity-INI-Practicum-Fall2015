package com.cmu.interfaces;

import java.util.List;

public interface AlgorithmsDao {
	public List<String> getAlgorithms();
	public List<String> getEnabledAlgorithms();
	public void enableAlgorithm(String algorithm);
	public void disableAlgorithm(String algorithm);
	
}
