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
        static void Main(string[] args)
        {
            const string pathToProffyExecutable = "c:\\Proffy_586\\bin32\\Proffy32.exe";
            const string outputXmlFile = "c:\\Proffy_586\\result.xml";
            const string outputDotFile = "c:\\Proffy_586\\result.dot";
            const double delayBetweenSamplesInSeconds = 0.1;
            const bool profileTheProfiler = false;

            SemaphoreSecurity security = new SemaphoreSecurity();
            Semaphore startFlag = new Semaphore(0, 10);
            Semaphore stopFlag = new Semaphore(0, 10);

            StringBuilder argumentsBuilder = new StringBuilder();
            argumentsBuilder.Append(Process.GetCurrentProcess().Id);
            argumentsBuilder.Append(" " + outputXmlFile);
            argumentsBuilder.Append(" " + outputDotFile);
            argumentsBuilder.Append(" " + startFlag.SafeWaitHandle.DangerousGetHandle());
            argumentsBuilder.Append(" " + stopFlag.SafeWaitHandle.DangerousGetHandle());
            argumentsBuilder.Append(" " + delayBetweenSamplesInSeconds); 
            argumentsBuilder.Append(" " + (profileTheProfiler ? "1" : "0"));

            System.Console.Out.WriteLine(argumentsBuilder.ToString());
            using (Process process = Process.Start(pathToProffyExecutable, argumentsBuilder.ToString())) {
                process.WaitForExit();
            }
        }
    }
}
