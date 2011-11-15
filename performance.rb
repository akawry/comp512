#!/usr/bin/env ruby

require 'gruff'

# Print help
def print_usage()
  puts "Usage : ./performance.rb host port trsec filename"
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 4 )
  print_usage
end

host = ARGV.shift
port = ARGV.shift.to_i
trsec = ARGV.shift.to_i
filename = ARGV.shift
nbloop = []
nbloop << 5 << 15 << 30 << 40 <<  50 << 60 << 100
smallOneRM = [0]
smallMultipleRM = [0]
bigOneRM = [0]
bigMultipleRM = [0]

nbloop.each do  |i|
# Launch client
# Collect data (time+transaction) from stdin
# Calculate mean ..etc..
puts "Starting performance test for loop of size #{i}"
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i*3}:#{trsec*3}:1")
smallOneRM <<  ( exec.readlines.last.to_i / i)
puts smallOneRM.last

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i*3}:#{trsec*3}:2")
smallMultipleRM  << ( exec.readlines.last.to_i / i )
puts smallMultipleRM.last

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i}:#{trsec}:3")
bigOneRM <<  ( exec.readlines.last.to_i / i )
puts bigOneRM.last
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i}:#{trsec}:4")
bigMultipleRM <<  ( exec.readlines.last.to_i / i )
puts bigMultipleRM.last
end


# Generate graph
g = Gruff::Line.new(600)
g.title = "Performance"
i = 0
nbloop.each do |l|
  g.labels.store(i, l.to_s)
  i = i + 1
end
g.data("Small One", smallOneRM)
g.data("Small Multiple", smallMultipleRM)
g.data("Big One", bigOneRM)
g.data("Big Multiple", bigMultipleRM)
g.write(filename)
