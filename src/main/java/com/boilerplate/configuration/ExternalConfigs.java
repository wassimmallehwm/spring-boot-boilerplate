package com.boilerplate.configuration;

import java.io.File;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.boilerplate.configuration.data.AES;

@Component
public class ExternalConfigs {

	private static final Logger logger = LogManager.getLogger(ExternalConfigs.class);
	private File externalConfigPath;
	private PropertiesConfiguration configuration;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${external.config.path}")
	private String configPath;

	@PostConstruct
	private void init() {
		try {
			if (activeProfile.equals("prod")) {
				ClassPathResource resource = new ClassPathResource("external-config.properties");
				externalConfigPath = resource.getFile();
			} else {
				externalConfigPath = new File(configPath + "external-config-dev.properties");
			}
			configuration = new PropertiesConfiguration(externalConfigPath);

			FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();

			fileChangedReloadingStrategy.setRefreshDelay(5 * 1000 * 60 * 12);
			configuration.setReloadingStrategy(fileChangedReloadingStrategy);

			encryptExternalProperties();
		} catch (ConfigurationException e) {
			logger.error("Enable to parse the config file");
			e.printStackTrace();

		} catch (IOException e) {
			logger.error("Failed to application config file");
		}
	}

	private void encryptExternalProperties() {
		encryptPropertie("spring.mail.password");
		encryptPropertie("spring.datasource.password");

		try {
			configuration.save();
		} catch (ConfigurationException e) {
			logger.error("Encrypt properties value in the config file failed");
			e.printStackTrace();
		}
	}

	private void encryptPropertie(String propertieName) {
		String propertieValue = configuration.getString(propertieName);
		if (!propertieValue.startsWith(AES.ENCRYPTION_PREFIX)) {
			configuration.setProperty(
					propertieName,
					AES.encrypt(propertieValue, null, true)
			);
		}
	}
	public boolean isDatabaseInitialized() {
		return configuration.getBoolean("is_initialized");
	}

	public void setIsDatabaseInitialized(boolean isInitialized) {
		configuration.setProperty("is_initialized", isInitialized);
		try {
			configuration.save();
		} catch (ConfigurationException e) {
			logger.error("Updating \'is_initialized\' value in the config file failed");
			e.printStackTrace();
		}
	}
	public String getRessourceFolderPath() {
		return externalConfigPath.getParentFile().getAbsolutePath() + File.separator;
	}
	public String getAppEnv() {
		return configuration.getString("app.env");
	}
	public Boolean isAppEnvDev() {
		return getAppEnv().equalsIgnoreCase("dev");
	}
	public Boolean isAppEnvProd() {
		return getAppEnv().equalsIgnoreCase("prod");
	}

	//Database properties
	public String getDBUrl() {
		return configuration.getString("spring.datasource.url");
	}
	public String getDBUsername() {
		return configuration.getString("spring.datasource.username");
	}
	public String getDBPassword() {
		String password = configuration.getString("spring.datasource.password");
		return AES.decrypt(password, null, true);
	}
	public Long getConnectionTimeout() {
		return configuration.getLong("spring.datasource.connection-timeout");
	}
	public int getMinimumIdle() {
		return configuration.getInt("spring.datasource.minimum-idle");
	}
	public int getMaximumPoolSize() {
		return configuration.getInt("spring.datasource.maximum-pool-size");
	}
	public Long getIdleTimeout() {
		return configuration.getLong("spring.datasource.idle-timeout");
	}
	public Long getMaxLifetime() {
		return configuration.getLong("spring.datasource.max-lifetime");
	}
	public boolean getAutoCommit() {
		return configuration.getBoolean("spring.datasource.auto-commit");
	}

	public String getJwtSecret() {
		return configuration.getString("security.jwt.secret");
	}
	public Integer getJwtAccessTokenExpiration() {
		return configuration.getInt("security.jwt.access.expiration");
	}
	public Integer getJwtRefreshTokenExpiration() {
		return configuration.getInt("security.jwt.refresh.expiration");
	}
	public String getAppLink(){
		return configuration.getString("app.link");
	}
	public String getEmailHost(){
		return configuration.getString("spring.mail.host");
	}
	public Integer getEmailHostPort(){
		return configuration.getInt("spring.mail.host.port");
	}
	public String getEmailUsername(){
		return configuration.getString("spring.mail.username");
	}
	public String getEmailSender(){
		return configuration.getString("spring.mail.sender");
	}
	public String getEmailPassword(){
		String password = configuration.getString("spring.mail.password");
		return AES.decrypt(password, null, true);
	}
	public String getEmailProtocol(){
		return configuration.getString("spring.mail.transport.protocol");
	}
	public String getTrackingLink(){
		return configuration.getString("tracking.link");
	}
	public String getCaughtLink(){
		return configuration.getString("caught.link");
	}
	public String getTrainLink(){
		return configuration.getString("train.link");
	}
	public int getStreamChunkSize(){
		return configuration.getInt("stream.chunk.size");
	}

	public Integer getOtpTokenExpiration(){
		return configuration.getInt("email.token.otp");
	}
	public Integer getAccountActivationTokenExpiration(){
		return configuration.getInt("email.token.account.activation");
	}
	public Integer getForgotPasswordTokenExpiration(){
		return configuration.getInt("email.token.forgot.password");
	}
	public int getMaxLoginAttempt() {
		return configuration.getInt("auth.max.attempts");
	}
	public Long getLoginExpireBlocageDuration() {
		return configuration.getLong("auth.expire.blockage.duration");
	}
	public String getLoginExpireBlocageUnit() {
		return configuration.getString("auth.expire.blockage.time.unit");
	}
	public String getAssetsLink() {
		return configuration.getString("payloads.assets.link");
	}
}
