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
    }
}
