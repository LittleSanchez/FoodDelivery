using Food.Application.Account;
using Food.Application.User.Profile;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Food.WebApi.Controllers
{
    public class UserController : BaseController
    {
        [HttpGet("profile")]
        [Authorize]
        public async Task<ActionResult<UserViewModel>> ProfileAsync()
        {
            UserProfileCommand userCommand = new UserProfileCommand
            {
                UserName = User.Claims.FirstOrDefault(x => x.Type == "username").Value
            };

            return await Mediator.Send(userCommand);
        }

        [HttpPost("change-image")]
        public async Task<ActionResult<UserViewModel>> UsersAsync(ChangeImageCommand command)
        {
            return await Mediator.Send(command);
        }

    }
}
