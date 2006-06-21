using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Collections;
using System.Runtime.InteropServices;
using System.IO;

namespace Defrag
{
    public class Win32ChangeWatcher
    {
        public Win32ChangeWatcher(string volume)
        {
            watcher.Path = volume;
            watcher.IncludeSubdirectories = true;
            watcher.NotifyFilter = NotifyFilters.LastWrite;
            watcher.Changed += new FileSystemEventHandler(OnWrite);
            watcher.Created += new FileSystemEventHandler(OnWrite);
            watcher.EnableRaisingEvents = true;
        }

        private void OnWrite(object source, FileSystemEventArgs e)
        {
            lock (this)
            {
                pending.Remove(e.FullPath);
                pending.Add(e.FullPath);
            }
        }

        public string NextFile()
        {
            lock (this) {
                if (pending.Count == 0)
                {
                    return null;
                }
                string path = pending[0];
                pending.RemoveAt(0);
                return path;
            }
        }

        FileSystemWatcher watcher = new FileSystemWatcher();
        List<string> pending = new List<string>();
    }
}
