using System;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    class FileLayout
    {
        public FileLayout(Array map)
        {
            this.map = map;
        }

        public uint Length()
        {
            return (uint)(UInt64)map.GetValue(map.Length / 2 - 1, 0); 
        }

        public uint Fragments()
        {
            uint fragments = 1;
            ulong last = 0;
            for (int i = 1; i < map.Length / 2; i++)
            {
                if (((UInt64)map.GetValue(i - 1, 0) + (UInt64)map.GetValue(i - 1, 1) - last) != (UInt64)map.GetValue(i, 1))
                {
                    fragments++;
                }
                last = (UInt64)map.GetValue(i - 1, 0);
            }
            return fragments;
        }

        private readonly Array map;
    }
}
