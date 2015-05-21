/**
 * 
 */
package br.edu.unitri.testador;

import br.edu.unitri.testador.FXDialog.Type;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Marcos
 *
 */
public class Principal extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			new FormFX<PrincipalControler>("TelaPrincipal.fxml", primaryStage, "Aplica��o principal - APP Aula 4", true);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		    e.printStackTrace();
		}	
	}
	
	@Override
	public void stop() throws Exception {		 	
		 Platform.exit();	
	};
	
	public static void main(String[] args) {
		launch(args);
	}

}
