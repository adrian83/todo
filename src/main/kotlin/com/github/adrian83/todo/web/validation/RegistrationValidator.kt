package com.github.adrian83.todo.web.validation

import com.github.adrian83.todo.web.model.Registration;
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class RegistrationValidator: ConstraintValidator<ValidRegistration, Registration> {
	
	
    override fun initialize(validRegistration: ValidRegistration ) {
    }

    override fun isValid(registration: Registration, context: ConstraintValidatorContext ): Boolean {
//        if ( car == null ) {
//            return true;
//        }
//
//        return car.getPassengers().size() <= car.getSeatCount();
		return false
    }
	
	
}