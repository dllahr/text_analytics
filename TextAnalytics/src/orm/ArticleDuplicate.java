package orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ARTICLE_DUPLICATE")
public class ArticleDuplicate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ARTICLE_ID_ORIG")
	private Integer articleIdOrig;
	
	@Id
	@Column(name="ARTICLE_ID_DUP")
	private Integer articleIdDuplicate;
	
	public ArticleDuplicate() {
	}

	public Integer getArticleIdOrig() {
		return articleIdOrig;
	}

	public void setArticleIdOrig(Integer articleIdOrig) {
		this.articleIdOrig = articleIdOrig;
	}

	public Integer getArticleIdDuplicate() {
		return articleIdDuplicate;
	}

	public void setArticleIdDuplicate(Integer articleIdDuplicate) {
		this.articleIdDuplicate = articleIdDuplicate;
	}

	@Override
	public String toString() {
		return articleIdOrig + Constants.toStringDelimeter + articleIdDuplicate;
	}
}
