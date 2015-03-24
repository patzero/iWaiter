/* ******************************************************
 * Project iWaiter - Insta
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * ******************************************************/
package com.insta.iwaiter.application;

import com.insta.iwaiter.data.Data;
import com.insta.iwaiter.engine.Engine;
import com.insta.iwaiter.services.DataService;
import com.insta.iwaiter.services.ViewerService;
import com.insta.iwaiter.userinterface.Viewer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main  extends Application {

	private static Viewer viewer;
	private static Engine engine;
	private static DataService data;
	private static AnimationTimer timer;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		viewer = new Viewer();
		engine = new Engine();
		data = new Data();

		engine.bindData(data);
		engine.bindViewer(viewer);

		viewer.bindData(data);
		viewer.bindEngine(engine);
		
		Scene scene = new Scene(viewer.getPanel());
		scene.getStylesheets().add(getClass().getResource("/stylesheet/application.css").toExternalForm());
		
		stage.setScene(scene);
		stage.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				engine.startController();
			}
		});
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				engine.stop();
			}
		});
		stage.show();

		timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				scene.setRoot(((ViewerService)viewer).getPanel());
			}
		};
		timer.start();
	}
}
