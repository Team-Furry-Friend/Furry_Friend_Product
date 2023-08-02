package com.v3.furry_friend_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FurryFriendProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurryFriendProductApplication.class, args);
    }

}
