package com.insta.iwaiter.userinterface;

import java.awt.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.insta.iwaiter.engine.Engine;
import com.insta.iwaiter.services.BrainService;
import com.insta.iwaiter.services.DataService;
import com.insta.iwaiter.services.ViewerService;
import com.insta.iwaiter.tools.FileLoader;
import com.insta.iwaiter.tools.SimParameters;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Viewer implements ViewerService {

	private DataService data;
	private Engine engine;
	private FileLoader fileLoader;

	// Setting Images url
	private String srcUrl = "file:src";
	private Image restaurantImg = new Image(srcUrl + "/images/background.png");
	private Image explosionImg = new Image(srcUrl + "/images/boom.png");
	private Image tableImg = new Image(srcUrl + "/images/table.png");
	private Image tableFamilyImg = new Image(srcUrl + "/images/table-familliale.png");

	private String bombFile = new File("src/media/explosion.mp3").toURI().toString();
	private Media bombMedia;
	private MediaPlayer bombPlayer;

	private BorderPane rootLayout;
	private	Group rootPanel;
	private Group obstaclesLayer;
	private StackPane tablePane;
	private StackPane tableFamilyPane;
	private ImageView robotAvatar;
	private ImageView restaurantPlan;

	// Lists of Robot sprite animation
	private List<Image> robotUp =  new ArrayList<Image>() {{
		add(new Image(srcUrl + "/images/Robot/up0.png"));
		add(new Image(srcUrl + "/images/Robot/up1.png"));
		add(new Image(srcUrl + "/images/Robot/up2.png"));
	}};
	private List<Image> robotDown =  new ArrayList<Image>() {{
		add(new Image(srcUrl + "/images/Robot/down0.png"));
		add(new Image(srcUrl + "/images/Robot/down1.png"));
		add(new Image(srcUrl + "/images/Robot/down2.png"));
	}};
	private List<Image> robotLeft =  new ArrayList<Image>() {{
		add(new Image(srcUrl + "/images/Robot/left0.png"));
		add(new Image(srcUrl + "/images/Robot/left1.png"));
		add(new Image(srcUrl + "/images/Robot/left2.png"));
	}};
	private List<Image> robotRight =  new ArrayList<Image>() {{
		add(new Image(srcUrl + "/images/Robot/right0.png"));
		add(new Image(srcUrl + "/images/Robot/right1.png"));
		add(new Image(srcUrl + "/images/Robot/right2.png"));
	}};

	private List<Rectangle> obstacles =  new ArrayList<Rectangle>() {{
		//add(new Rectangle(x, y, w, h));
		/** Wall **/
		add(new Rectangle(0, 0, 872, 15)); // #1
		add(new Rectangle(0, 0, 205, 160));
		add(new Rectangle(0, 160, 50, 30));
		add(new Rectangle(0, 190, 205, 250));
		add(new Rectangle(0, 440, 150, 100)); // #5
		add(new Rectangle(0, 540, 60, 30));
		add(new Rectangle(0, 570, 140, 152));
		add(new Rectangle(140, 595 , 30, 127));
		add(new Rectangle(170, 620, 25, 102));
		add(new Rectangle(195, 650, 10, 72)); // #10
		add(new Rectangle(205, 690, 10, 32));
		add(new Rectangle(260, 35, 30, 275));
		add(new Rectangle(215, 717, 657, 5));
		add(new Rectangle(830, 15, 42, 702));
	}};

	private List<Rectangle> tables =  new ArrayList<Rectangle>(){{
		/** Table normal **/
		add(new Rectangle(350, 100, 100, 59));
		add(new Rectangle(500, 100, 100, 59));
		add(new Rectangle(650, 100, 100, 59));

		add(new Rectangle(350, 200, 100, 59));
		add(new Rectangle(500, 200, 100, 59));
		add(new Rectangle(650, 200, 100, 59));

		add(new Rectangle(350, 300, 100, 59));
		add(new Rectangle(500, 300, 100, 59));
		add(new Rectangle(650, 300, 100, 59));

		add(new Rectangle(350, 400, 100, 59));
		add(new Rectangle(500, 400, 100, 59));
		add(new Rectangle(650, 400, 100, 59));
	}};

	private List<Circle> tablesFamily =  new ArrayList<Circle>(){{
		/** Family Table **/
		add(new Circle(485, 575, 64));
		add(new Circle(625, 575, 64));
	}};

	public Viewer() {
		initializeViewer();
	}

	@Override
	public void bindData(DataService d) {
		data = d;
	}

	@Override
	public void bindEngine(Engine e) {
		engine = e;
	}

	@Override
	public void initializeViewer() {
		// Create list of brain files
		fileLoader = new FileLoader();

		rootLayout = new BorderPane();
		rootLayout.setPrefWidth(SimParameters.defaultWidth);
		rootLayout.setPrefHeight(SimParameters.defaultHeight);
		rootLayout.setLeft(getSimulatorPanel());
		rootLayout.setTop(getMenu());
		rootLayout.setRight(getRightPanel());
		rootLayout.setBottom(getFooterPanel());
	}

	private Group getSimulatorPanel() {		
		rootPanel = new Group();

		// Initialize restaurant and robot Image view
		restaurantPlan = new ImageView(restaurantImg);
		robotAvatar = new ImageView(robotDown.get(1));

		// Initialize all rectangles in obstacles list
		int i = 1;
		for(Rectangle rec : obstacles) {
			rec.setFill(Color.rgb(75, 145, 215, .50));
			rec.setStroke(Color.BLACK);
			// Set a Tooltip to easily identifies each rectangles
			Tooltip t = new Tooltip("Obs " + i);
			Tooltip.install(rec, t);
			i++;
		} 

		// Create Obstacles layer and add it's element
		obstaclesLayer = new Group();
		obstaclesLayer.setOpacity(0.0);
		Rectangle recPlayer = new Rectangle(SimParameters.robotPosX+12, SimParameters.robotPosY+22, 10, 5);
		recPlayer.setFill(Color.LIME);
		obstaclesLayer.getChildren().addAll(obstacles);
		obstaclesLayer.getChildren().addAll(tables);
		obstaclesLayer.getChildren().addAll(tablesFamily);
		obstaclesLayer.getChildren().add(recPlayer);

		// Create Tables layer with tables rectangle list and images
		tablePane = new StackPane();
		for(Rectangle t : tables) {
			t.setFill(Color.rgb(75, 145, 215, .50));
			t.setStroke(Color.BLACK);
			ImageView tableImage = new ImageView(tableImg);
			tableImage.setTranslateX(t.getX());
			tableImage.setTranslateY(t.getY());
			tablePane.getChildren().add(tableImage);
		}

		tableFamilyPane = new StackPane();
		for(Circle tf : tablesFamily) {
			tf.setFill(Color.rgb(75, 145, 215, .50));
			tf.setStroke(Color.BLACK);
			ImageView tableImage = new ImageView(tableFamilyImg);
			tableImage.setTranslateX(tf.getCenterX() - 150/2);
			tableImage.setTranslateY(tf.getCenterY() - 150/2);
			tableImage.setFitWidth(150);
			tableImage.setFitHeight(150);
			tableFamilyPane.getChildren().add(tableImage);
		}

		// controller event handler
		controllerKeyEventHandler();

		// Add everyhing to root panel
		rootPanel.getChildren().addAll(restaurantPlan, tablePane, tableFamilyPane, robotAvatar, obstaclesLayer);
		return rootPanel;
	}

	private MenuBar getMenu()
	{
		// Create Menubar and it's item and set it at top of root layout
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		MenuItem exitApp = new MenuItem("Close");
		menuFile.getItems().addAll(exitApp);
		exitApp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	Platform.exit();
            }
        }); 
		
		Menu menuHelp = new Menu("Help");
		MenuItem menuAbout = new MenuItem("About");
		menuHelp.getItems().addAll(menuAbout);
		menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	Stage dialog = new Stage();
        		dialog.setWidth(600);
        		dialog.setHeight(200);
        		dialog.initStyle(StageStyle.UTILITY);
        		Scene scene = new Scene(new Group(new Text(50, 50, SimParameters.aboutMsg)));
        		dialog.setScene(scene);
        		dialog.show();
            }
        });
		menuBar.getMenus().addAll(menuFile, menuHelp);
		return menuBar;
	}

	private HBox getRightPanel()
	{
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		vbox.setPrefWidth(278);
		vbox.setPrefHeight(722);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.setId("vertical-container");
		

		ComboBox<String> combobox = new ComboBox<String>();
		combobox.setId("select");
		combobox.setLayoutX(5);
		combobox.setLayoutX(45);
		combobox.setPromptText("Choose Robot Brain");
		
		// Add brain file name to combobox list
		for (String fileName : fileLoader.getFileNames()) {
			combobox.getItems().add(fileName);
		}

		Button playButton = new Button("Start simulation");
		playButton.setId("play");
		playButton.getStyleClass().add("simulator-button");
		playButton.setDisable(true);
		
		Button stopButton = new Button("Stop simulation");
		stopButton.setDisable(true);
		stopButton.getStyleClass().add("simulator-button");

		playButton.setTranslateX(6);
		playButton.setTranslateY(60);
		playButton.setPrefWidth(140);
		
		stopButton.setTranslateX(6);
		stopButton.setTranslateY(80);
		stopButton.setPrefWidth(140);
		    
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				engine.start();
				playButton.setDisable(true);
				stopButton.setDisable(false);
				combobox.setDisable(true);
				event.consume();
			}
		});
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent event) {
				engine.stop();
				playButton.setDisable(false);
				stopButton.setDisable(true);
				combobox.setDisable(false);
				event.consume();
			}
		});
		playButton.setFocusTraversable(false);
		stopButton.setFocusTraversable(false);

		combobox.valueProperty().addListener(new ChangeListener<String>() {
			@Override 
			public void changed(ObservableValue ov, String deselectedString, String selectedString) {
				// Instantiate brain service from a selected list and bind to engine service
				BrainService brain = SimParameters.instantiate("com.insta.iwaiter.AI." + selectedString, BrainService.class);
				engine.bindBrainService(brain);
				brain.bindSimulatorService(engine);
				playButton.setDisable(false);
			}    
		});
		combobox.setFocusTraversable(false);

		vbox.getChildren().addAll(combobox, playButton, stopButton);   
		hbox.getChildren().addAll(new Separator(Orientation.VERTICAL), vbox);     

		return hbox;
	}

	private VBox getFooterPanel()
	{
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		vbox.setPrefWidth(1150);
		vbox.setPrefHeight(128);
		vbox.setAlignment(Pos.TOP_LEFT);
		vbox.setStyle("-fx-border-style: solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: black");

		vbox.getChildren().addAll(
				new Text(SimParameters.keyBoardMsg));   
		hbox.getChildren().addAll(new Separator(Orientation.VERTICAL), vbox); 

		return vbox;
	}

	/* 
	 * Refresh every millisecond by Main -> AnimationTimer
	 */
	@Override
	public Parent getPanel() {
		// Get robotAvatar ImageView from root panel and set it's new position
		rootPanel.getChildren().get(3).setTranslateX(data.getBotPosition().x);
		rootPanel.getChildren().get(3).setTranslateY(data.getBotPosition().y);
		return rootLayout;
	}

	public void setRobotExplosion() {
		// Replace robot image with explosion  
		robotAvatar.setImage(explosionImg);
		robotAvatar.setFitWidth(32);
		robotAvatar.setFitHeight(32); 
	}

	private void controllerKeyEventHandler() {
		// Set event handle on robot avartar
		robotAvatar.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.UP)) {
					System.out.println("UP : y=" + robotAvatar.getTranslateY());
					engine.setUpKey(true);
				}
				if (event.getCode().equals(KeyCode.DOWN)) {
					System.out.println("DOWN : y=" + robotAvatar.getTranslateY());
					engine.setDownKey(true);
				}
				if (event.getCode().equals(KeyCode.LEFT)) {
					System.out.println("LEFT : x=" + robotAvatar.getTranslateX());
					engine.setLeftKey(true);
				}
				if (event.getCode().equals(KeyCode.RIGHT)) {
					System.out.println("RIGHT : x=" + robotAvatar.getTranslateX());
					engine.setRightKey(true);
				}
				if (event.getCode().equals(KeyCode.O)) {
					// Show obstacles layer
					obstaclesLayer.setOpacity(1.0);
				}
				if (event.getCode().equals(KeyCode.SPACE)) {
					if (engine.isPlayed()) {
						engine.pause();
					} else  {
						engine.play();
					}
				}
				event.consume();
			}
		});
		robotAvatar.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.UP))
					engine.setUpKey(false);
				if (event.getCode().equals(KeyCode.DOWN))
					engine.setDownKey(false);
				if (event.getCode().equals(KeyCode.LEFT)) 
					engine.setLeftKey(false);
				if (event.getCode().equals(KeyCode.RIGHT))
					engine.setRightKey(false);
				if (event.getCode().equals(KeyCode.O))
					obstaclesLayer.setOpacity(0.0);
				event.consume();
			}
		});
		// set focus on bot avatar
		robotAvatar.setFocusTraversable(true);
	}

	public void showFailedPopup() {
		Stage dialog = new Stage();
		dialog.setWidth(400);
		dialog.setHeight(200);
		dialog.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(new Group(new Text(50, 50, "Simulation Failed !!!!!")));
		dialog.setScene(scene);
		dialog.show();
		playBombSound();
	}

	public void playBombSound() {
		bombMedia = new Media(bombFile);
		bombPlayer = new MediaPlayer(bombMedia);
		bombPlayer.play();
	}

	public ImageView getRobotAvatar() {
		return robotAvatar;
	}

	public void setRobotAvatar(ImageView robotAvatar) {
		this.robotAvatar = robotAvatar;
	}

	public List<Image> getRobotUp() {
		return robotUp;
	}

	public void setRobotUp(List<Image> robotUp) {
		this.robotUp = robotUp;
	}

	public List<Image> getRobotDown() {
		return robotDown;
	}

	public void setRobotDown(List<Image> robotDown) {
		this.robotDown = robotDown;
	}

	public List<Image> getRobotLeft() {
		return robotLeft;
	}

	public void setRobotLeft(List<Image> robotLeft) {
		this.robotLeft = robotLeft;
	}

	public List<Image> getRobotRight() {
		return robotRight;
	}

	public void setRobotRight(List<Image> robotRight) {
		this.robotRight = robotRight;
	}

	public List<Rectangle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(List<Rectangle> obstacles) {
		this.obstacles = obstacles;
	}

	public List<Rectangle> getTables() {
		return tables;
	}

	public void setTables(List<Rectangle> tables) {
		this.tables = tables;
	}

	public List<Circle> getTablesFamily() {
		return tablesFamily;
	}

	public void setTablesFamily(List<Circle> tablesFamily) {
		this.tablesFamily = tablesFamily;
	}
}
