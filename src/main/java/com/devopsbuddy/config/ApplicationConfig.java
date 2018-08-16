package com.devopsbuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@PropertySource("file:///${user.home}/.devopsbuddy/application-common.properties")
@PropertySource("file:///${user.home}/.devopsbuddy/stripe.properties")
@EnableJpaRepositories(basePackages = "com.devopsbuddy.backend.persistence.repositories")
@EntityScan(basePackages = "com.devopsbuddy.backend.persistence.domain.backend")
@EnableTransactionManagement
public class ApplicationConfig {

	@Value("${aws.s3.profile}")
	private String awsProfileName;

	@Bean
	public AmazonS3 s3Client() {		
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new ProfileCredentialsProvider(awsProfileName))
				.withRegion(Regions.EU_CENTRAL_1)
				.build();
	}
}