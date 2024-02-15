package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class Statistics {

	private int totalWins;
	private int totalLosses;
	private int totalDraws;
	
	public Statistics() {}
	
	public Statistics(int totalWins, int totalLosses, int totalDraws){
		this.totalWins = totalWins;
		this.totalLosses = totalLosses;
		this.totalDraws = totalDraws;
	}

	public int getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}

	public int getTotalLosses() {
		return totalLosses;
	}

	public void setTotalLosses(int totalLosses) {
		this.totalLosses = totalLosses;
	}

	public int getTotalDraws() {
		return totalDraws;
	}

	public void setTotalDraws(int totalDraws) {
		this.totalDraws = totalDraws;
	}
	
}
