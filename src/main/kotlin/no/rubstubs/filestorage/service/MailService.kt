package no.rubstubs.filestorage.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import javax.mail.*
import javax.mail.internet.*

@Service
class MailService(
        @Autowired private val env: Environment
) {
    val msg: Message = setupMail()

    /**
     * Mail connections config.
     * @return Message
     */
    private fun setupMail(): Message {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        /**
         * Mail login auth. Gets credentials from environment variables
         */
        fun getPasswordAuthentication(): Authenticator {
            return object : Authenticator() {
                override fun getPasswordAuthentication() : PasswordAuthentication {
                    return PasswordAuthentication("${env["MAIL_ADDRESS"]}", "${env["MAIL_PASSWORD"]}")
                }
            }
        }

        val session: Session = Session.getInstance(props, getPasswordAuthentication())
        return MimeMessage(session)
    }

    /**
     * Sends a happy mail containing download link
     */
    @Throws(AddressException::class, MessagingException::class, IOException::class)
    fun sendMail(receiver: String, url: String) {
        println("Sending mail to $receiver")
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver))
        msg.subject = "Zippy, The Werewolf presents your download link"
        msg.sentDate = Date()
        val messageBodyPart = MimeBodyPart()
        messageBodyPart.setContent("<h1>*Hoooooowl*</h1><br><p>here's your link: $url</p>", "text/html")
        val multipart: Multipart = MimeMultipart()
        multipart.addBodyPart(messageBodyPart)
        msg.setContent(multipart)
        Transport.send(msg)
    }

    /**
     * Sends sad apology mail.
     */
    fun sendApologyMail(receiver: String) {
        println("Sending apology mail to $receiver")
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver))
        msg.subject = "Zippy, The Werewolf apologizes"
        msg.sentDate = Date()
        val messageBodyPart = MimeBodyPart()
        messageBodyPart.setContent("<h1>*Hoooooowl*</h1><br><p>Something went wrong. Sorry about that.</p>", "text/html")
        val multipart: Multipart = MimeMultipart()
        multipart.addBodyPart(messageBodyPart)
        msg.setContent(multipart)
        Transport.send(msg)
    }
}