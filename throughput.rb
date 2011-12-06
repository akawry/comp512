#!/usr/bin/env ruby

# Print help
def print_usage()
  puts "Usage : ./throughput.rb host port"
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 2 )
  print_usage
end

host = ARGV.shift
port = ARGV.shift.to_i
nbloop = 100
trsec =  0

nbclient = []
nbclient  << 10 << 10 << 10#<< 2 <<   3 << 5 << 10 << 50 << 100
nbclient.each do  |i|
  puts i
  (3..3).each do |type|
  puts IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i}:#{nbloop}:#{trsec}:#{type}").readlines.last.to_i
  #puts IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{i}:#{nbloop}:#{trsec}:#{type}").readlines
  end 
end
