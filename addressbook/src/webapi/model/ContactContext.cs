using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace Models
{
    public class DbContactContext : DbContext
    {
        public DbContactContext(DbContextOptions<ContactContext> options)
            : base(options)
        { }

        public DbSet<Contact> Contacts { get; set; }
    }
}
