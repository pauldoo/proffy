using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using NUnit.Gui;

namespace DefragUnitTests
{
    class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            NUnit.Gui.AppEntry.Main(args);
        }
    }
}
