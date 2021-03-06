#region Using declarations
using System;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using System.Web;
using System.Globalization;
using System.Collections.Generic;
using System.Timers;

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
		private StreamReader tcpReader;
		private TcpClient tcpClient;
		private Thread messageHandler;
		private bool isCon;
		private Dictionary<String, IOrder> orderMap = new Dictionary<String, IOrder>();
		private String prevMessage_n = "";
		private String prevMessage_f = "";
		private String currentMessage;
		private String md0 = "";
		private String md1 = "";
		private System.Timers.Timer timer;



		//handle by myself
		private String[] ntToken = new[]{":-:"};
		private String secondInstrTicker = "CL 12-16";
		private int ntPort = 8085;
		private String ntHost = "127.0.0.1";
		private int sendingDelayMs = 50; //200
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
				Print("InitializeConnection exception " + e2.ToString());
				closeTcpEntities();
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

			messageHandler = new Thread(new ThreadStart(ReceiveMessages));
			messageHandler.Start();
		}

		private void proceedWrite(){
			tcpWriter = new StreamWriter(tcpClient.GetStream());
			tcpWriter.WriteLine("TEST1.23");

			timer  = new System.Timers.Timer(sendingDelayMs);
			timer.Elapsed+=OnTimedEvent;
			timer.AutoReset = true;
			timer.Enabled = true;
		}

		private void OnTimedEvent(Object source, ElapsedEventArgs e)
		{	//id 0 is market data messages!
			SendMessages("n", md0);
			SendMessages("f", md1);
		}

		private void ReceiveMessages()
		{
			while (isCon)
			{
				try{
					String msgFromJ = tcpReader.ReadLine();
					processMessage(msgFromJ);

				}catch (Exception e4){
					Print("receiveMessages exception: " + e4.ToString());
					closeTcpEntities();
				}
			}
		}

		private void processMessage(String msg)
		{
			try{
					String[] tempArr = msg.Split(ntToken, StringSplitOptions.None);
					String msgId = tempArr[0];
					String msgCmd = tempArr[1];
					String msgParam = tempArr[2];
					int instrumentN = -1;
					String ordId = "";
					int size = -1;
					double price = -1d;

					IOrder tempIOrd;

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
							if(msgCmd == "CHOR"){
								ordId = tempParamArr[0];
							}else{
								instrumentN = Convert.ToInt32(tempParamArr[0]);
							}
							size =  Convert.ToInt32(tempParamArr[1]);
							price = Double.Parse(tempParamArr[2],NumberFormatInfo.InvariantInfo);
							break;
						default:
							ifParamIncorrect(instrumentN + " " + size + " " + price);
							break;
					}

					switch (msgCmd){
						case "OFF":
							Print("Good buy, Blue Sky!");
							CancelAllOrders(true,true);
							closeTcpEntities();
							break;
						case "ORDS":
							System.Collections.IEnumerator ListOfOrders = Account.Orders.GetEnumerator();
							String tempStr = "";
							if(Account.Orders.Count == 0){
								tempStr = " "; //just empty row, cause in java "" equal to null when parse...
							}else{
								for (int i = 0; i < Account.Orders.Count;i++)
								{
									ListOfOrders.MoveNext();
									Order ord1 = (Order)ListOfOrders.Current;
									tempStr += "ord:" + ord1.ToString() + " Time=" + "'" + ord1.Time.ToString() + "'";
								}
							}
							SendMessages(msgId, tempStr);
							break;
						case "BYID":
							Order ord = Orders.FindByOrderId(ordId);

							Print("id: " + msgId + " msg: " + ord.ToString() + " Time=" + "'" + ord.Time.ToString() + "'");

							SendMessages(msgId, ord.ToString() + " Time=" + "'" + ord.Time.ToString() + "'");
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
									printPonentialFailInfo("before submitOrder market buy" + msgCmd);
							tempIOrd = SubmitOrder(instrumentN,OrderAction.Buy, OrderType.Market, size, 0, 0, "", "");
							AddToOrderMap(tempIOrd);
							SendMessages(msgId, PrepareOrderToSend(tempIOrd));
									printPonentialFailInfo("success market buy");
							break;
						case "BLMT":
									printPonentialFailInfo("before submitOrder " + msgCmd);
							tempIOrd = SubmitOrder(instrumentN, OrderAction.Buy, OrderType.Limit, size, price, 0, "", "");
									printPonentialFailInfo("before adding to map");
							AddToOrderMap(tempIOrd);
									printPonentialFailInfo("before sending msg");
							SendMessages(msgId, PrepareOrderToSend(tempIOrd));
									printPonentialFailInfo("success");
							break;
						case "SMRT":
									printPonentialFailInfo("before submitOrder market sell" + msgCmd);
							tempIOrd = SubmitOrder(instrumentN, OrderAction.Sell, OrderType.Market, size, 0, 0, "", "");
							AddToOrderMap(tempIOrd);
							SendMessages(msgId, PrepareOrderToSend(tempIOrd));
									printPonentialFailInfo("success market sell");
							break;
						case "SLMT":
									printPonentialFailInfo("before submitOrder " + msgCmd);
							tempIOrd = SubmitOrder(instrumentN, OrderAction.Sell, OrderType.Limit, size, price, 0, "", "");
									printPonentialFailInfo("before adding to map");
							AddToOrderMap(tempIOrd);
									printPonentialFailInfo("before sending msg");
							SendMessages(msgId, PrepareOrderToSend(tempIOrd));
									printPonentialFailInfo("success");
							break;
						case "CNAL":
							CancelAllOrders(true,true);
							break;
						case "CNID":
									printPonentialFailInfo("before cancelOrder " + msgCmd);
							CancelOrder(orderMap[ordId]);
									printPonentialFailInfo("waiting for FILLED in map");
							if(orderMap[ordId].OrderState == OrderState.Filled){
								SendMessages(msgId, PrepareOrderToSend(orderMap[ordId]));
							}else{
								while(orderMap[ordId].OrderState != OrderState.Cancelled)
								{
	//								NOP ---- trying to get final filled value
								}
									printPonentialFailInfo("before sending msg");
								SendMessages(msgId,PrepareOrderToSend(orderMap[ordId]));
									printPonentialFailInfo("success");
							}
							break;
						case "CHOR":
							ChangeOrder(orderMap[ordId], size, price, 0);
							SendMessages(msgId, PrepareOrderToSend(orderMap[ordId]));
							break;
						case "FLLD":
							SendMessages(msgId,orderMap[ordId].Filled.ToString());
							break;
//						case "BDAK":
//							currentMessage = getBDAKMessage(instrumentN);
//							SendMessages(msgId, currentMessage);
//							break;
						default:
							ifParamIncorrect(instrumentN + " " + size + " " + price);
							break;
					}
			}catch(NullReferenceException e13){
//				NOP
			}
			catch(Exception e5){
				Print("processMessage exception: " + e5.ToString() + "message was: " + msg );
			}
		}

		private void SendMessages(String messageId, String message)
		{
//			Print(messageId + " " + message);
			lock(tcpWriter){
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
		}

		private String AddToOrderMap(IOrder order){
			orderMap.Add(order.OrderId, order);
			return order.OrderId;
		}

		private String PrepareOrderToSend(IOrder order){
			return order.ToString()  + " Time=" + "'" + order.Time.ToString() + "'";
		}

		protected override void OnTermination()
		{
			Print("calling termination method.");
			closeTcpEntities();
		}

        protected override void OnBarUpdate()
        {

        }

		protected override void OnMarketData(MarketDataEventArgs e)
		{
			try{
				if(BarsInProgress == 0){
					md0 = getBDAKMessage(0);
				}

				if(BarsInProgress == 1){
					md1 = getBDAKMessage(1);
				}

			}catch(Exception e3){
				Print("onMarketData exception: " + e3.ToString());
				closeTcpEntities();
			}
		}

		private String getBDAKMessage(int instrN){
			return "b"
						+ " "
						+ GetCurrentBid(instrN)
						+ " "
						+ GetCurrentBidVolume(instrN)
						+ " " +
					"a"
						+ " "
						+ GetCurrentAsk(instrN)
						+ " "
						+ GetCurrentAskVolume(instrN);
		}

		private void ifParamIncorrect(String s){
			Print("incorrect parametres. I'm out - bb man" + s);
			closeTcpEntities();
		}

		private void closeTcpEntities(){
			isCon = false;
			if(timer != null){
				timer.Stop();
				timer.Dispose();
			}
			if (messageHandler != null) {
				messageHandler = null;
			}
			if (tcpWriter != null) {
				tcpWriter.Close();
				tcpWriter = null;
			}
			if (tcpReader != null) {
				tcpReader.Close();
				tcpReader = null;
			}
			if (tcpClient != null) {
				tcpClient.Close();
				tcpClient = null;
			}
//			Disable();
		}

		private void printPonentialFailInfo(String addTo){
			Print(DateTime.Now + " " + DateTime.Now.Millisecond + " "  + addTo + " size of dictionary: " + orderMap.Count);
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
