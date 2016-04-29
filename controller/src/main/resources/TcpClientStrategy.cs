#region Using declarations
using System;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using System.Web;
using System.Globalization;
using System.Collections.Generic;

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
		private Dictionary<String, IOrder> orderMap = new Dictionary<String, IOrder>();



		//handle by myself
		private String[] ntToken = new[]{":-:"};
		private String secondInstrTicker = "CL 07-16";
		private int ntPort = 8085;
		private String ntHost = "127.0.0.1";
		//handle by myself

        /// <summary>
        /// This method is used to configure the indicator and is called once before any bar data is loaded.
        /// </summary>
        protected override void Initialize()
        {
			CalculateOnBarClose = true;
			Enabled = true;
			Unmanaged = true;
			Add(secondInstrTicker, PeriodType.Minute,1);
        }

        protected override void OnStartUp()
		{
			InitializeConnection ();
		}

		private void InitializeConnection()
		{
			try
			{
				proceedConn();
				proceedWrite();
				proceedRead();
			}
			catch (Exception e2)
			{
				correctClosingAndAbortCon();
				Print("Initialize exception " + e2.ToString());
			}
		}

		private void proceedConn(){
			IPAddress ipAddr = IPAddress.Parse(ntHost);
			tcpClient = new TcpClient();
			tcpClient.Connect(ipAddr, ntPort);
			isCon = true;
		}

		private void proceedRead(){
			tcpReader = new StreamReader(tcpClient.GetStream());

			thrMessaging = new Thread(new ThreadStart(ReceiveMessages));
			thrMessaging.Start();
		}

		private void proceedWrite(){
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
			try{
					//parsing here
					Print(msg);
					String[] tempArr = msg.Split(ntToken, StringSplitOptions.None);
					String msgId = tempArr[0];
					String msgCmd = tempArr[1];
					String msgParam = tempArr[2];
					int instrumentN = -1;
					String ordId = "";
					int size = -1;
					double price = -1d;

					//param
					String[] tempParamArr;
					if (msgParam == ""){
						tempParamArr = new String[]{};
					}else{
						tempParamArr = msgParam.Split(new[]{" "}, StringSplitOptions.None);
					}

					switch(tempParamArr.Length){
						case 0:
//							NOP
							break;
						case 1:
							if(msgCmd == "BYID" || msgCmd == "CNID" || msgCmd == "FLLD"){
								ordId = tempParamArr[0];
							}else{
								instrumentN = Convert.ToInt32(tempParamArr[0]);
							}
							break;
						case 2:
							instrumentN = Convert.ToInt32(tempParamArr[0]);
							size =  Convert.ToInt32(tempParamArr[1]);
							break;
						case 3:
							instrumentN = Convert.ToInt32(tempParamArr[0]);
							size =  Convert.ToInt32(tempParamArr[1]);
							price = Double.Parse(tempParamArr[2],NumberFormatInfo.InvariantInfo);
							break;
						default:
							ifParamIncorrect(instrumentN + " " + size + " " + price);
							break;
					}

					switch (msgCmd){
						case "GJ":
							Print("Good buy, Blue Sky!");
							correctClosingAndAbortCon();
							break;
						case "ORDS":
							System.Collections.IEnumerator ListOfOrders = Account.Orders.GetEnumerator();
							String tempStr = "";
							for (int i = 0; i < Account.Orders.Count;i++)
							{
								ListOfOrders.MoveNext();
								tempStr += "ord:" + ListOfOrders.Current.ToString();
							}
							SendMessages(msgId, tempStr);
							break;
						case "BYID":
							SendMessages(msgId, Orders.FindByOrderId(ordId).ToString());
							break;
						case "POS":
							int pos = Positions[instrumentN].Quantity;
							switch(Positions[instrumentN].MarketPosition){
								case(MarketPosition.Flat):
									pos = pos * 0;
									break;
								case(MarketPosition.Long):
									pos = pos * 1;
									break;
								case(MarketPosition.Short):
									pos = pos * -1;
									break;
								default:
									ifParamIncorrect("market pos incorrect");
									break;
							}

							SendMessages(msgId, pos.ToString());
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
						case "BMRT":
							SubmitOrder(instrumentN,OrderAction.Buy, OrderType.Market, size, 0, 0, "", "");
							break;
						case "BLMT":
							String tempBOrdId = AddToOrderMap(SubmitOrder(instrumentN, OrderAction.Buy, OrderType.Limit, size, price, 0, "", ""));
							SendMessages(msgId, tempBOrdId);
							break;
						case "SMRT":
							SubmitOrder(instrumentN, OrderAction.Sell, OrderType.Market, size, 0, 0, "", "");
							break;
						case "SLMT":
							String tempSOrdId = AddToOrderMap(SubmitOrder(instrumentN, OrderAction.Sell, OrderType.Limit, size, price, 0, "", ""));
							SendMessages(msgId, tempSOrdId);
							break;
						case "CNAL":
							CancelAllOrders(true,true);
							break;
						case "CNID":
							CancelOrder(orderMap[ordId]);
							break;
						case "FLLD":
							SendMessages(msgId,orderMap[ordId].Filled.ToString());
							break;
						case "BDAK":
							SendMessages(msgId,
								"b["
									+ GetCurrentBid(instrumentN)
									+ " "
									+ GetCurrentBidVolume(instrumentN)
								+ "]"
								+ " a["
									+ GetCurrentAsk(instrumentN)
									+ " "
									+ GetCurrentAskVolume(instrumentN)
								+ "]");
							break;
						default:
							ifParamIncorrect(instrumentN + " " + size + " " + price);
							break;
					}
			}catch(Exception e5){
				Print("processMessage exception: " + e5.ToString() + "message was: " + msg );
			}
		}

		private void SendMessages(String messageId, String message)
		{
			try{
				if (message != "")
					{
						tcpWriter.WriteLine(messageId + String.Concat(ntToken) + message);
						tcpWriter.Flush();
					}
			}catch(Exception e6){
				Print("send message exception: " + e6.ToString());
			}
		}

		private String AddToOrderMap(IOrder order){
			orderMap.Add(order.OrderId, order);
			return order.OrderId;
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

		private void ifParamIncorrect(String s){
			Print("incorrect parametres. I'm out - bb man" + s);
			Disable();
			correctClosingAndAbortCon();
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
