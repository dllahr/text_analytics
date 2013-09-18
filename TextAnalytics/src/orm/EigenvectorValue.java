package orm;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Query;

import controller.util.Utilities;

@Entity
@Table(name="EIGENVECTOR_VALUE")
public class EigenvectorValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="EIGENVALUE_ID")
	private Eigenvalue eigenvalue;
	
	@Id
	@ManyToOne
	@JoinColumn(name="ARTICLE_ID")
	private Article article;
	
	private Double value;
	
	public EigenvectorValue() {
	}

	public EigenvectorValue(Eigenvalue eigenvalue, Article article, Double value) {
		this.eigenvalue = eigenvalue;
		this.article = article;
		this.value = value;
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalue) {
		this.eigenvalue = eigenvalue;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public static List<EigenvectorValue> getEigenvectorValuesOrderByArticleDayIndexAndEigId(ScoringModel scoringModel) {
		final String scoringModelParam = "scoringModelParam";
		Query query = SessionManager.createQuery("from EigenvectorValue where eigenvalue.scoringModel = :" + scoringModelParam 
				+ " order by article.dayIndex asc, eigenvalue.id");
		query.setParameter(scoringModelParam, scoringModel);
		
		return Utilities.convertGenericList(query.list());
	}
}
