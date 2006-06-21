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
            Defrag.IFileSystem fileSystem = new Defrag.Win32FileSystem("D:");
            Defrag.Optimizer optimizer = new Defrag.Optimizer(fileSystem);

            Defrag.Win32ChangeWatcher watcher = new Defrag.Win32ChangeWatcher("D:\\");
            while (true)
            {
                String path = watcher.NextFile();
                Thread.Sleep(100);
                if (path != null)
                {
                    System.Console.WriteLine(path);
                    try
                    {
                        optimizer.DefragFile(path);
                        System.Console.WriteLine(" Defragged");
                    }
                    catch (Exception e)
                    {
                        System.Console.WriteLine(" Failed!");
                    }
                }
            }
        }
    }
}
