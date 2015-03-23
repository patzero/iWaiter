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
		} else if (step >= 80 && step < 112) {
			simulator.moveUp();
		} else if (step >= 112 && step < 175) {
			simulator.moveRight();
		} else if (step >= 175 && step < 195) {
			simulator.moveUp();
		} else if (step >= 195 && step < 255) {
			simulator.moveLeft();
		} else if (step >= 255 && step < 265) {
			simulator.moveUp();
		} else if (step >= 265 && step < 280) {
			simulator.moveLeft();
		} else if (step >= 280 && step < 310) {
			simulator.moveDown();
		} else if (step >= 310 && step < 345) {
			simulator.moveLeft();
		} else if (step >= 340) {
			simulator.stop();
		}
	}
}
