package orm;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Query;

@Entity
@Table(name="SCORING_MODEL")
public class ScoringModel  {

	@Id
	private Integer id;
	
	private String notes;
	
	@Column(name="ARTICLES_NORMALIZED")
	private boolean articlesNormalized;
	
	@Column(name="NO_STOP_WORDS")
	private boolean noStopWords;

	@ManyToMany
	@JoinTable(name="SCORING_MODEL_ARTICLE_SOURCE", joinColumns={@JoinColumn(name="SCORING_MODEL_ID")},
	inverseJoinColumns={@JoinColumn(name="ARTICLE_SOURCE_ID")})
	private Set<ArticleSource> articleSourceSet;
		
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

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean getArticlesNormalized() {
		return articlesNormalized;
	}

	public void setArticlesNormalized(boolean articlesNormalized) {
		this.articlesNormalized = articlesNormalized;
	}
	
	public boolean getNoStopWords() {
		return noStopWords;
	}

	public void setNoStopWords(boolean noStopWords) {
		this.noStopWords = noStopWords;
	}
	
	public Set<ArticleSource> getArticleSourceSet() {
		return articleSourceSet;
	}

	public void setArticleSourceSet(Set<ArticleSource> articleSourceSet) {
		this.articleSourceSet = articleSourceSet;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(Constants.toStringDelimeter);
		
		if (articleSourceSet.size() > 0) {
			for (ArticleSource as : articleSourceSet) {
				builder.append(as.getId()).append(",");
			}
			builder = new StringBuilder(builder.substring(0, builder.length()-1));
		} else {
			builder.append("no article sources");
		}
		
		builder.append(Constants.toStringDelimeter);
		
		builder.append("\"").append(notes).append("\"").append(Constants.toStringDelimeter);
		builder.append(articlesNormalized).append(Constants.toStringDelimeter);
		builder.append(noStopWords);

		return builder.toString();
	}
	
	public static ScoringModel getScoringModel(int scoringModelId) {
		Query query = SessionManager.createQuery("from ScoringModel where id = :id");
		query.setInteger("id", scoringModelId);
		return (ScoringModel)query.list().get(0);
	}
	
	public static Integer[] getArticleIds(int scoringModelId) throws HibernateException, SQLException {
		ResultSet rs = SessionManager.resultSetQuery("select article_id_array from scoring_model_article where scoring_model_id = " + scoringModelId);
		
		Integer[] result = null;
		if (rs.next()) {
			Array sqlArray = rs.getArray(1);
			result = (Integer[])sqlArray.getArray();
		}

		return result;
	}
}
