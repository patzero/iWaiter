package com.insta.iwaiter.services;

import com.insta.iwaiter.engine.Engine;

public interface BrainService {
	public void bindSimulatorService(Engine engine);
	public void activation();
	public void stepAction();
}
