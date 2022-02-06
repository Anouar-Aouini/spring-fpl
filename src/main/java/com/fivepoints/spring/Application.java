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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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

		// Save users
		User user1 = new User("Anouar", "Aouini",
					"anouar@gmail.com","true","",
				this.passwordEncoder().encode("123456"),0);

		// ManyToMany Relations
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		user1.setRoles(roles);
		AtomicReference<Boolean> existedAdmin = new AtomicReference<>(false);
		this.userRepository.findAll().forEach(user->{
			if(user.getEmail().equals("anouar@gmail.com")){
				existedAdmin.set(true);
			}
		});
		if(!existedAdmin.get()){
			this.userRepository.save(user1);
		}

	}
}
