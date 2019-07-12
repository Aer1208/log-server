package com.tongbao.log.mail;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author xiaohaifang
 * @date 2019/7/10 11:35
 */
@Component("mailSenderServer")
@Slf4j
public class MailSenderServer {

    private final static String personal="风控系统";

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private MailConfig mailConfig;

    /**
     * 发送邮件
     *
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public void sendMail(String subject, String html) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(mailConfig.getEmailForm(), personal);
        messageHelper.setTo(mailConfig.getEmailTo().split(","));
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    /**
     * 开始发送邮箱
     * @param jsonObject
     */
    @Async
    public void sendMail(JSONObject jsonObject) {
        try {
            String subjet = "【异常通知】用户ID:" + jsonObject.get("userId")+ "," + "调用类型" + jsonObject.get("type") + "子类型" + jsonObject.get("subType");
            String html = getHtml(jsonObject);
            this.sendMail(subjet, html);
        }catch (Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 获取发送邮件类容
     * @param jsonObject
     * @return
     */
    private String getHtml(JSONObject jsonObject) {

        /**
         *         json.put("userId", entity.getUserId());
         *         json.put("serialNo", entity.getSerialNo());
         *         json.put("type", entity.getType());
         *         json.put("subType", entity.getSubType());
         *         json.put("url", entity.getUrl());
         *         json.put("result", entity.getUrl());
         *         json.put("msg", ex.getMessage());
         */
        String body = jsonObject.getString("msg");
        if (body != null) {
            body = body.replaceAll(System.getProperty("line.separator", "\r\n"),"<br/>");
        }
        StringBuffer htmlBuffer = new StringBuffer();
        htmlBuffer.append("<html lang=\"en\">\n" +
                "<head>\n" +
                "\t<meta charset=\"UTF-8\">\n" +
                "\t<title>1</title>\n" +
                "\t<style>\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-right:1px solid #000;border-bottom:1px solid #000\n" +
                "\t\t}\n" +
                "\t\ttr{\n" +
                "\t\t\ttext-align: left;\n" +
                "\t\t}\n" +
                "\t\tth, td {\n" +
                "\t\t\tword-wrap:break-word;\n" +
                "\t\t\tword-break:break-all;\n" +
                "\t\t\tborder-left:1px solid #000;border-top:1px solid #000\n" +
                "\t\t}\n" +
                "\t\ttd.title{\n" +
                "\t\t\twidth:10%;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body>")
               .append("<table style='border:1px solid #FFF'>")

                .append("<tr>")
                .append("<td class='title'>影响的用户id</td>")
                .append("<td>").append(jsonObject.get("userId")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>申请请求编号</td>")
                .append("<td>").append(jsonObject.getString("serialNo")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>请求类型</td>")
                .append("<td>").append(jsonObject.getString("type")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>请求子类型</td>")
                .append("<td>").append(jsonObject.getString("subType")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>请求url</td>")
                .append("<td>").append(jsonObject.getString("url")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>请求返回</td>")
                .append("<td>").append(jsonObject.getString("result")).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td class='title'>错误信息</td>")
                .append("<td>").append(body).append("</td>")
                .append("</tr>")
                .append("</table></body></html>");
        return htmlBuffer.toString();
    }
}
