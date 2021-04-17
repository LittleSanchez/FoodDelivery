using Food.Application.Account;
using MediatR;
using System;
using System.Collections.Generic;
using System.Text;

namespace Food.Application.User.Profile
{
    public class ChangeImageCommand : IRequest<UserViewModel>
    {
        public string UserName { get; set; }
        public string Image { get; set; }
    }
}
