package com.ityi.web.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/***
 *      ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │  ─┬┘       └┬─  │
 *   │                 │
 *   │       ─┴─       │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
public class MailUtil {
    public static void send(String to,String subject,String content) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host","smtp.163.com");//设置主机地址
        props.setProperty("mail.smtp.auth","true");//认证


        //2.产生一个用于邮件发送的Session对象
        Session session = Session.getInstance(props);

        //3.产生一个邮件的消息对象
        MimeMessage message = new MimeMessage(session);

        //4.设置消息的发送者
        message.setFrom(new InternetAddress("ityigu@163.com"));

        //5.设置消息接收者
        //TO 直接发送 cc抄送 bcc密送
        message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(to));

        //6.设置主题
        message.setSubject(subject);

        //7.设置正文
        message.setText(content);

        //8.准备发送，得到火箭
        Transport transport = session.getTransport("smtp");
        //9.设置火箭的发射目标
        transport.connect("smtp.163.com","ityigu@163.com","hzyhzy1");

        //10.发送
        transport.sendMessage(message,message.getAllRecipients());

        //11.关闭
        transport.close();
    }
}
