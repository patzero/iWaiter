package com.insta.iwaiter.engine;

import com.insta.iwaiter.data.Data;
import com.insta.iwaiter.services.BrainService;
import com.insta.iwaiter.tools.SimParameters;
import com.insta.iwaiter.tools.Position;
import com.insta.iwaiter.userinterface.Viewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Engine {
	//	private enum USERCOMMAND { LEFT, RIGHT, UP, DOWN, NONE};
	//	private USERCOMMAND command;

	private Data data;
	private Viewer viewer;
	private BrainService brain;
	
	private Boolean upKey;
	private Boolean downKey;
	private Boolean leftKey;
	private Boolean rightKey;
	private Timeline botEngine;
	private int imgIndex = 0;
	private boolean isPlayed = false;
	
	public Engine() {
		upKey = false;
		downKey = false;
		leftKey = false;
		rightKey = false;
		botEngine = new Timeline();
	}
	
	public void bindData(Data d) {
		data = d;
	}
	
	public void bindViewer(Viewer v) {
		viewer = v;
	}
	
	public void bindBrainService(BrainService b) {
		brain = b;
	}

	/** 
	 * stimulate robot movement with brain service
	 * **/
	public void start() {
		reset();
		brain.activation();
		botEngine.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				brain.stepAction();
				for(Rectangle rec : viewer.getObstacles()) {
					if(rec.getBoundsInLocal().intersects(data.getBotPosition().x + 7, data.getBotPosition().y + 18, 19, 10)) {
						viewer.setRobotExplosion();
						viewer.playBombSound();
						pause();
						return;
					}
				}
				for(Rectangle table : viewer.getTables()) {
					if(table.getBoundsInLocal().intersects(data.getBotPosition().x + 7, data.getBotPosition().y + 18, 19, 10)) {
						viewer.setRobotExplosion();
						viewer.playBombSound();
						pause();
						return;
					}
				}
				for(Circle tableFamily : viewer.getTablesFamily()) {
					if(tableFamily.getBoundsInLocal().intersects(data.getBotPosition().x + 12, data.getBotPosition().y + 22, 10, 5)) {
						viewer.setRobotExplosion();
						viewer.playBombSound();
						pause();
						return;
					}
				}
				data.setStepNumber(data.getStepNumber()+1);
			}   
		};
		KeyFrame keyFrame = new KeyFrame(Duration.millis(SimParameters.enginePaceMillis), onFinished);
		botEngine.getKeyFrames().add(keyFrame);
		botEngine.play();
		setPlayed(true);
	}
	
	/** 
	 * method to manually stimulate robot movement with arrow key 
	 * **/
	public void startController() {
		botEngine.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(downKey) {
					moveDown();
					System.out.println("Step : " + data.getStepNumber());
					data.setStepNumber(data.getStepNumber()+1);
				}
				if(upKey) {
					moveUp();
					System.out.println("Step : " + data.getStepNumber());
					data.setStepNumber(data.getStepNumber()+1);
				}
				if(rightKey) {
					moveRight();
					System.out.println("Step : " + data.getStepNumber());
					data.setStepNumber(data.getStepNumber()+1);
				}
				if(leftKey) {
					moveLeft();
					System.out.println("Step : " + data.getStepNumber());
					data.setStepNumber(data.getStepNumber()+1);
				}
				for(Rectangle obs : viewer.getObstacles()) {
					if(obs.getBoundsInLocal().intersects(data.getBotPosition().x + 7, data.getBotPosition().y + 18, 19, 10)) {
						System.out.println("Boom Boomm Boooommmmmmmm !!!!!");
						viewer.setRobotExplosion();
						//pause();
						return;
					}
				}
				for(Rectangle table : viewer.getTables()) {
					if(table.getBoundsInLocal().intersects(data.getBotPosition().x + 7, data.getBotPosition().y + 18, 19, 10)) {
						System.out.println("Boom Boomm Boooommmmmmmm !!!!!");
						viewer.setRobotExplosion();
						return;
					}
				}
				for(Circle tableFamily : viewer.getTablesFamily()) {
					if(tableFamily.getBoundsInLocal().intersects(data.getBotPosition().x + 12, data.getBotPosition().y + 22, 10, 5)) {
						System.out.println("Boom Boomm Boooommmmmmmm !!!!!");
						viewer.setRobotExplosion();
						return;
					}
				}
			}   
		};
		KeyFrame keyFrame = new KeyFrame(Duration.millis(100), onFinished);
		botEngine.getKeyFrames().add(keyFrame);
		botEngine.play();
	}
	
	public void stop() {
		botEngine.getKeyFrames().clear();
		botEngine.stop();
		setPlayed(false);
	}
	
	public void reset() {
		data.setStepNumber(0);
		data.setBotPosition(new Position(SimParameters.robotPosX, SimParameters.robotPosY));
	}
	
	public void reStart() {
		stop();
		reset();
		start();
	}
	
	public void play() {
		botEngine.play();
		setPlayed(true);
	}
	public void pause() {
		botEngine.pause();
		setPlayed(false);
	}
	
	public void moveLeft(){
		viewer.getRobotAvatar().setImage(viewer.getRobotLeft().get(++imgIndex%3));
		data.setBotPosition(new Position(data.getBotPosition().x-5, data.getBotPosition().y));
	}

	public void moveRight(){
		viewer.getRobotAvatar().setImage(viewer.getRobotRight().get(++imgIndex%3));
		data.setBotPosition(new Position(data.getBotPosition().x+5, data.getBotPosition().y));
	}

	public void moveUp(){
		viewer.getRobotAvatar().setImage(viewer.getRobotUp().get(++imgIndex%3));
		data.setBotPosition(new Position(data.getBotPosition().x, data.getBotPosition().y-5));
	}

	public void moveDown(){
		viewer.getRobotAvatar().setImage(viewer.getRobotDown().get(++imgIndex%3));
		data.setBotPosition(new Position(data.getBotPosition().x, data.getBotPosition().y+5));
	}
	
	public void movePause(){
		data.setBotPosition(new Position(data.getBotPosition().x, data.getBotPosition().y));
	}

	public Boolean getUpKey() {
		return upKey;
	}

	public void setUpKey(Boolean upKey) {
		this.upKey = upKey;
	}

	public Boolean getDownKey() {
		return downKey;
	}

	public void setDownKey(Boolean downKey) {
		this.downKey = downKey;
	}

	public Boolean getLeftKey() {
		return leftKey;
	}

	public void setLeftKey(Boolean leftKey) {
		this.leftKey = leftKey;
	}

	public Boolean getRightKey() {
		return rightKey;
	}

	public void setRightKey(Boolean rightKey) {
		this.rightKey = rightKey;
	}
	
	public int getStepNumber() {
		return data.getStepNumber();
	}

	public boolean isPlayed() {
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}



}
