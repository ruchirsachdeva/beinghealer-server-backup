package com.beinghealer.service;

import com.beinghealer.model.User;
import com.beinghealer.dto.UserParams;
import com.beinghealer.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.Resource;
import javax.validation.ParameterNameProvider;
import javax.validation.executable.ExecutableValidator;
import java.util.Optional;

@Component
public class UserParamsValidator extends LocalValidatorFactoryBean {
	
	private UserRepository userRepository;
	
	@Resource
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(UserParams.class);
	}

	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		
		super.validate(obj, errors, validationHints);
		
		if (!errors.hasErrors()) {
			UserParams signupForm = (UserParams) obj;
			Optional<User> user = userRepository.findByUsername(signupForm.getEmail().orElse(null));
			if (!user.isPresent())
				errors.rejectValue("email", "emailNotUnique");			
		}
		
	}

	@Override
	public ExecutableValidator forExecutables() {
		return null;
	}

	@Override
	public ParameterNameProvider getParameterNameProvider() {
		return null;
	}
}
