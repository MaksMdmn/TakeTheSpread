#region Using declarations
using System;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using System.Web;

using System.ComponentModel;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Xml.Serialization;
using NinjaTrader.Cbi;
using NinjaTrader.Data;
using NinjaTrader.Gui.Chart;
#endregion

// This namespace holds all strategies and is required. Do not change it.
namespace NinjaTrader.Strategy
{
    /// <summary>
    /// Enter the description of your strategy here
    /// </summary>
    [Description("Enter the description of your strategy here")]
    public class TcpClientStrategy : Strategy
    {
        #region Variables
        // Wizard generated variables
            private int myInput0 = 1; // Default setting for MyInput0
        // User defined variables (add any user defined variables below)
        #endregion
		private StreamWriter tcpWriter;
		private TcpClient tcpClient;
		private Thread thrMessaging;
		private Thread thrSendering;
		private StreamReader tcpReader;
		private bool isCon;
		private String prevMessage = "";

        /// <summary>
        /// This method is used to configure the indicator and is called once before any bar data is loaded.
        /// </summary>
        protected override void Initialize()
        {
			CalculateOnBarClose = true;
			Enabled = true;
			Unmanaged = true;
			Add("CL 07-16", PeriodType.Minute,1);
        }

        protected override void OnStartUp()
		{
			InitializeConnection ();
		}

		private void InitializeConnection()
		{
			try
			{
				logicConnect();
				logicWrite();
				logicRead();
			}
			catch (Exception e2)
			{
				correctClosingAndAbortCon();
				Print("Initialize exception " + e2.ToString());
			}
		}

		private void logicConnect(){
			IPAddress ipAddr = IPAddress.Parse("127.0.0.1");
			tcpClient = new TcpClient();
			tcpClient.Connect(ipAddr, 8085);
			isCon = true;
		}

		private void logicRead(){
			tcpReader = new StreamReader(tcpClient.GetStream());

			thrMessaging = new Thread(new ThreadStart(ReceiveMessages));
			thrMessaging.Start();
		}

		private void logicWrite(){
			tcpWriter = new StreamWriter(tcpClient.GetStream());
			tcpWriter.WriteLine("TEST1.23");
		}

		private void ReceiveMessages()
		{
			while (isCon)
			{
				try{
					String con = tcpReader.ReadLine();
					processMessage(con);


				}catch (Exception e4){
					Print("receiveMessages exception: " + e4.ToString());
					correctClosingAndAbortCon();
				}
			}
		}

		private void processMessage(String msg)
		{
			Print(msg);
			String instrum = Instrument.FullName.Replace(" ","");
			instrum = instrum.Remove(instrum.Length - 3);

			//parsing here
			String[] tempArr = msg.Split(new[]{":-:"}, StringSplitOptions.None);
			String msgId = tempArr[0];
			String msgCmd = tempArr[1];
			String msgParam = tempArr[2];

			Print("id: " + msgId + "cmd: " + msgCmd + "param: " + msgParam);

			switch (msgCmd){
				case "GJ":
					Print("Good buy, Blue Sky!");
					correctClosingAndAbortCon();
					break;
				case "ORDS":
					System.Collections.IEnumerator ListOfOrders = Account.Orders.GetEnumerator();
					SendMessages(msgId, ListOfOrders.ToString());
//					for (int i = 0; i < Account.Orders.Count;i++)
//					{
//						ListOfOrders.MoveNext();
//						Print(ListOfOrders.Current.ToString());
//					}

					break;
				case "BYID":
					//msgParam
					SendMessages(msgId, Orders.FindByOrderId(msgParam).ToString());
					break;
				case "POS":
					SendMessages(msgId, Positions[0].Quantity.ToString());
					SendMessages(msgId, Positions[1].Quantity.ToString());
					break;
				case "BPOW":
					SendMessages(msgId, GetAccountValue(AccountItem.BuyingPower).ToString());
					break;
				case "CSHV":
					SendMessages(msgId, GetAccountValue(AccountItem.CashValue).ToString());
					break;
				case "RPNL":
					SendMessages(msgId, GetAccountValue(AccountItem.RealizedProfitLoss).ToString());
					break;
					//1 equals the parameter which we need to recieve
				case "BMRT":
					SubmitOrder(0,OrderAction.Buy, OrderType.Market, 1,0,0,"","");
					SubmitOrder(1,OrderAction.Sell, OrderType.Market, 2,0,0,"","");
					break;
				case "BLMT":
					SubmitOrder(0,OrderAction.Buy,OrderType.Limit,1,1,0,"","");
					break;
				case "SMRT":
					SubmitOrder(0,OrderAction.Sell, OrderType.Market, 1,0,0,"","");
					break;
				case "SLMT":
					SubmitOrder(0,OrderAction.Sell,OrderType.Limit,1,1,0,"","");
					break;
				case "BBID0":
					SendMessages(msgId, instrum
						+ " "
						+ "b["
							+ GetCurrentBid(0)
							+ " "
							+ GetCurrentBidVolume(0)
						+ "]"
						+ " a["
							+ GetCurrentAsk(0)
							+ " "
							+ GetCurrentAskVolume(0)
						+ "]"
						+ Environment.NewLine);
					break;
				case "BBID1":
					SendMessages(msgId, instrum
						+ " "
						+ "b["
							+ GetCurrentBid(1)
							+ " "
							+ GetCurrentBidVolume(1)
						+ "]"
						+ " a["
							+ GetCurrentAsk(1)
							+ " "
							+ GetCurrentAskVolume(1)
						+ "]"
						+ Environment.NewLine);
					break;
				default:
					break;
			}
		}

