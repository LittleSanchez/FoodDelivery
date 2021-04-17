using MediatR;
using System;
using System.Collections.Generic;
using System.Text;

namespace Food.Application.Account.Login
{
    public class LoginQuery : IRequest<UserViewModel>
    {
        public string Email { get; set; }

        public string Password { get; set; }
    }
}
