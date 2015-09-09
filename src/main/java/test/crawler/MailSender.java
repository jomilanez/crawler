package test.crawler;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	// Recipient's email ID needs to be mentioned.
		String to = "josiane.milanez@gmail.com";

		private Session session;

		public MailSender() {
			Properties props = System.getProperties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.store.protocol", "pop3");
			props.put("mail.transport.protocol", "smtp");

			session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("testforcrawling", "crawtest123");
				}
			});
		}

		public void sendMail(String title, Offer offer) {

			if (session != null) {

				try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(to));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					message.setSubject(title);
					message.setText(createMessage(offer));
					Transport.send(message);
				} catch (MessagingException mex) {
					mex.printStackTrace();
				}
			}

		}
		
		private String createMessage(Offer offer) {
			StringBuilder sb = new StringBuilder();
			sb.append("title: ").append(offer.getTitle()).append("\n");
			sb.append("price: ").append(offer.getPrice()).append("\n");
			sb.append("local: ").append(offer.getLocal()).append("\n");
			sb.append("description: ").append(offer.getDescription()).append("\n");
			sb.append("url: ").append(offer.getUrl()).append("\n");
			return sb.toString();
		}

}
