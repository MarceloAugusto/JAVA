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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import br.edu.unitri.controler.PessoaControler;
import br.edu.unitri.model.Pessoa;
import br.edu.unitri.testador.FXDialog.Type;
import br.edu.unitri.util.GenericTable;

/**
 * @author marcos.fernando
 *
 */
public class PessoaFX implements Initializable {
	
	@FXML private Tab tabConsulta;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private Tab tabCadastro;
    @FXML private TextField txtBuscar;
    @FXML private RadioButton rbEmail;
    @FXML private TableView<Pessoa> tbPessoas;
    @FXML private Button btnIncluir;
    @FXML private RadioButton rbNome;
    @FXML private ToggleGroup buscarPor;
    @FXML private DatePicker dtNascimento;
    @FXML private Button btnExcluir;
    @FXML private TabPane tabTela;
    @FXML private Button btnBuscar;
    @FXML private Button btnNovo;

	private ObservableList<Pessoa> dados = FXCollections.observableArrayList();
	private int Operacao;
	private PessoaControler pessoaCtr = new PessoaControler();
	private Pessoa pessoa;

	@FXML
    void btnBuscarClick(ActionEvent event) throws SQLException {
	   	if (isValidConsulta()) {
			dados.clear();
			if (rbNome.isSelected()) {
			   dados.addAll(pessoaCtr.findAll("select p.* from tbPessoa p"," where p.nome like  '%"+ txtBuscar.getText() + "%'"));			
			} else if (rbEmail.isSelected()) {
				dados.addAll(pessoaCtr.findAll("select p.* from tbPessoa p"," where p.email like  '%"+ txtBuscar.getText() + "%'"));
			}
			tbPessoas.setItems(dados);
		} else {
			new FXDialog(Type.WARNING, "Escolha pelo menos uma das op��es para consulta!").showDialog();
			txtBuscar.requestFocus();
		}
    }

    private boolean isValidConsulta() {
		boolean ok = rbNome.isSelected() || rbEmail.isSelected();
		if (ok) {
			ok = !txtBuscar.getText().isEmpty();
		}
		return ok;
	}

	@FXML
    void btnIncluirClick(ActionEvent event) {
		if (isValidaTela()) {
			Pessoa pessoa = new Pessoa(txtNome.getText(), txtEmail.getText(), dtNascimento.getValue());
			switch (getOperacao()) {
			case 0:
				try {
					if (pessoaCtr.save(pessoa) != null) {
						new FXDialog(Type.INFO, "Opera��o realizada com sucesso!").showDialog();
					}
				} catch (SQLException e) {
					new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
				}
				;
				popularDados();
				break;
			case 1:
				try {
					if (pessoaCtr.update(pessoa,(int) getPessoa().getId())) {
						new FXDialog(Type.INFO,"Registro atualizado com sucesso!").showDialog();
					}
				} catch (SQLException e) {
					new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
				}
				popularDados();
				break;
			}
		}
    }

    private boolean isValidaTela() {
		boolean ok = true;
		if (txtNome.getText().isEmpty()) {
			new FXDialog(Type.WARNING, "Favor preencher o nome da Pessoa!").showDialog();			
			txtNome.requestFocus();
			ok = false;			
		}
		if (ok){
			if (txtEmail.getText().isEmpty()) {
				new FXDialog(Type.WARNING, "Favor preencher o email da Pessoa!").showDialog();
				txtEmail.requestFocus();
				ok = false;			
			}
		}
		if (ok){
			if (dtNascimento.getValue() == null) {
				new FXDialog(Type.WARNING, "Favor preencher a data de nascimento da Pessoa!").showDialog();
				dtNascimento.requestFocus();
				ok = false;			
			}
		}
		return ok;		 
	}

	@FXML
    void btnNovoClick(ActionEvent event) {
		setOperacao(0);
		txtNome.setText("");
		txtEmail.setText("");
		dtNascimento.setValue(null);
		txtNome.requestFocus();
    }

    @FXML
    void btnExcluirClick(ActionEvent event) {
		boolean ok = new FXDialog(Type.CONFIRM,
				"Tem certeza que deseja excluir este registro?").showDialog();
		if ((ok) && (getOperacao() == 1)) {
			try {
				ok = pessoaCtr.delete(getPessoa());
			} catch (SQLException e) {
				new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
			}
			if (ok) {
				new FXDialog(Type.INFO, "Opera��o realizada com sucesso!").showDialog();
				popularDados();
				btnNovoClick(event);
			} else {
				new FXDialog(Type.WARNING,"Existem rela��es com outras entidades!").showDialog();
			}
		} else {
			new FXDialog(Type.WARNING,"N�o foi selecionado nenhuma Pessoa!").showDialog(); 
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbPessoas.getColumns().addAll(new GenericTable<Pessoa>().tableColunas(Pessoa.class));
		popularDados();
		tbPessoas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					tabTela.getSelectionModel().select(1);
					setOperacao(1);
					setPessoa(tbPessoas.getSelectionModel().getSelectedItem());
					txtEmail.setText(tbPessoas.getSelectionModel().getSelectedItem().getEmail());
					txtNome.setText(tbPessoas.getSelectionModel().getSelectedItem().getNome());
					dtNascimento.setValue(tbPessoas.getSelectionModel().getSelectedItem().getDtNascimento());
				}
			}
		});
	}
	
	private void popularDados() {
		dados.clear();
		tbPessoas.getItems().clear();
		try {
			dados.addAll(pessoaCtr.findAll());
		} catch (SQLException e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
		tbPessoas.setItems(dados);
	}

	public int getOperacao() {
		return Operacao;
	}

	public void setOperacao(int operacao) {
		Operacao = operacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
}
