#!/usr/bin/ruby -w

repository = ARGV[0]

path = "~/#{repository}"
version = `svnlook youngest #{path}`.strip()
($?.to_i != 0) and raise "svnlook failed"

previous_version = `cat ~/backups/#{repository}.lastid`.strip()

if version == previous_version
    puts "Previous backup is still OK."
else
    command = "(svnadmin dump #{path} --quiet | bzip2 -c9 > ~/backups/#{repository}.dump.bz2.new) && mv ~/backups/#{repository}.dump.bz2.new ~/backups/#{repository}.dump.bz2"

    puts "Executing: #{command}\n"
    system "#{command}" or raise "command failed"
    
    puts "Emailing.\n"
    system "~/bin/email_backup.sh #{repository}" or raise "email failed"
    system "echo '#{version}' > ~/backups/#{repository}.lastid"
end


