using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Defrag
{
    public interface IFileSystem
    {
        /// <summary>
        /// Fetch a bitmap representing the allocated blocks on the volume.
        /// </summary>
        /// <returns>A BitArray containing one element per block, true values indicate a block in use.</returns>
        BitArray GetVolumeMap();

        /// <summary>
        /// Fetch the layout of a given file on the volume.
        /// </summary>
        /// <param name="path">The file to fetch layout information for.</param>
        /// <returns>Something quite shite.</returns>
        Array GetFileMap(string path);

        /// <summary>
        /// Move a chunk of file to someplace new.
        /// </summary>
        /// <param name="path">The file to move.</param>
        /// <param name="VCN">The virtual cluster number (file offset) at which to begin the move.</param>
        /// <param name="LCN">The logical cluster number (volume offset) to move the chunk to.</param>
        /// <param name="count">The number of clusters to move.</param>
        void MoveFile(string path, ulong VCN, ulong LCN, uint count);
    }
}
