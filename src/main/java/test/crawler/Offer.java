package test.crawler;

public class Offer {
	
	private String price;
	private String local;
	private String url;
	private String description;
	private String title;

	public Offer(String title, String price, String local, String url, String description) {
		this.title = title;
		this.price = price;
		this.local = local;
		this.url = url;
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public String getLocal() {
		return local;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}
	
}
