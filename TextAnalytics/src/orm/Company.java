package orm;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.Query;

@Entity
public class Company {

	@Id
	private Integer id;
	
	private String name;
	
	@Column(name="STOCK_SYMBOL")
	private String stockSymbol;
	
	@ManyToMany
	@JoinTable(name="COMPANY_SCORING_MODEL", joinColumns={@JoinColumn(name="COMPANY_ID")}, 
	inverseJoinColumns={@JoinColumn(name="SCORING_MODEL_ID")})
	private Set<ScoringModel> scoringModelSet;

	public Company() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	@Override
	public String toString() {
		return id + Constants.toStringDelimeter + name + Constants.toStringDelimeter + stockSymbol;
	}

	public Set<ScoringModel> getScoringModelSet() {
		return scoringModelSet;
	}

	public void setScoringModelSet(Set<ScoringModel> scoringModelSet) {
		this.scoringModelSet = scoringModelSet;
	}
	
	public static Company findById(int companyId) {
		Query query = SessionManager.createQuery("from Company where id = :id");
		query.setInteger("id", companyId);
		
		@SuppressWarnings("rawtypes")
		List list = query.list();
		
		if (list.size() > 0) {
			return (Company)list.get(0);
		} else {
			return null;
		}
	}
}
