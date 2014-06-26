using Microsoft.Lync.Model;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace LyncClientAgent
{
    public class LyncLocationSetter
    {
        private Thread _worker;
        private string _lastLocation;

        public void SetLyncLocation(string location)
        {
            try
            {
                Self self = LyncClient.GetClient().Self;
                var list = new List<KeyValuePair<PublishableContactInformationType, object>>();
                list.Add(new KeyValuePair<PublishableContactInformationType, object>(PublishableContactInformationType.LocationName, location));
                list.Add(new KeyValuePair<PublishableContactInformationType, object>(PublishableContactInformationType.PersonalNote, location));
                self.EndPublishContactInformation(self.BeginPublishContactInformation(list, null, null));
                Console.WriteLine("Setting Location: {0}", location);
                _lastLocation = location;

            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        public void Start()
        {
            _worker = new Thread(Run);
            _worker.Start();
        }

        public void Run()
        {
            while (true)
            {
                Thread.Sleep(1000);
                UserLocation loc = GetUserLocation();
                if (loc != null && loc.Location != _lastLocation)
                {
                    SetLyncLocation(loc.Location);
                }
            }
        }

        private UserLocation GetUserLocation()
        {
            try
            {
                var user = System.Security.Principal.WindowsIdentity.GetCurrent();
                string userName = user.Name.Substring(user.Name.LastIndexOf('\\') + 1);
                var req = WebRequest.CreateHttp("http://zcamp14-brunhilde.zuehlke.ws:80/location?username=" + userName);
                var response = req.GetResponse().GetResponseStream();
                StreamReader rd = new StreamReader(response);
                return JsonConvert.DeserializeObject<UserLocation>(rd.ReadToEnd());
            }
            catch (WebException ex)
            {
                Console.WriteLine("Exception: " + ex.Message);
            }
            return null;
        }
    }
}
