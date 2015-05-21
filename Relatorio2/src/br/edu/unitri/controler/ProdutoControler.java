/**
 * 
 */
package br.edu.unitri.controler;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.unitri.model.Produto;
import br.edu.unitri.util.JpaUtil;

/**
 * @author MARCOS FERNANDO
 *
 */
public class ProdutoControler implements CRUD<Produto, Integer> {

	private EntityManager manager = JpaUtil.getManager();

	@Override
	public Produto save(Produto t) throws SQLException {
		manager.getTransaction().begin();
		manager.persist(t);
		manager.getTransaction().commit();
		return t;
	}

	@Override
	public boolean delete(Produto t) throws SQLException {
		manager.getTransaction().begin();
		manager.remove(t);
		manager.getTransaction().commit();
		return true;
	}

	@Override
	public boolean update(Produto t, Integer i) throws SQLException {
		boolean ok = false;
		Produto produto = manager.find(Produto.class, (long) i);
		if (produto != null) {
			ok = true;
			produto.setCategoria(t.getCategoria());
			produto.setNomeProduto(t.getNomeProduto());
			produto.setValorProd(t.getValorProd());
			produto.setQuantidade(t.getQuantidade());
			produto.setImagem(t.getImagem());

			manager.getTransaction().begin();
			manager.persist(produto);
			manager.getTransaction().commit();
		}
		return ok;
	}

	@Override
	public Produto getById(Integer i) throws SQLException {
		return manager.find(Produto.class, (long) i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> findAll() throws SQLException {
		return  manager.createQuery("from Produto").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> findAll(String qry, String parametros)
			throws SQLException {
		if (!parametros.isEmpty()) {
			qry = qry.concat(parametros);
		}
		return manager.createNativeQuery(qry ,Produto.class).getResultList();
	}

}
