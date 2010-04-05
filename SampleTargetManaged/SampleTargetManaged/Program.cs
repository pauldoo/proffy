using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.Threading;
using System.Security.AccessControl;

namespace SampleTargetManaged
{
    class Program
    {
        private static int fib(int n)
        {
            switch (n) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                default:
                    return fib(n-2) + fib(n-1);
            }
        }

        static void Main(string[] args)
        {
            const string pathToProffyExecutable = "..\\..\\..\\..\\dist\\bin64\\Proffy64.exe";
            const string outputFolder = "..\\..\\..\\..\\dist";
            const double delayBetweenSamplesInSeconds = 0.1;
            const bool profileTheProfiler = false;

            System.Console.Out.WriteLine(System.IO.Directory.GetCurrentDirectory());


            string guid = System.Guid.NewGuid().ToString();
            string semaphoreStartName = "Proffy_" + guid + "_start";
            string semaphoreStopName = "Proffy_" + guid + "_stop";

            using (Semaphore startFlag = new Semaphore(0, 10, semaphoreStartName))
            {
                using (Semaphore stopFlag = new Semaphore(0, 10, semaphoreStopName))
                {
                    StringBuilder argumentsBuilder = new StringBuilder();
                    argumentsBuilder.Append(Process.GetCurrentProcess().Id);
                    argumentsBuilder.Append(" " + outputFolder);
                    argumentsBuilder.Append(" " + semaphoreStartName);
                    argumentsBuilder.Append(" " + semaphoreStopName);
                    argumentsBuilder.Append(" " + delayBetweenSamplesInSeconds);
                    argumentsBuilder.Append(" " + (profileTheProfiler ? "1" : "0"));
                    System.Console.Out.WriteLine(argumentsBuilder.ToString());

                    using (Process process = Process.Start(pathToProffyExecutable, argumentsBuilder.ToString()))
                    {
                        System.Console.Out.WriteLine("Waiting for proffy to start..");
                        startFlag.WaitOne();

                        System.Console.Out.WriteLine("Spinning..");
                        long start = System.DateTime.UtcNow.ToFileTimeUtc();
                        long ticksPerSecond = 10000000; // ToFileTimeUtc() returns in units of 100-nanoseconds, ie, 10^7 ticks per second.
                        while (System.DateTime.UtcNow.ToFileTimeUtc() - start < 10 * ticksPerSecond) {
                            System.Console.Out.Write(".");
                            fib(10);
                        }
                        System.Console.Out.WriteLine();
                        System.Console.Out.WriteLine("Spun for 10 seconds..");

                        stopFlag.Release();
                  
                        System.Console.Out.WriteLine("Waiting for proffy to stop..");
                        process.WaitForExit();
                        System.Console.Out.WriteLine("Done..");
                    }
                }
            }
        }
    }
}
