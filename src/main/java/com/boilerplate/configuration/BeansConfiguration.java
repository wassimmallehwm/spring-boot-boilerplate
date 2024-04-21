package com.boilerplate.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class BeansConfiguration {
    @Autowired
    private ExternalConfigs externalConfigs;

    @Bean(name = "datasource")
    public DataSource dataSource() {
        String url = externalConfigs.getDBUrl();
        String password = externalConfigs.getDBPassword();
        String username = externalConfigs.getDBUsername();
        String driverClassName = "org.postgresql.Driver";

        HikariDataSource datasource = DataSourceBuilder.create().driverClassName(driverClassName)
                .url(url).username(username).password(password).type(HikariDataSource.class).build();
        datasource.setConnectionTimeout(externalConfigs.getConnectionTimeout());
        datasource.setMinimumIdle(externalConfigs.getMinimumIdle());
        datasource.setMaximumPoolSize(externalConfigs.getMaximumPoolSize());
        datasource.setIdleTimeout(externalConfigs.getIdleTimeout());
        datasource.setMaxLifetime(externalConfigs.getMaxLifetime());
        datasource.setAutoCommit(externalConfigs.getAutoCommit());
        return datasource;
    }

    @Bean(name = "javaMailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(externalConfigs.getEmailUsername());
        mailSender.setHost(externalConfigs.getEmailHost());
        mailSender.setPassword(externalConfigs.getEmailPassword());
        mailSender.setProtocol(externalConfigs.getEmailProtocol());
        mailSender.setPort(externalConfigs.getEmailHostPort());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", externalConfigs.getEmailProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        setEncryptionProtocol(props, externalConfigs.getEmailProtocol());
        return mailSender;
    }
    private void setEncryptionProtocol(Properties props, String protocol){
        if(protocol.equalsIgnoreCase("starttls") || protocol.equalsIgnoreCase("tls")){
            props.put("mail.smtp.starttls.enable", true);
        } else {
            props.put("mail.smtp.startssl.enable", true);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.ssl.enable", "true");
        }
    }
}
