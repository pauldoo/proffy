using System;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    public interface IFreeSpace
    {
        ulong TotalFreeSpace();

        ulong LargestFreeSpan();

        ulong FindFreeSpan(ulong length);

        /// <summary>
        /// Update the free space map with info about a new allocation.
        /// </summary>
        /// <param name="offset"></param>
        /// <param name="length"></param>
        /// <returns>A new IFreeSpace which is the new top level node replacing this.</returns>
        IFreeSpace Allocate(ulong offset, ulong length);
    }
}
