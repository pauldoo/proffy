#!/usr/bin/env ruby

# Most that I have ever been able to fit on a dvd:
# 4484
# Smallest number that still would not fit:
# 4493

def Toggle(files, total, desc)
    files = files.clone();
    if files.size == 0
        printf("%05i", total / 1024);
        print ":" + desc + "\n";
    else
        file = files.pop;
        #size = File.stat(file).size;
        size = `du -k "#{file}"`;
        size = Integer(size[/\d+/]);
        Toggle(files, total, desc);
        Toggle(files, total + size, " \"" + file + "\"" + desc);
    end
end
    
Toggle(ARGV, 0, "");

