package orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company  {

	@Id
	private Integer id;
	
	private String name;
	
	@Column(name="STOCK_SYMBOL")
	private String stockSymbol;
		
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
		return id + " " + name + " " + stockSymbol;
	}
	
	
}
