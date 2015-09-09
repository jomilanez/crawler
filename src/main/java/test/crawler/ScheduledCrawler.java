package test.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@Component
public class ScheduledCrawler {

	private final static MailSender sender = new MailSender();
	private final static List<String> visitedPages = new ArrayList<String>();
	
    @Scheduled(fixedRate = 180000)
    public void crawlWebSite() {
    	try {
    		createCrawler("KINDERWAGEN", "http://www.ebay-kleinanzeigen.de/s-zu-verschenken/berlin/anzeige:angebote/kinderwagen/k0c192l3331r30");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	private static void createCrawler(String searchTitle, String url) throws Exception {
		Document doc = Jsoup.connect(url).get();
		Element searchResult = doc.getElementById("srchrslt-adtable");
		if (searchResult != null) {
			Elements results = searchResult.getElementsByClass("ad-title");
			for (Element element : results) {
				retrieveOffer(searchTitle, "http://www.ebay-kleinanzeigen.de/" + element.attr("href"));
			}
		} else {
			System.out.println("no results");
		}
	}

	private static void retrieveOffer(String searchTitle, String url) throws IOException {
		final String id = url.substring(url.lastIndexOf('/'));
		if (!visitedPages.contains(id)) {
			Document doc = Jsoup.connect(url).get();
			Element price = doc.getElementById("viewad-price");
			Element local = doc.getElementById("viewad-locality");
			Element description = doc.getElementById("viewad-description-text");
			Element title = doc.getElementById("viewad-title");  
			Offer offer = new Offer(title == null? null : title.html() , price == null? null : price.html(), local == null? null : local.html(), url == null? null : url, description == null? null : description.html());
			sender.sendMail(searchTitle, offer);
			visitedPages.add(id);
		} else {
			System.out.println("Already contains id " + id);
		}
	}

	
	private static void sendMessage(final String url) throws IOException {
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage(url);
			
			DomElement element = page.getElementById("viewad-contact-button-login-top");
			
			final HtmlPage page1 = element.click();
			
			final HtmlForm form = page1.getFirstByXPath("//form[@id='login-form']");

			HtmlElement button = form.getElementsByAttribute("button", "id", "login-submit").get(0);
			HtmlTextInput login = (HtmlTextInput) form.getElementsByAttribute("input", "id", "login-email").get(0);
			HtmlPasswordInput password = (HtmlPasswordInput) form
					.getElementsByAttribute("input", "id", "login-password").get(0);

			login.setValueAttribute("neumutter1@gmail.com");
			//set the password value
			final HtmlPage page2 = button.click();
			DomElement viewContactButton = page2.getElementById("viewad-contact-button");
			final HtmlPage page3 = viewContactButton.click();

			final HtmlForm sendMessageForm = page3.getFirstByXPath("//form[@id='viewad-contact-form']");

			HtmlTextArea message = (HtmlTextArea) sendMessageForm
					.getElementsByAttribute("textarea", "id", "viewad-contact-message").get(0);
			message.setText("gtestest  set set  set set ");
			HtmlElement sendMessageButton = sendMessageForm
					.getElementsByAttribute("button", "id", "viewad-contact-submit").get(0);
			sendMessageButton.click();
		}
	}
}
