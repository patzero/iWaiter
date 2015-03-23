package com.insta.iwaiter.services;

import com.insta.iwaiter.tools.Position;

public interface DataService {

	public int getStepNumber();

	public void setStepNumber(int stepNumber);

	public Position getBotPosition();

	public void setBotPosition(Position botPosition);

}