package com.insta.iwaiter.data;

import com.insta.iwaiter.tools.SimParameters;
import com.insta.iwaiter.tools.Position;


public class Data {
	
	private Position botPosition;
	private int stepNumber;
	
	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public Data(){
		setBotPosition(new Position(SimParameters.robotPosX, SimParameters.robotPosY));
	}

	public Position getBotPosition() {
		return botPosition;
	}

	public void setBotPosition(Position botPosition) {
		this.botPosition = botPosition;
	}
}
