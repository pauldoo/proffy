using System;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    interface IFreeSpace
    {
        ulong TotalFreeSpace();

        ulong LargestFreeSpan();

        ulong FindFreeSpan(ulong length);
    }
}
