package controller.stockPrices.update;

import orm.StockData;

public class AddOrReplaceInfo {
	public enum AddOrReplace {
		onlyAddNew,
		replaceAll
	}
	
	public final StockData newestInDatabase;
	
	public final AddOrReplace addOrReplace;

	public AddOrReplaceInfo(StockData newestInDatabase,
			AddOrReplace updateOrReplace) {
		this.newestInDatabase = newestInDatabase;
		this.addOrReplace = updateOrReplace;
	}
}
