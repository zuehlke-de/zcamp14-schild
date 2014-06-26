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
        private const string URL = "http://zcamp14-brunhilde.zuehlke.ws:80/location?username={0}";
        private const string LyncNote = "Hier ist mein Schild: {0}";

        private Thread _worker;
        private string _lastLocation;

        public void SetLyncLocation(string location)
        {
            try
            {
                Self self = LyncClient.GetClient().Self;
                var list = new List<KeyValuePair<PublishableContactInformationType, object>>();
                list.Add(new KeyValuePair<PublishableContactInformationType, object>(PublishableContactInformationType.LocationName, location));
                list.Add(new KeyValuePair<PublishableContactInformationType, object>(PublishableContactInformationType.PersonalNote, string.Format(LyncNote, location)));
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
                UserLocation location = GetUserLocation();
                if (location != null && location.Location != _lastLocation)
                {
                    SetLyncLocation(location.Location);
                }
            }
        }

        private UserLocation GetUserLocation()
        {
            try
            {
                var user = System.Security.Principal.WindowsIdentity.GetCurrent();
                string userName = user.Name.Substring(user.Name.LastIndexOf('\\') + 1);
                var request = WebRequest.CreateHttp(string.Format(URL, userName));
                var response = request.GetResponse().GetResponseStream();
                StreamReader responseReader = new StreamReader(response);
                return JsonConvert.DeserializeObject<UserLocation>(responseReader.ReadToEnd());
            }
            catch (WebException ex)
            {
                Console.WriteLine("Exception: " + ex.Message);
            }
            return null;
        }
    }
}
