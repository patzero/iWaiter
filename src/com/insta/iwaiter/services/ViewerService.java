package com.insta.iwaiter.services;

import javafx.scene.Parent;

import com.insta.iwaiter.engine.Engine;

public interface ViewerService {

	public void bindData(DataService d);

	public void bindEngine(Engine e);

	public void initializeViewer();

	public Parent getPanel();

}