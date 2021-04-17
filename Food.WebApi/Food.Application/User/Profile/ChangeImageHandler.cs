using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using Food.Application.Account;
using Food.Domain;
using Food.EFData;
using MediatR;
using Microsoft.AspNetCore.Identity;

namespace Food.Application.User.Profile
{
    public class ChangeImageHandler : IRequestHandler<ChangeImageCommand, UserViewModel>
    {
        private readonly UserManager<AppUser> _userManager;
		private readonly DataContext _context;

        public ChangeImageHandler(UserManager<AppUser> userManager, DataContext context)
        {
            _userManager = userManager;
			_context = context;
        }

        public object DataTime { get; private set; }

        public async Task<UserViewModel> Handle(ChangeImageCommand request, CancellationToken cancellationToken)
        {
			var user = _context.Users.Where(x => x.UserName == request.UserName).First();
			if (request.Image == null || user == null)
				throw new Exception("User not found or image is null");

			/*if (await _context.Users.Where(x => x.UserName == request.UserName).AnyAsync())
			{
				throw new RestException(HttpStatusCode.BadRequest, new { UserName = "UserName already exist" });
			}*/

			//var base64Data = Regex.Match(request.Image, @"data:image/(?<type>.+?),(?<data>.+)").Groups["data"].Value;
			var binData = Convert.FromBase64String(request.Image);

			using (var stream = new MemoryStream(binData))
			{
				var filepath = $"{request.UserName}{DateTimeOffset.Now.ToUnixTimeMilliseconds()}.jpg";
				var image = Bitmap.FromStream(stream);
				image.Save("uploads/" + filepath);
				user.Image = filepath;
				_context.Users.Update(user);
				await _context.SaveChangesAsync();
			}

			return new UserViewModel
			{
				DisplayName = user.DisplayName,
				Image = user.Image,
				UserName = user.UserName
			};


		}
    }
}
