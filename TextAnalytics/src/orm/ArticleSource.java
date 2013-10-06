package orm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="ARTICLE_SOURCE")
public class ArticleSource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	private String description;
	
	public ArticleSource() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return id + Constants.toStringDelimeter + description;
	}
}
