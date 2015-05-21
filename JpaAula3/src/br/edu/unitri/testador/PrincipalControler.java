package br.edu.unitri.testador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import javax.persistence.EntityManager;

import br.edu.unitri.testador.FXDialog.Type;
import br.edu.unitri.util.JpaUtil;

public class PrincipalControler implements Initializable {

	@FXML private MenuItem menuItemAutoresClick;
	@FXML private MenuItem menuItemLivrosClick;
	@FXML private MenuItem menuItemDepartamentoClick;
	@FXML private MenuItem menuItemFuncionarioClick;
	@FXML private MenuItem menuItemPessoaClick;
	@FXML private MenuItem menuItemEstadoClick;
	@FXML private MenuItem menuItemGovernadorClick;
	@FXML private MenuItem menuItemAutorLivroClick;
	@FXML private MenuItem menuItemAboutClick;

	@FXML
	void menuItemFecharClick(ActionEvent event) {
		boolean ok = new FXDialog(Type.CONFIRM,"Tem certeza que deseja encerrar a aplica��o?").showDialog();
		if (ok) {
			JpaUtil.closeManager(JpaUtil.getManager());
			System.exit(0);
		}
	}

	@FXML
	void menuItemAutoresClick(ActionEvent event) {
		Stage telaAutor = new Stage();
		try {
			new FormFX<AutorFX>("Autor.fxml", telaAutor, "Opera��es com Autores", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemLivrosClick(ActionEvent event) {
		Stage telaLivros = new Stage();
		try {
			new FormFX<LivroFX>("Livro.fxml", telaLivros, "Opera��es com Livros", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemDepartamentoClick(ActionEvent event) {
		Stage telaDepartamento = new Stage();
		try {
			new FormFX<DepartamentoFX>("Departamento.fxml", telaDepartamento, "Opera��es com Departamento", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemFuncionarioClick(ActionEvent event) {
		Stage telaFuncionario = new Stage();
		try {
			new FormFX<FuncionarioFX>("Funcionario.fxml", telaFuncionario, "Opera��es com Funcion�rio", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemPessoaClick(ActionEvent event) {
		Stage telaPessoa = new Stage();
		try {
			new FormFX<PessoaFX>("Pessoa.fxml", telaPessoa, "Opera��es com Pessoa", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemEstadoClick(ActionEvent event) {
		Stage telaEstado = new Stage();
		try {
			new FormFX<EstadoFX>("Estado.fxml", telaEstado, "Opera��es com Estado", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemGovernadorClick(ActionEvent event) {
		Stage telaGovernador = new Stage();
		try {
			new FormFX<GovernadorFX>("Governador.fxml", telaGovernador, "Opera��es com Governador", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemAboutClick(ActionEvent event) {
		Stage telaAbout = new Stage();
		try {
			new FormFX<AboutFX>("About.fxml", telaAbout, "Sobre o Desenvolvedor", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@FXML
	void menuItemAutorLivroClick(ActionEvent event) {
		Stage telaRelacao = new Stage();
		try {
			new FormFX<LivrosAutoresFX>("LivroAutor.fxml", telaRelacao, "Opera��es Livros/Autores", false);
		} catch (Exception e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		EntityManager manager = JpaUtil.getManager();
		manager.clear();
		
	}
}
