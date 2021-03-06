package me.quiz_together.root.support.hashid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HashUserIdConstraintValidator implements ConstraintValidator<HashUserId, Long> {
    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if(id == null || id >= 0) {
            return true;
        }

        return false;
    }
}
