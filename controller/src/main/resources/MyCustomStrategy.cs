#region Using declarations
using System;

using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using System.Web;
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

// This namespace holds all indicators and is required. Do not change it.
namespace NinjaTrader.Indicator
{
    /// <summary>
    /// Enter the description of your new custom indicator here
    /// </summary>
    [Description("Enter the description of your new custom indicator here")]
    public class sClient : Indicator
    {
        #region Variables
        // Wizard generated variables
            private int myInput0 = 1; // Default setting for MyInput0
        // User defined variables (add any user defined variables below)
        #endregion
		
		private System.Timers.Timer timer;
		private StreamWriter tcpWriter;
		private TcpClient tcpClient;
		private Thread thrMessaging;
		private Thread thrSendering;
		private StreamReader tcpReader;
		private bool isCon;
		private String prevMessage = "";
		
		private System.IO.StreamWriter fileWriter;
		
		

        /// <summary>
        /// This method is used to configure the indicator and is called once before any bar data is loaded.
        /// </summary>
        protected override void Initialize()
        {
            Overlay				= true;
			CalculateOnBarClose = true;
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
		}
		
		private void ReceiveMessages()
		{
			while (isCon)
			{		
				try{
					String con = tcpReader.ReadLine();
					processMessage(con);
					
					if(con == "GJ"){
						Print("Good bye blue sky");
						correctClosingAndAbortCon();
						
					}
				}catch (Exception e4){
					correctClosingAndAbortCon();
					
					Print("receiveMessages exception: " + e4.ToString());
				}
			}
		}

		private void processMessage(String p)
		{
			Print("got this: " + p);
		}

		
		//				p = HttpUtility.UrlEncode(p, System.Text.Encoding.UTF8);
		private void SendMessages(String message)
		{
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
			try{
				if (e.MarketDataType == MarketDataType.Last)
				{
					String tickStr = "t: " + e.Time + " "
					+ "ask: " + GetCurrentAsk() + " " 
					+ "askVol: " + GetCurrentAskVolume() + " "
					+ "bid: " + GetCurrentBid() + " " 
					+ "bidVol: " + GetCurrentBidVolume() + 
					Environment.NewLine;
					
					if (prevMessage!=tickStr){
						SendMessages(tickStr);
						prevMessage = tickStr;
					}
					
				}	
			}catch(Exception e3){
				correctClosingAndAbortCon();
				Print("onMarketData exception: " + e3.ToString());
			}
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
		}
		
        #region Properties
        [Browsable(false)]	// this line prevents the data series from being displayed in the indicator properties dialog, do not remove
        [XmlIgnore()]		// this line ensures that the indicator can be saved/recovered as part of a chart template, do not remove
        public DataSeries Plot0
        {
            get { return Values[0]; }
        }

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

#region NinjaScript generated code. Neither change nor remove.
// This namespace holds all indicators and is required. Do not change it.
namespace NinjaTrader.Indicator
{
    public partial class Indicator : IndicatorBase
    {
        private sClient[] cachesClient = null;

        private static sClient checksClient = new sClient();

        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        public sClient sClient(int myInput0)
        {
            return sClient(Input, myInput0);
        }

        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        public sClient sClient(Data.IDataSeries input, int myInput0)
        {
            if (cachesClient != null)
                for (int idx = 0; idx < cachesClient.Length; idx++)
                    if (cachesClient[idx].MyInput0 == myInput0 && cachesClient[idx].EqualsInput(input))
                        return cachesClient[idx];

            lock (checksClient)
            {
                checksClient.MyInput0 = myInput0;
                myInput0 = checksClient.MyInput0;

                if (cachesClient != null)
                    for (int idx = 0; idx < cachesClient.Length; idx++)
                        if (cachesClient[idx].MyInput0 == myInput0 && cachesClient[idx].EqualsInput(input))
                            return cachesClient[idx];

                sClient indicator = new sClient();
                indicator.BarsRequired = BarsRequired;
                indicator.CalculateOnBarClose = CalculateOnBarClose;
#if NT7
                indicator.ForceMaximumBarsLookBack256 = ForceMaximumBarsLookBack256;
                indicator.MaximumBarsLookBack = MaximumBarsLookBack;
#endif
                indicator.Input = input;
                indicator.MyInput0 = myInput0;
                Indicators.Add(indicator);
                indicator.SetUp();

                sClient[] tmp = new sClient[cachesClient == null ? 1 : cachesClient.Length + 1];
                if (cachesClient != null)
                    cachesClient.CopyTo(tmp, 0);
                tmp[tmp.Length - 1] = indicator;
                cachesClient = tmp;
                return indicator;
            }
        }
    }
}

// This namespace holds all market analyzer column definitions and is required. Do not change it.
namespace NinjaTrader.MarketAnalyzer
{
    public partial class Column : ColumnBase
    {
        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        [Gui.Design.WizardCondition("Indicator")]
        public Indicator.sClient sClient(int myInput0)
        {
            return _indicator.sClient(Input, myInput0);
        }

        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        public Indicator.sClient sClient(Data.IDataSeries input, int myInput0)
        {
            return _indicator.sClient(input, myInput0);
        }
    }
}

// This namespace holds all strategies and is required. Do not change it.
namespace NinjaTrader.Strategy
{
    public partial class Strategy : StrategyBase
    {
        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        [Gui.Design.WizardCondition("Indicator")]
        public Indicator.sClient sClient(int myInput0)
        {
            return _indicator.sClient(Input, myInput0);
        }

        /// <summary>
        /// Enter the description of your new custom indicator here
        /// </summary>
        /// <returns></returns>
        public Indicator.sClient sClient(Data.IDataSeries input, int myInput0)
        {
            if (InInitialize && input == null)
                throw new ArgumentException("You only can access an indicator with the default input/bar series from within the 'Initialize()' method");

            return _indicator.sClient(input, myInput0);
        }
    }
}
#endregion
