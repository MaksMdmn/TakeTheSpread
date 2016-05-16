#region Using declarations
using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Xml.Serialization;
using NinjaTrader.Cbi;
using NinjaTrader.Data;
using NinjaTrader.Indicator;
using NinjaTrader.Gui.Chart;
using NinjaTrader.Strategy;
#endregion

// This namespace holds all strategies and is required. Do not change it.
namespace NinjaTrader.Strategy
{
    /// <summary>
    /// Enter the description of your strategy here
    /// </summary>
    [Description("Enter the description of your strategy here")]
    public class MyCustomStrategy : Strategy
    {
		private bool doOnce = true;
        #region Variables
        // Wizard generated variables
        private int myInput0 = 1; // Default setting for MyInput0
        // User defined variables (add any user defined variables below)
        #endregion

        /// <summary>
        /// This method is used to configure the strategy and is called once before any strategy method is called.
        /// </summary>
        protected override void Initialize()
        {
            CalculateOnBarClose = true;
			Enabled = true;
			Unmanaged = true;
        }

        /// <summary>
        /// Called on each bar update event (incoming tick)
        /// </summary>
        protected override void OnBarUpdate()
        {

        }


		protected override void OnMarketData(MarketDataEventArgs e)
		{
			if(doOnce){
				SubmitOrder(0,OrderAction.Buy,OrderType.Limit,3,47.73,0,"","");
//				SubmitOrder(0,OrderAction.Sell,OrderType.Market,5,0,0,"","");
				doOnce = false;
			}
		}

		protected override void OnOrderUpdate(IOrder order)
		{
			Print("on ord upd: "  + order.ToString());
		}

		protected override void OnExecution(IExecution execution)
		{
			Print("on exec upd execution: " + execution.ToString());
			Print("on exec upd order: " + execution.Order.ToString());
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

//**NT** Disabling NinjaScript strategy 'MyCustomStrategy/8b78cfb4a427486890abd9de700ea669'
//**NT** Enabling NinjaScript strategy 'MyCustomStrategy/8b78cfb4a427486890abd9de700ea669' : On starting a real-time strategy - StrategySync=WaitUntilFlat SyncAccountPosition=False EntryHandling=AllEntries EntriesPerDirection=1 StopTargetHandling=PerEntryExecution ErrorHandling=StopStrategyCancelOrdersClosePositions ExitOnClose=True/ triggering 30 before close Set order quantity by=Strategy ConnectionLossHandling=KeepRunning DisconnectDelaySeconds=10 CancelEntryOrdersOnDisable=False CancelExitOrdersOnDisable=True CalculateOnBarClose=True MaxRestarts=4 in 5 minutes
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=PendingSubmit Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=0 Fill price=0 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=Accepted Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=0 Fill price=0 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=Working Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=0 Fill price=0 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=PartFilled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=1 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on exec upd execution: Execution='b5152bed2ae841908a307e53eb243c79' Instrument='CL 06-16' Account='Sim101' Name='Buy' Exchange=Default Price=47,73 Quantity=1 Market position=Long Commission=0 Order='471e672476c64f589f8f9dfef62f9593' Time='16.05.2016 21:50:21'
//on exec upd order: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=PartFilled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=1 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=PartFilled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=2 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on exec upd execution: Execution='3505d37e745d4fd6a687fe1452e9bf07' Instrument='CL 06-16' Account='Sim101' Name='Buy' Exchange=Default Price=47,73 Quantity=1 Market position=Long Commission=0 Order='471e672476c64f589f8f9dfef62f9593' Time='16.05.2016 21:50:21'
//on exec upd order: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=PartFilled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=2 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on ord upd: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=Filled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=3 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//on exec upd execution: Execution='641f27637f3040b09491f5c40e803d53' Instrument='CL 06-16' Account='Sim101' Name='Buy' Exchange=Default Price=47,73 Quantity=1 Market position=Long Commission=0 Order='471e672476c64f589f8f9dfef62f9593' Time='16.05.2016 21:50:21'
//on exec upd order: Order='471e672476c64f589f8f9dfef62f9593/Sim101' Name='Buy' State=Filled Instrument='CL 06-16' Action=Buy Limit price=47,73 Stop price=0 Quantity=3 Type=Limit Tif=Gtc OverFill=False Oco='' Filled=3 Fill price=47,73 Token='471e672476c64f589f8f9dfef62f9593' Gtd='01.01.0001 0:00:00'
//**NT** Disabling NinjaScript strategy 'MyCustomStrategy/8b78cfb4a427486890abd9de700ea669'
