package orm;

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
public class Stem {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stemIdSeq")
	@SequenceGenerator(name = "stemIdSeq", sequenceName = "stem_id_seq", allocationSize = 1)
	@Id
	private Integer id;
	
	@Column(name="ARTICLE_SOURCE_ID")
	private int articleSourceId;
	
	private String text;
	
	@Column(name="IS_STOP")
	private boolean isStop;
	
	public Stem() {
		isStop = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getArticleSourceId() {
		return articleSourceId;
	}

	public void setArticleSourceId(int articleSourceId) {
		this.articleSourceId = articleSourceId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
	
	public static List<Stem> getStemsOrderedById(int articleSourceId) {
		Query query = SessionManager.createQuery("from Stem where articleSourceId = :asId order by id");
		query.setParameter("asId", articleSourceId);
		
		return Utilities.convertGenericList(query.list());
	}
}