		private void SendMessages(String messageId, String message)
		{

			if (message != "")
				{
					tcpWriter.WriteLine(messageId + ":-:" + message);
					tcpWriter.Flush();
				}
		}


		protected override void OnTermination()
		{
			Print("termination...");
			correctClosingAndAbortCon();
		}

        protected override void OnBarUpdate()
        {
        }

		protected override void OnMarketData(MarketDataEventArgs e)
		{
//			String instrum = Instrument.FullName.Replace(" ","");
//			instrum = instrum.Remove(instrum.Length - 3);
//
//			Print(instrum
//						+ " "
//						+ "b["
//							+ GetCurrentBid()
//							+ " "
//							+ GetCurrentBidVolume()
//						+ "]"
//						+ " a["
//							+ GetCurrentAsk()
//							+ " "
//							+ GetCurrentAskVolume()
//						+ "]"
//						+ Environment.NewLine);
//			try{
//				if(BarsInProgress == 0){
//					if (e.MarketDataType == MarketDataType.Last)
//					{
//						String tickStr = "CL06, time: " + e.Time + " "
//						+ "bid: " + GetCurrentBid() + " "
//						+ "bidVol: " + GetCurrentBidVolume() +
//						+ "ask: " + GetCurrentAsk() + " "
//						+ "askVol: " + GetCurrentAskVolume() + " "
//						Environment.NewLine;
//
//						if (prevMessage!=tickStr){
//							SendMessages(msgId, tickStr);
//							prevMessage = tickStr;
//						}
//
//					}
//				}else{
//					if (e.MarketDataType == MarketDataType.Last)
//					{
//						String tickStr = "CL07, time: " + e.Time + " "
//						+ "bid: " + GetCurrentBid() + " "
//						+ "bidVol: " + GetCurrentBidVolume() +
//						+ "ask: " + GetCurrentAsk() + " "
//						+ "askVol: " + GetCurrentAskVolume() + " "
//						Environment.NewLine;
//
//						if (prevMessage!=tickStr){
//							SendMessages(msgId, tickStr);
//							prevMessage = tickStr;
//						}
//
//					}
//				}
//			}catch(Exception e3){
//				correctClosingAndAbortCon();
//				Print("onMarketData exception: " + e3.ToString());
//			}
		}

		private void correctClosingAndAbortCon(){
			isCon = false;

			if (thrMessaging != null) {
				thrMessaging = null;
			}

			if (tcpClient != null) {
				tcpClient.GetStream().Close();
				tcpClient.Close();
				tcpClient = null;
			}

			if (tcpWriter != null) {
				tcpWriter.Close();
				tcpWriter = null;
			}

			if (tcpReader != null) {
				tcpReader.Close();
				tcpReader = null;
			}

			Disable();
		}


        #region Properties
        [Description("")]
        [GridCategory("Parameters")]
        public int MyInput0
        {
            get { return myInput0; }
            set { myInput0 = Math.Max(1, value); }
        }
        #endregion
    }
}
