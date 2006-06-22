using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace Defrag
{
    public class Optimizer
    {
        public Optimizer(IFileSystem fileSystem)
        {
            this.fileSystem = fileSystem;
        }

        public void DefragFile(string path, TextWriter log)
        {
            log.WriteLine("Looking at: " + path);
            FileLayout layout = new FileLayout(fileSystem.GetFileMap(path));

            if (free_space_map == null)
            {
                log.WriteLine("Updating free space map");
                UpdateFreeSpaceMap();
            }

            try
            {
                DefragFile(path, layout, log);
            }
            catch (Exception)
            {
                free_space_map = null;
                throw;
            }
        }

        private void UpdateFreeSpaceMap()
        {
            BitArray bitmap = fileSystem.GetVolumeMap();
            free_space_map = new FreeSpaceTree(bitmap);
        }
        

        private void DefragFile(string path, FileLayout layout, TextWriter log)
        {
            if (layout.Fragments() <= 1)
            {
                log.WriteLine("Skipped, does not require defrag.");
                return;
            }

            uint file_length = layout.Length();
            ulong free_space_position = free_space_map.FindFreeSpan(file_length);
            if (free_space_position == UInt64.MaxValue)
            {
                log.WriteLine("Skipped, could not find a big enough span of free space.");
                return;
            }
            fileSystem.MoveFile(path, 0, free_space_position, file_length);
            log.WriteLine("Defrag successfull.");
        }

        private readonly IFileSystem fileSystem;

        private IFreeSpace free_space_map;
    }
}
