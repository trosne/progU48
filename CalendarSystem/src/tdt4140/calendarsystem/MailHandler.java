///*
//* Description:
//*      Class to handle emails
//*/
//
//package tdt4140.calendarsystem;
//
//import com.sun.mail.smtp.SMTPTransport;
//import java.security.Security;
//import java.util.Date;
//import java.util.Properties;
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.faces.context.FacesContext;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
///**
//*
//* @author Patrik Fridberg Bakken
//*/
//public class MailHandler {
//
//    private String recipient;
//    private String sender;
//    private String host;
//    private String username;
//    private String password;
//    private String subject;
//    private String content;
//    private String[] files;
//
//    /*
//     * Constructor with default values
//     */
//    public MailHandler() throws Exception {
//        // Recipient's email ID needs to be mentioned.
//        //recipient = "bakken.patrik@gmail.com";
//
//        // Sender's email ID needs to be mentioned
//        sender = "tdt4140.48@gmail.com";
//
//        // Assuming you are sending email from this host
//        host = "smtp.gmail.com";
//
//        // Username and password
//        username = "tdt4140.48";
//        password = "felles2014";
//    }
//
//    public String getRecipient() {
//        return recipient;
//    }
//
//    public void setRecipient(String recipient) {
//        this.recipient = recipient;
//    }
//
//    public String getSender() {
//        return sender;
//    }
//
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String[] getFiles() {
//        return files;
//    }
//
//    public void setFiles(String[] files) {
//        this.files = files;
//    }
//
//    public void sendMail() {
//        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtps.host", host);
//        properties.setProperty("mail.user", username); // if needed
//        properties.setProperty("mail.password", password); // if needed
//        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
//        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
//        properties.setProperty("mail.smtp.port", "465");
//        properties.setProperty("mail.smtp.socketFactory.port", "465");
//        properties.setProperty("mail.smtps.auth", "true");
//
//        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(sender));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(recipient));
//
//            // Set Subject: header field
//            message.setSubject(subject);
//
//            // Set the email message text.
//            MimeBodyPart messagePart = new MimeBodyPart();
//            messagePart.setContent(content, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messagePart);
//
//            // Set the email attachment files
//            if (files != null) {
//                for (int i = 0; i < files.length; i++) {
//                    MimeBodyPart attachmentPart = new MimeBodyPart();
//                    String  realpath = ((javax.servlet.http.HttpServletRequest)(FacesContext.getCurrentInstance().getExternalContext().getRequest())).getRealPath(files[i]);
//                    FileDataSource fileDataSource = new FileDataSource(realpath) {
//                        @Override
//                        public String getContentType() {
//                            return "application/octet-stream";
//                        }
//                    };
//
//                    attachmentPart.setDataHandler(new DataHandler(fileDataSource));
//                    attachmentPart.setFileName(fileDataSource.getName());
//
//                    multipart.addBodyPart(attachmentPart);
//                }
//            }
//            message.setContent(multipart);
//            message.setSentDate(new Date());
//
//            // Send message
//            SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
//
//            t.connect("smtp.gmail.com", username, password);
//            t.sendMessage(message, message.getAllRecipients());
//            t.close();
//            //Transport.send(message);
//            System.out.println("Sent message successfully....");
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }
//}
