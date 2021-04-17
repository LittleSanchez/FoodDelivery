using Food.Application.Account;
using MediatR;
using System;
using System.Collections.Generic;
using System.Text;

namespace Food.Application.User.Profile
{
    public class UserProfileCommand: IRequest<UserViewModel>
    {
        public string UserName { get; set; }
    }
}
