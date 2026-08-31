package testdatamanager.dps;

public class DpsExecutionData {

	private String crawler;
	private String catalogName;
	private String dataSources;
	private String dataIntegration;
	private String dataIntegrationJobName;
	private String dataLake;

	public String getCrawler() {
		return crawler;
	}

	public void setCrawler(String crawler) {
		this.crawler = crawler;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getDataSources() {
		return dataSources;
	}

	public void setDataSources(String dataSources) {
		this.dataSources = dataSources;
	}

	public String getDataIntegration() {
		return dataIntegration;
	}

	public void setDataIntegration(String dataIntegration) {
		this.dataIntegration = dataIntegration;
	}

	public String getDataIntegrationJobName() {
		return dataIntegrationJobName;
	}

	public void setDataIntegrationJobName(String dataIntegrationJobName) {
		this.dataIntegrationJobName = dataIntegrationJobName;
	}

	public String getDataLake() {
		return dataLake;
	}

	public void setDataLake(String dataLake) {
		this.dataLake = dataLake;
	}
}

