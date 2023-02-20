package com.github.adrian83.todo.web.validation

import com.github.adrian83.todo.web.model.Registration
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class RegistrationValidator : ConstraintValidator<ValidRegistration, Registration> {

    override fun initialize(validRegistration: ValidRegistration) {}

    override fun isValid(form: Registration?, context: ConstraintValidatorContext): Boolean =
        form != null &&
            (form.password != null && form.repeatedPassword != null) &&
            (form.password.equals(form.repeatedPassword))
}
