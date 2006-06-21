using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    public class Optimizer
    {
        public Optimizer(IFileSystem fileSystem)
        {
            this.fileSystem = fileSystem;
        }

        public void DefragFile(string path)
        {
            FileLayout layout = new FileLayout(fileSystem.GetFileMap(path));

            if (free_space_map == null)
            {
                UpdateFreeSpaceMap();
            }

            try
            {
                DefragFile(path, layout);
            }
            catch (Exception)
            {
                free_space_map = null;
            }
        }

        private void UpdateFreeSpaceMap()
        {
            DateTime a = DateTime.Now;
            BitArray bitmap = fileSystem.GetVolumeMap();
            DateTime b = DateTime.Now;
            free_space_map = new FreeSpaceTree(bitmap);
            DateTime c = DateTime.Now;
            //System.Console.WriteLine("Fetching bitmap took: " + (b.Subtract(a)).TotalMilliseconds + "ms");
            //System.Console.WriteLine("Building free tree took: " + (c.Subtract(b)).TotalMilliseconds + "ms");
        }
        

        private void DefragFile(string path, FileLayout layout)
        {
            if (layout.Fragments() <= 1)
            {
                return;
            }

            uint file_length = layout.Length();
            ulong free_space_position = free_space_map.FindFreeSpan(file_length);
            if (free_space_position == UInt64.MaxValue)
            {
                return;
            }
            fileSystem.MoveFile(path, 0, free_space_position, file_length);
        }

        private readonly IFileSystem fileSystem;

        private IFreeSpace free_space_map;
    }
}
