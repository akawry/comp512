#!/usr/bin/env ruby

require 'gruff'

# Print help
def print_usage()
  puts "Usage : ./performance.rb host port nbloop filename"
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 4 )
  print_usage
end

host = ARGV.shift
port = ARGV.shift.to_i
nbloop = ARGV.shift.to_i
filename = ARGV.shift
trsec = []
trsec << 5 << 10 << 15 <<20 << 30 << 40 << 50 << 75 << 100 << 150 << 200 << 350 <<  500 << 750 << 1000 << 5000 << 10000 << 50000 << 0
smallOneRM = []
smallMultipleRM = []
bigOneRM = []
bigMultipleRM = []

trsec.each do  |i|
# Launch client
# Collect data (time+transaction) from stdin
# Calculate mean ..etc..
puts "Starting performance test for throughput #{i} tr/sec"
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:1")
smallOneRM <<  ( exec.readlines.last.to_i / nbloop)
puts smallOneRM.last

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:2")
smallMultipleRM  << ( exec.readlines.last.to_i / nbloop )
puts smallMultipleRM.last

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:3")
bigOneRM <<  ( exec.readlines.last.to_i / nbloop )
puts bigOneRM.last
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:4")
bigMultipleRM <<  ( exec.readlines.last.to_i / nbloop )
puts bigMultipleRM.last
end


# Generate graph
g = Gruff::Line.new(900)
g.title = "Performance"
i = 0
trsec.each do |l|
  g.labels.store(i, l.to_s)
  i = i + 1
end
g.data("Small One", smallOneRM)
g.data("Small Multiple", smallMultipleRM)
g.data("Big One", bigOneRM)
g.data("Big Multiple", bigMultipleRM)
g.write(filename)
