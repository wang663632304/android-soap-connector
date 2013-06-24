package pl.aaugustyniak.stateconnectorclient.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class Server implements Serializable {

	private int id;
	private int reportable;
	private String displayName;
	private String url;
	private String apiKey;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the reportable
	 */
	public int getReportable() {
		return reportable;
	}

	/**
	 * @param reportable
	 *            the reportable to set
	 */
	public void setReportable(int reportable) {
		this.reportable = reportable;
	}
}
