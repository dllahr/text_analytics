package orm;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="SCORING_MODEL")
public class ScoringModel  {

	@Id
	private Integer id;
	
	private String notes;

	@ManyToMany
	@JoinTable(name="COMPANY_SCORING_MODEL", joinColumns={@JoinColumn(name="SCORING_MODEL_ID")}, 
	inverseJoinColumns={@JoinColumn(name="COMPANY_ID")})
	private Set<Company> companySet;
		
	public ScoringModel() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setName(String notes) {
		this.notes = notes;
	}

	public Set<Company> getCompanySet() {
		return companySet;
	}

	public void setCompanySet(Set<Company> companySet) {
		this.companySet = companySet;
	}
	
	public Company getCompanyById(int companyId) {
		Company result = null;
		
		for (Company company : companySet) {
			if (companyId == company.getId()) {
				result = company;
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(Constants.toStringDelimeter).append("companies: ");
		for (Company company : companySet) {
			builder.append(company.getId()).append(Constants.toStringDelimeter);
		}
		builder.append(notes);

		return builder.toString();
	}
}
