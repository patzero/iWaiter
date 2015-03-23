package com.insta.iwaiter.services;

import com.insta.iwaiter.userinterface.Viewer;

public interface EngineService {

	public void bindData(DataService d);

	public void bindViewer(Viewer v);

	public void bindBrainService(BrainService b);

	public void start();
	
	public void stop();

	public void moveLeft();

	public void moveRight();

	public void moveUp();

	public void moveDown();

}