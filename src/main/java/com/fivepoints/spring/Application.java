package com.fivepoints.spring;

import com.fivepoints.spring.entities.*;
import com.fivepoints.spring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableSwagger2
public class Application implements ApplicationRunner {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// this bean used to crypt the password
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoderBean = applicationContext.getBean(BCryptPasswordEncoder.class);
		return passwordEncoderBean;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// Save roles
		Role adminRole = this.roleRepository.save(new Role(ERole.ADMIN));
		Role userRole = this.roleRepository.save(new Role(ERole.USER));
		Role subscriberRole = this.roleRepository.save(new Role(ERole.SUBSCRIBER));




		// Save users
		User user1 = new User("Anouar", "Aouini",
					"anouar@gmail.com","true",
				this.passwordEncoder().encode("123456"));

		// ManyToMany Relations
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		user1.setRoles(roles);
		this.userRepository.save(user1);


	}
}
