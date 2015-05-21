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
import br.edu.unitri.DTO.Consultas.ConsultaLetraG;
import br.edu.unitri.controler.ExerciciosControler;
import br.edu.unitri.testador.FXDialog.Type;
import br.edu.unitri.util.GenericTable;

/**
 * @author marcos.fernando
 *
 */
public class ConsultaGFX implements Initializable {

	private ObservableList<ConsultaLetraG> dados = FXCollections.observableArrayList();
	private ExerciciosControler exerciciosCtr = new ExerciciosControler();

	@FXML private Tab tabConsulta;
	@FXML private TableView<ConsultaLetraG> tbDados;
	@FXML private RadioButton rbDescricao;
	@FXML private RadioButton rbProjeto;
	@FXML private ToggleGroup buscarPor;
	@FXML private TextField txtBuscar;
	@FXML private TabPane tabTela;
	@FXML private Button btnBuscar;

	@FXML
	void btnBuscarClick(ActionEvent event) throws SQLException {
	   	if (isValidConsulta()) {	
	   		String qry = "select d.descLocal, p.numProjeto from tbProjeto p join tbDepartamento d on d.idDepartamento = p.departamento_id";
	   		String where ="";
			if (rbProjeto.isSelected()) {
				where = " where d.descLocal like  '%"+ txtBuscar.getText() + "%'";
			}
			if (rbDescricao.isSelected()) {
				where = " where p.numProjeto like  '%"+ txtBuscar.getText() + "%'";
			}
			
			tbDados.getItems().clear();			
			dados.addAll(exerciciosCtr.findLetraG(qry,where));
			tbDados.setItems(dados);
		} else {
			new FXDialog(Type.WARNING, "Escolha pelo menos uma das op��es para consulta!").showDialog();
			popularDados();
			txtBuscar.requestFocus();
		}
	}

	private boolean isValidConsulta() {
		boolean ok = rbProjeto.isSelected() || rbDescricao.isSelected();
		if (ok) {
			ok = !txtBuscar.getText().isEmpty();
		}
		return ok;
	}

	private void popularDados() {
		tbDados.getItems().clear();
		dados.clear();
		try {
			dados.addAll(exerciciosCtr.findLetraG());
		} catch (SQLException e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
		tbDados.setItems(dados);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbDados.getColumns().addAll(new GenericTable<ConsultaLetraG>().tableColunas(ConsultaLetraG.class));
		popularDados();		
	}

}
