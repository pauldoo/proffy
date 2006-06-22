using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace DefragBinary
{
    public class Program
    {
        public static void Main(string[] args)
        {
            Defrag.IFileSystem fileSystem = new Defrag.Win32FileSystem("C:");
            Defrag.Optimizer optimizer = new Defrag.Optimizer(fileSystem);

            Defrag.Win32ChangeWatcher watcher = new Defrag.Win32ChangeWatcher("C:\\");
            System.IO.TextWriter log = new System.IO.StreamWriter(System.Console.OpenStandardOutput());

            while (true)
            {
                String path = watcher.NextFile();
                Thread.Sleep(100);
                if (path != null)
                {
                    try
                    {
                        optimizer.DefragFile(path, log);
                    }
                    catch (Exception)
                    {
                        log.WriteLine("* * Failed! * *");
                    }
                    finally
                    {
                        log.WriteLine();
                        log.Flush();
                    }

                }
            }
        }
    }
}
