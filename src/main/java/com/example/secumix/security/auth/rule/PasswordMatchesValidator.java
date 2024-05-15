package com.example.secumix.security.auth.rule;




import com.example.secumix.security.user.ChangePasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(ChangePasswordRequest changePasswordRequest, ConstraintValidatorContext context) {
        return changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword());
    }
}
