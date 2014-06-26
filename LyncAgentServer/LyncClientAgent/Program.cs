using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.DirectoryServices;
using System.DirectoryServices.AccountManagement;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace LyncClientAgent
{
    class Program
    {
        static void Main(string[] args)
        {
            new LyncLocationSetter().Run();
        }

        static void TestAD()
        {
            using (var context = new PrincipalContext(ContextType.Domain, "ads.zuehlke.com"))
            {
                UserPrincipal up = new UserPrincipal(context);
                using (var searcher = new PrincipalSearcher(up))
                {
                    up.SamAccountName = "wmax";
                    foreach (var result in searcher.FindAll())
                    {
                        DirectoryEntry de = result.GetUnderlyingObject() as DirectoryEntry;
                        if (de.Properties["samAccountName"].Value.Equals("wmax"))
                        {
                            foreach (var val in de.Properties.PropertyNames)
                            {
                                Console.WriteLine(val + ": " + de.Properties[val.ToString()].Value);
                            }
                        }
                        /*
                        Console.WriteLine("First Name: " + de.Properties["givenName"].Value);
                        Console.WriteLine("Last Name : " + de.Properties["sn"].Value);
                        Console.WriteLine("SAM account name   : " + de.Properties["samAccountName"].Value);
                        Console.WriteLine("User principal name: " + de.Properties["userPrincipalName"].Value);
                        Console.WriteLine();
                         */
                    }
                }
            }
            Console.ReadLine();
        }
    }
}
