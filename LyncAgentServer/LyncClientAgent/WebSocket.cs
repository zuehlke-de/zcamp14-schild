﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace LyncClientAgent
{
    public class WebSocket
    {
        private Uri mUrl;
        private TcpClient mClient;
        private NetworkStream mStream;
        private bool mHandshakeComplete;
        private Dictionary<string, string> mHeaders;

        public WebSocket(Uri url)
        {
            mUrl = url;

            string protocol = mUrl.Scheme;
            if (!protocol.Equals("ws") && !protocol.Equals("wss"))
                throw new ArgumentException("Unsupported protocol: " + protocol);
        }

        public void SetHeaders(Dictionary<string, string> headers)
        {
            mHeaders = headers;
        }

        public void Connect()
        {
            string host = mUrl.DnsSafeHost;
            string path = mUrl.PathAndQuery;
            string origin = "http://" + host;

            mClient = CreateSocket(mUrl);
            mStream = mClient.GetStream();

            int port = ((IPEndPoint)mClient.Client.RemoteEndPoint).Port;
            if (port != 80)
                host = host + ":" + port;

            StringBuilder extraHeaders = new StringBuilder();
            if (mHeaders != null)
            {
                foreach (KeyValuePair<string, string> header in mHeaders)
                    extraHeaders.Append(header.Key + ": " + header.Value + "\r\n");
            }

            string request = "GET " + path + " HTTP/1.1\r\n" +
                             "Upgrade: WebSocket\r\n" +
                             "Connection: Upgrade\r\n" +
                             "Host: " + host + "\r\n" +
                             "Origin: " + origin + "\r\n" +
                             extraHeaders.ToString() + "\r\n";
            byte[] sendBuffer = Encoding.UTF8.GetBytes(request);

            mStream.Write(sendBuffer, 0, sendBuffer.Length);

            StreamReader reader = new StreamReader(mStream);
            {
                string header = reader.ReadLine();
                if (header == null || !header.Equals("HTTP/1.1 101 Web Socket Protocol Handshake"))
                    throw new IOException("Invalid handshake response");

                header = reader.ReadLine();
                if (!header.Equals("Upgrade: WebSocket"))
                    throw new IOException("Invalid handshake response");

                header = reader.ReadLine();
                if (!header.Equals("Connection: Upgrade"))
                    throw new IOException("Invalid handshake response");
            }

            mHandshakeComplete = true;
        }

        public void Send(string str)
        {
            if (!mHandshakeComplete)
                throw new InvalidOperationException("Handshake not complete");

            byte[] sendBuffer = Encoding.UTF8.GetBytes(str);

            mStream.WriteByte(0x00);
            mStream.Write(sendBuffer, 0, sendBuffer.Length);
            mStream.WriteByte(0xff);
            mStream.Flush();
        }

        public string Recv()
        {
            if (!mHandshakeComplete)
                throw new InvalidOperationException("Handshake not complete");

            StringBuilder recvBuffer = new StringBuilder();

            BinaryReader reader = new BinaryReader(mStream);
            byte b = reader.ReadByte();
            if ((b & 0x80) == 0x80)
            {
                // Skip data frame
                int len = 0;
                do
                {
                    b = (byte)(reader.ReadByte() & 0x7f);
                    len += b * 128;
                } while ((b & 0x80) != 0x80);

                for (int i = 0; i < len; i++)
                    reader.ReadByte();
            }

            while (true)
            {
                b = reader.ReadByte();
                if (b == 0xff)
                    break;

                recvBuffer.Append(b);
            }

            return recvBuffer.ToString();
        }

        public void Close()
        {
            mStream.Dispose();
            mClient.Close();
            mStream = null;
            mClient = null;
        }

        private static TcpClient CreateSocket(Uri url)
        {
            string scheme = url.Scheme;
            string host = url.DnsSafeHost;

            int port = url.Port;
            if (port <= 0)
            {
                if (scheme.Equals("wss"))
                    port = 443;
                else if (scheme.Equals("ws"))
                    port = 80;
                else
                    throw new ArgumentException("Unsupported scheme");
            }

            if (scheme.Equals("wss"))
                throw new NotImplementedException("SSL support not implemented yet");
            else
                return new TcpClient(host, port);
        }
    }
}