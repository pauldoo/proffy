using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Collections;
using System.Runtime.InteropServices;
using System.IO;
using System.Threading;

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
            if (new DirectoryInfo(e.FullPath).Exists)
            {
                return;
            }
            lock (this)
            {
                DateTime last_activity_time;
                IList<string> filenames;
                if (filename_to_last_activity_time.TryGetValue(e.FullPath, out last_activity_time))
                {
                    last_activity_time_to_filenames.TryGetValue(last_activity_time, out filenames);
                    filenames.Remove(e.FullPath);
                    if (filenames.Count == 0)
                    {
                        last_activity_time_to_filenames.Remove(last_activity_time);
                    }
                }
                filename_to_last_activity_time.Remove(e.FullPath);

                last_activity_time = DateTime.UtcNow;
                filename_to_last_activity_time.Add(e.FullPath, last_activity_time);
                if (last_activity_time_to_filenames.TryGetValue(last_activity_time, out filenames))
                {
                    filenames.Add(e.FullPath);
                }
                else
                {
                    filenames = new List<string>();
                    filenames.Add(e.FullPath);
                    last_activity_time_to_filenames.Add(last_activity_time, filenames);
                }
            }
        }

        public string NextFile()
        {
            lock (this) {
                if (last_activity_time_to_filenames.Count == 0)
                {
                    return null;
                }
                SortedDictionary<DateTime, IList<string>>.Enumerator enumerator = last_activity_time_to_filenames.GetEnumerator();
                if (!enumerator.MoveNext())
                {
                    return null;
                }
                DateTime oldest_activity = enumerator.Current.Key;
                DateTime time_to_sleep_until = oldest_activity + new TimeSpan(s_delay_after_activity * TimeSpan.TicksPerMillisecond);
                DateTime time_now = DateTime.UtcNow;
                if (time_to_sleep_until > time_now)
                {
                    return null;
                }
                IList<string> filenames = enumerator.Current.Value;
                string path = filenames[0];
                filenames.RemoveAt(0);
                if (filenames.Count == 0)
                {
                    last_activity_time_to_filenames.Remove(oldest_activity);
                }
                filename_to_last_activity_time.Remove(path);
                return path;
            }
        }

        private FileSystemWatcher watcher = new FileSystemWatcher();
        private IDictionary<string, DateTime> filename_to_last_activity_time = new SortedDictionary<string, DateTime>();
        private SortedDictionary<DateTime, IList<string>> last_activity_time_to_filenames = new SortedDictionary<DateTime, IList<string>>();
        private static readonly int s_delay_after_activity = 3000;
    }

}
