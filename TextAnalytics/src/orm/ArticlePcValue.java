package orm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ARTICLE_PC_VALUE")
public class ArticlePcValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="ARTICLE_ID")
	private Article article;
	
	@Id
	@ManyToOne
	@JoinColumn(name="EIGENVALUE_ID")
	private Eigenvalue eigenvalue;
	
	private Double value;
	
	public ArticlePcValue() {
	}

	public ArticlePcValue(Article article, Eigenvalue eigenvalue, Double value) {
		this.article = article;
		this.eigenvalue = eigenvalue;
		this.value = value;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalue) {
		this.eigenvalue = eigenvalue;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return article.getId() + Constants.toStringDelimeter + eigenvalue.getId() + Constants.toStringDelimeter + value;
	}
}
