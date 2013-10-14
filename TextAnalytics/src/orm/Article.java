package orm;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.Query;

import controller.util.Utilities;

@Entity
public class Article {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articleIdSeq")
	@SequenceGenerator(name = "articleIdSeq", sequenceName = "article_id_seq", allocationSize = 1)
	@Id
	private Integer id;
	
	@Column(name="ARTICLE_SOURCE_ID")
	private Integer articleSourceId;
	
	private String filename;
	
	@Column(name="PUBLISH_DATE")
	private Date publishDate;
	
	@Column(name="DAY_INDEX")
	private Integer dayIndex;
	
	@Column(name="START_LINE_NUM")
	private Integer startLineNum;
	
	@Column(name="ADDITIONAL_IDENTIFIER")
	private String additionalIdentifier;
	
	public Article() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getArticleSourceId() {
		return articleSourceId;
	}

	public void setArticleSourceId(Integer articleSourceId) {
		this.articleSourceId = articleSourceId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(Integer dayIndex) {
		this.dayIndex = dayIndex;
	}
	
	public void setDayIndex(Date date) {
		this.dayIndex = Utilities.calculateDayIndex(date);
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getStartLineNum() {
		return startLineNum;
	}

	public void setStartLineNum(Integer startLineNum) {
		this.startLineNum = startLineNum;
	}

	public String getAdditionalIdentifier() {
		return additionalIdentifier;
	}

	public void setAdditionalIdentifier(String additionalIdentifier) {
		this.additionalIdentifier = additionalIdentifier;
	}

	/**
	 * 
	 * @param minArticleDate required
	 * @param maxArticleDate optional / can be null
	 * @param articleSourceId
	 * @param excludeArticlePcValueArticles exclude articles that already have entries in article_pc_value
	 * @param excludeDuplicates exclude articles marked as duplicates in article_duplicate
	 * @return
	 */
	public static List<Integer> getArticleIdsForMinDateAndArticleSource(Date minArticleDate, 
			Date maxArticleDate, int articleSourceId, boolean excludeArticlePcValueArticles, boolean excludeDuplicates) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("select id from Article where articleSourceId = :articleSourceId and publishDate >= :minPublishDate");
		
		if (maxArticleDate != null) {
			builder.append(" and publishDate <= :maxPublishDate");
		}
		if (excludeArticlePcValueArticles) {
			builder.append(" and id not in (select article.id from ArticlePcValue)");
		}
		if (excludeDuplicates) {
			builder.append(" and id not in (select articleIdDuplicate from ArticleDuplicate)");
		}
	
		Query query = SessionManager.createQuery(builder.toString());
		query.setInteger("articleSourceId", articleSourceId);
		query.setDate("minPublishDate", minArticleDate);
		
		if (maxArticleDate != null) {
			query.setDate("maxPublishDate", maxArticleDate);
		}
		
		return Utilities.convertGenericList(query.list());
	}
	
	public static List<Article> getArticlesOrderById(int articleSourceId) {
		Query query = SessionManager.createQuery("from Article where articleSourceId = :asId order by id");
		query.setParameter("asId", articleSourceId);
		
		return Utilities.convertGenericList(query.list());
	}
}
