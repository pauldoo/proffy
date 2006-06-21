using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    public class Win32FileSystem : IFileSystem
    {
        public Win32FileSystem(string volume)
        {
            this.volume = volume;
        }

        public BitArray GetVolumeMap()
        {
            return IOWrapper.GetVolumeMap(volume);
        }

        public Array GetFileMap(string path)
        {
            return IOWrapper.GetFileMap(path);
        }

        public void MoveFile(string path, ulong VCN, ulong LCN, uint count)
        {
            IOWrapper.MoveFile(volume, path, (long)VCN, (long)LCN, (int)count);
        }

        private readonly string volume;
    }
}
