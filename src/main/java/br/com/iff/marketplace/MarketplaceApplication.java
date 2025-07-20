package br.com.iff.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
		"br.com.iff.marketplace.user",
		"br.com.iff.marketplace.product",
		"br.com.iff.marketplace.category",
		"br.com.iff.marketplace.order",
		"br.com.iff.marketplace.review",
		"br.com.iff.marketplace.model"
})
public class MarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketplaceApplication.class, args);
	}
}