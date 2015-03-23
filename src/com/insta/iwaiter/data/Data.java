package com.insta.iwaiter.data;

import com.insta.iwaiter.services.DataService;
import com.insta.iwaiter.tools.SimParameters;
import com.insta.iwaiter.tools.Position;


public class Data implements DataService {
	
	private Position botPosition;
	private int stepNumber;

	@Override
	public int getStepNumber() {
		return stepNumber;
	}

	@Override
	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public Data(){
		setBotPosition(new Position(SimParameters.robotPosX, SimParameters.robotPosY));
	}

	@Override
	public Position getBotPosition() {
		return botPosition;
	}

	@Override
	public void setBotPosition(Position botPosition) {
		this.botPosition = botPosition;
	}
}
