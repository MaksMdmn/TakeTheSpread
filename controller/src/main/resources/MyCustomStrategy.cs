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
			SendMessages("TEST SUCCESS 1.23");
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
			switch (msg){
				case "GJ":
					Print("Good buy, Blue Sky!");
					correctClosingAndAbortCon();
					break;
				case "ORD":
//					openOrder();
					SubmitOrder(0,OrderAction.Buy, OrderType.Market, 1,0,0,"","");
					break;
				case "ORDS":
					SubmitOrder(0,OrderAction.Sell, OrderType.Market, 1,0,0,"","");
					break;
				case "NEWID":
					Print(Account.Orders.ToString());
					System.Collections.IEnumerator ListOfOrders = Account.Orders.GetEnumerator();
					for (int i = 0; i < Account.Orders.Count;i++)
					{
						ListOfOrders.MoveNext();
						Print(ListOfOrders.Current.ToString());
					}
					Print(Orders.Count.ToString());
//					SendMessages(Orders.ToString());
					break;
				case "POS":
//					SendMessages(GetAtmStrategyPositionQuantity(tmp).ToString());
					SendMessages(Position.Quantity.ToString());
					break;
				case "BPOW":
					SendMessages(GetAccountValue(AccountItem.BuyingPower).ToString());
					break;
				case "CSHV":
					SendMessages(GetAccountValue(AccountItem.CashValue).ToString());
					break;
				case "RPNL":
					SendMessages(GetAccountValue(AccountItem.RealizedProfitLoss).ToString());
					break;
				default:
					break;
			}
		}

		private void openOrder() {
			AtmStrategyCreate(OrderAction.Buy,
					OrderType.Market,
					0,
					0,
					TimeInForce.Day,
					GetAtmStrategyUniqueId(),
					"atm",
					GetAtmStrategyUniqueId());
		}


		//				p = HttpUtility.UrlEncode(p, System.Text.Encoding.UTF8);
		private void SendMessages(String message)
		{
			Print("COMMAND IS: " + message);

			if (message != "")
				{
					tcpWriter.WriteLine(message);
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
//			try{
//				if (e.MarketDataType == MarketDataType.Last)
//				{
//					String tickStr = "t: " + e.Time + " "
//					+ "ask: " + GetCurrentAsk() + " "
//					+ "askVol: " + GetCurrentAskVolume() + " "
//					+ "bid: " + GetCurrentBid() + " "
//					+ "bidVol: " + GetCurrentBidVolume() +
//					Environment.NewLine;
//
//					if (prevMessage!=tickStr){
//						SendMessages(tickStr);
//						prevMessage = tickStr;
//					}
//
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
