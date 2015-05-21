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
import br.edu.unitri.controler.PessoaJuridicaControler;
import br.edu.unitri.model.PessoaJuridica;
import br.edu.unitri.testador.FXDialog.Type;
import br.edu.unitri.util.ConverterUtil;
import br.edu.unitri.util.GenericTable;

/**
 * @author marcos.fernando
 *
 */
public class PessoaJuridicaFX implements Initializable {

	@FXML private Tab tabConsulta;
	@FXML private TextField txtInscricao;
	@FXML private RadioButton rbCidade;
	@FXML private TextField txtNome;
	@FXML private TextField txtEmail;
	@FXML private Tab tabCadastro;
	@FXML private TextField txtBuscar;
	@FXML private RadioButton rbEmail;
	@FXML private TextField txtCNPJ;
	@FXML private RadioButton rbUF;
	@FXML private TableView<PessoaJuridica> tbPessoas;
	@FXML private Button btnIncluir;
	@FXML private RadioButton rbCNPJ;
	@FXML private RadioButton rbInscricao;
	@FXML private RadioButton rbNome;
	@FXML private ToggleGroup buscarPor;
	@FXML private DatePicker dtNascimento;
	@FXML private Button btnExcluir;
 	@FXML private TabPane tabTela;
	@FXML private Button btnBuscar;
	@FXML private Button btnNovo;
	
	private ObservableList<PessoaJuridica> dados = FXCollections.observableArrayList();
	private int Operacao;
	private PessoaJuridicaControler pessoaJuridicaCtr = new PessoaJuridicaControler();
	private PessoaJuridica pessoaJuridica;


	@FXML
	void btnBuscarClick(ActionEvent event) throws SQLException {
	   	if (isValidConsulta()) {
			dados.clear();
			if (rbNome.isSelected()) {
			   dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p "," where pf.id = p.id and p.nome like  '%"+ txtBuscar.getText() + "%'"));			
			} else if (rbEmail.isSelected()) {
				dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p"," where pf.id = p.id and p.email like  '%"+ txtBuscar.getText() + "%'"));
			} else if (rbUF.isSelected()) {
				dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p, tbEndereco e, Pessoa_Endereco pe",
						" Where pf.id = p.id and pe.endereco_id = e.id and pe.pessoa_id = p.id and e.estado like '%"+ txtBuscar.getText() + "%'")); 
			} else if (rbCNPJ.isSelected()) {
				dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p"," where pf.id = p.id and pf.cnpj like  '%"+ txtBuscar.getText() + "%'"));
			} else if (rbInscricao.isSelected()) {
				dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p"," where pf.id = p.id and pf.InscEstadual like '%"+ txtBuscar.getText() + "%'"));
			} else if (rbCidade.isSelected()) {
				dados.addAll(pessoaJuridicaCtr.findAll("select pf.* from tbPessoaJuridica pf, tbPessoa p, tbEndereco e, Pessoa_Endereco pe",
						  " where pf.id = p.id and pe.endereco_id = e.id and pe.pessoa_id = p.id and e.cidade like  '%"+ txtBuscar.getText() + "%'"));
			}
			tbPessoas.setItems(dados);
		} else {
			new FXDialog(Type.WARNING, "Escolha pelo menos uma das op��es para consulta!").showDialog();
			txtBuscar.requestFocus();
		}
	}

	private boolean isValidConsulta() {
		boolean ok = rbNome.isSelected() || rbEmail.isSelected() || rbCidade.isSelected() || rbUF.isSelected() || rbCNPJ.isSelected() || rbInscricao.isSelected();
		if (ok) {
			ok = !txtBuscar.getText().isEmpty();
		}
		return ok;
	}

	@FXML
	void btnIncluirClick(ActionEvent event) {
		if (isValidaTela()) {
			PessoaJuridica pessoaJuridica = new PessoaJuridica(txtNome.getText(), txtEmail.getText(), ConverterUtil.localDateToDate(dtNascimento.getValue()));
			pessoaJuridica.setCnpj(txtCNPJ.getText());
			pessoaJuridica.setInscEstadual(txtInscricao.getText());
			switch (getOperacao()) {
			case 0:
				try {
					if (pessoaJuridicaCtr.save(pessoaJuridica) != null) {
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
					if (pessoaJuridicaCtr.update(pessoaJuridica,(int) getPessoaJuridica().getId())) {
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
		if (ok){
			if (txtCNPJ.getText().isEmpty()) {
				new FXDialog(Type.WARNING, "Favor preencher o CNPJ da Pessoa!").showDialog();
				txtCNPJ.requestFocus();
				ok = false;			
			}
		}
		if (ok){
			if (txtInscricao.getText().isEmpty()) {
				new FXDialog(Type.WARNING, "Favor preencher a Inscri��o Estadual da Pessoa Juridica!").showDialog();
				txtInscricao.requestFocus();
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
		txtCNPJ.setText("");
		txtInscricao.setText("");
		txtNome.requestFocus();
	}

	@FXML
	void btnExcluirClick(ActionEvent event) {
		boolean ok = new FXDialog(Type.CONFIRM,
				"Tem certeza que deseja excluir este registro?").showDialog();
		if ((ok) && (getOperacao() == 1)) {
			try {
				ok = pessoaJuridicaCtr.delete(getPessoaJuridica());
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
			new FXDialog(Type.WARNING,"N�o foi selecionado nenhuma Pessoa Juridica!").showDialog(); 
		}
	}
	
	private void popularDados() {
		dados.clear();
		tbPessoas.getItems().clear();
		try {
			dados.addAll(pessoaJuridicaCtr.findAll());
		} catch (SQLException e) {
			new FXDialog(Type.ERROR, e.getCause().getMessage()).showDialog();
		}
		tbPessoas.setItems(dados);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tbPessoas.getColumns().addAll(new GenericTable<PessoaJuridica>().tableColunas(PessoaJuridica.class));
		popularDados();
		tbPessoas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					tabTela.getSelectionModel().select(1);
					setOperacao(1);
					setPessoaJuridica(tbPessoas.getSelectionModel().getSelectedItem());
					txtEmail.setText(tbPessoas.getSelectionModel().getSelectedItem().getEmail());
					txtNome.setText(tbPessoas.getSelectionModel().getSelectedItem().getNome());
					dtNascimento.setValue(ConverterUtil.utilDateToLocalDate(tbPessoas.getSelectionModel().getSelectedItem().getDtNascimento()));
					txtCNPJ.setText(tbPessoas.getSelectionModel().getSelectedItem().getCnpj());
					txtInscricao.setText(tbPessoas.getSelectionModel().getSelectedItem().getInscEstadual());					
				}
			}
		});
	}

	public int getOperacao() {
		return Operacao;
	}

	public void setOperacao(int operacao) {
		Operacao = operacao;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}
	
}
