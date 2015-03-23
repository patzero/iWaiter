package com.insta.iwaiter.AI;

import java.util.Random;

import com.insta.iwaiter.engine.Engine;
import com.insta.iwaiter.services.BrainService;

public class WaiterBrain implements BrainService {
	private Engine simulator;
	private int step;

	public WaiterBrain() {}

	@Override
	public void bindSimulatorService(Engine engine) {
		simulator = engine;
	}

	@Override
	public void activation() {
		simulator.moveRight();
	}

	@Override
	public void stepAction() {
		step = simulator.getStepNumber();
		System.out.println("Step : " + step);
		if (step < 32) {
			simulator.moveRight();
		} else if (step >= 32 && step < 65) {
			simulator.moveDown();
		} else if (step >= 65 && step < 80) {
			simulator.moveRight();
		} else if (step >= 80 && step < 143) {
			simulator.moveUp();
		} else if (step >= 143 && step < 157) {
			simulator.moveLeft();
		} else if (step >= 157 && step < 188) {
			simulator.moveDown();
		} else if (step >= 188 && step < 220) {
			simulator.moveLeft();
		} else if (step >= 220) {
			simulator.stop();
		}
	}
}
