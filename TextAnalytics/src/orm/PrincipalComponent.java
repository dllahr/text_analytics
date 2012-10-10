package orm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PRINCIPAL_COMPONENT")
public class PrincipalComponent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="EIGENVALUE_ID")
	private Eigenvalue eigenvalue;
	
	@Id
	@ManyToOne
	@JoinColumn(name="STEM_ID")
	private Stem stem;
	
	private Double value;
	
	public PrincipalComponent() {
		
	}

	public Eigenvalue getEigenvalue() {
		return eigenvalue;
	}

	public void setEigenvalue(Eigenvalue eigenvalue) {
		this.eigenvalue = eigenvalue;
	}

	public Stem getStem() {
		return stem;
	}

	public void setStem(Stem stem) {
		this.stem = stem;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
