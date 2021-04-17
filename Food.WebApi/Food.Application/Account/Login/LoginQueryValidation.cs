using FluentValidation;
using System;
using System.Collections.Generic;
using System.Text;

namespace Food.Application.Account.Login
{
    public class LoginQueryValidation : AbstractValidator<LoginQuery>
    {
        public LoginQueryValidation()
        {
            RuleFor(x => x.Email).NotEmpty().WithMessage("Пошта не може бути порожньою");

            RuleFor(x => x.Password).NotEmpty().WithMessage("Пароль не може бути порожнім");
        }
    }
}
