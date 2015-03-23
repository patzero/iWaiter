package com.insta.iwaiter.AI;

import com.insta.iwaiter.engine.Engine;
import com.insta.iwaiter.services.BrainService;

public class WaiterExplosion  implements BrainService {
	private Engine simulator;
	private int step;
	
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
		} else if (step >= 65 ) {
			simulator.moveRight();
		} 
	}

}
