using Food.Application.Account;
using Food.Application.Exceptions;
using Food.Domain;
using MediatR;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Food.Application.User.Profile
{
    public class UserProfileHandler : IRequestHandler<UserProfileCommand, UserViewModel>
    {
        private readonly UserManager<AppUser> _userManager;
        public UserProfileHandler(UserManager<AppUser> userManager)
        {
            _userManager = userManager;
        }
        public async Task<UserViewModel> Handle(UserProfileCommand request, CancellationToken cancellationToken)
        {
            var user = await _userManager.FindByNameAsync(request.UserName);
            if (user == null)
            {
                throw new RestException(HttpStatusCode.NotFound);
            }

            return new UserViewModel
            {
                DisplayName = user.DisplayName,
                UserName = user.UserName,
                Image = user.Image ?? "1.jpg",
            };
        }
    }
}
