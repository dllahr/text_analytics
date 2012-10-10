package tools;


public class LoadDataExperimental extends LoadData {
	private static final String csvDelimeter = 
			"^((\"(?:[^\"]|\"\")*\"|[^,]*)(,(\"(?:[^\"]|\"\")*\"|[^,]*))*)$";

	
	public LoadDataExperimental() {
		LoadData.csvDelimeter = LoadDataExperimental.csvDelimeter;
	}
}
