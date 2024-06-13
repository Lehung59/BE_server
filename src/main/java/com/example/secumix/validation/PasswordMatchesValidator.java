package com.example.secumix.validation;




import com.example.secumix.payload.request.ChangePasswordRequest;

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
