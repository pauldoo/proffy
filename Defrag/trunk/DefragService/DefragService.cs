using System;
using System.Diagnostics;
using System.ServiceProcess;
using System.Threading;
using System.ComponentModel;
using System.IO;

namespace DefragService
{
    class DefragService : ServiceBase
    {
        /// <summary>
        /// Public Constructor for WindowsService.
        /// - Put all of your Initialization code here.
        /// </summary>
        public DefragService()
        {
            this.ServiceName = "Paul's Defrag Service";
            this.EventLog.Log = "Application";

            // These Flags set whether or not to handle that specific
            //  type of event. Set to true if you need it, false otherwise.
            this.AutoLog = true;
            this.CanShutdown = true;
            this.CanStop = true;
        }

        /// <summary>
        /// The Main Thread: This is where your Service is Run.
        /// </summary>
        static void Main()
        {
            ServiceBase.Run(new DefragService());
        }

        /// <summary>
        /// OnStart(): Put startup code here
        ///  - Start threads, get inital data, etc.
        /// </summary>
        /// <param name="args"></param>
        protected override void OnStart(string[] args)
        {
            base.OnStart(args);
            m_running = true;
            m_worker = new Thread(new ThreadStart(MainLoop));
            m_worker.Start();
        }

        /// <summary>
        /// OnStop(): Put your stop code here
        /// - Stop threads, set final data, etc.
        /// </summary>
        protected override void OnStop()
        {
            m_running = false;
            m_worker.Interrupt();
            m_worker.Join();
            m_worker = null;
            base.OnStop();
        }

        /// <summary>
        /// OnShutdown(): Called when the System is shutting down
        /// - Put code here when you need special handling
        ///   of code that deals with a system shutdown, such
        ///   as saving special data before shutdown.
        /// </summary>
        protected override void OnShutdown()
        {
            m_worker.Interrupt();
            m_worker.Join();
            m_worker = null;
            base.OnShutdown();
        }

        private void MainLoop()
        {
            Defrag.IFileSystem fileSystem = new Defrag.Win32FileSystem("C:");
            Defrag.Optimizer optimizer = new Defrag.Optimizer(fileSystem);

            Defrag.Win32ChangeWatcher watcher = new Defrag.Win32ChangeWatcher("C:\\");

            // Create the source, if it does not already exist.
            EventLog.DeleteEventSource("Defrag Service");
            if (!EventLog.SourceExists("Defrag Service"))
            {
                EventLog.CreateEventSource("Defrag Service", "Defrag Log");
            }

            // Create an EventLog instance and assign its source.
            EventLog event_log = new EventLog();
            event_log.Source = "Defrag Service";
            event_log.Log = "Defrag Log";
            event_log.WriteEntry("Hello");

            while (m_running)
            {
                String path = watcher.NextFile();
                if (path != null)
                {
                    StringWriter log = new StringWriter();
                    EventLogEntryType type = EventLogEntryType.Error;
                    try
                    {
                        optimizer.DefragFile(path, log);
                        type = EventLogEntryType.Information;
                    }
                    catch (Exception ex)
                    {
                        log.WriteLine(ex.Message);
                        log.WriteLine(ex.StackTrace);
                    }
                    finally
                    {
                        log.Flush();
                        event_log.WriteEntry(log.ToString(), type);
                    }
                }
                else
                {
                    Thread.Sleep(500);
                }
            }
        }

        private bool m_running = false;
        private Thread m_worker = null;
    }
}