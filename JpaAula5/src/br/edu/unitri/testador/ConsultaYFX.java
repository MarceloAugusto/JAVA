/**
 * 
 */
package br.edu.unitri.testador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import br.edu.unitri.DTO.Consultas.ConsultaLetraY;
import br.edu.unitri.controler.ExerciciosControler;
import br.edu.unitri.testador.FXDialog.Type;
import br.edu.unitri.util.GenericTable;

/**
 * @author marcos.fernando
 *
 */
public class ConsultaYFX implements Initializable {

	private ObservableList<ConsultaLetraY> dados = FXCollections.observableArrayList();
	private ExerciciosControler exerciciosCtr = new ExerciciosControler();

	@FXML private Tab tabConsulta;
	@FXML private TableView<ConsultaLetraY> tbDados;
	@FXML private ToggleGroup buscarPor;
	@FXML private RadioButton rbNumLocal;
	@FXML private TextField txtBuscar;
	@FXML private RadioButton rbNumproj;
	@FXML private TabPane tabTela;
	@FXML private Button btnBuscar;

	@FXML
	void btnBuscarClick(ActionEvent event) throws SQLException {
	   	if (isValidConsulta()) {
	   		String qry = "select p.descProjeto, p.numProjeto, l.descLocal, l.nomLocal"
					+ " from tbProjeto p join tbLocal l on l.idLocal = p.local_id";
			String where ="";
			if (rbNumLocal.isSelected()) {
				where = " where l.descLocal like  '%"+ txtBuscar.getText() + "%'";
			}
			if (rbNumproj.isSelected()) {
				where = " where p.descProjeto like  '%"+ txtBuscar.getText() + "%'";
			}
			tbDados.getItems().clear();			
			dados.addAll(exerciciosCtr.findLetraY(qry,where));
			tbDados.setItems(dados);
		} else {
			new FXDialog(Type.WARNING, "Escolha pelo menos uma das op��es para consulta!").showDialog();
			popularDados();
			txtBuscar.requestFocus();
		}
	}

	private boolean isValidConsulta() {
		boolean ok = rbNumproj.isSelected() || rbNumLocal.isSelected();
		if (ok) {
			ok = !txtBuscar.getText().isEmpty();
		}
		return ok;
	}

	private void popularDados() {
		tbDados.getItems().clear();
		dados.clear();
		try {
			dados.addAll(exerciciosCtr.findLetraY());
		} catch (SQLException e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
		tbDados.setItems(dados);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbDados.getColumns().addAll(new GenericTable<ConsultaLetraY>().tableColunas(ConsultaLetraY.class));
		popularDados();
	}

}
