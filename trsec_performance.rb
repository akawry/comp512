#!/usr/bin/env ruby

# Print help
def print_usage()
  puts "Usage : ./performance.rb host port nbloop "
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 3 )
  print_usage
end

host = ARGV.shift
port = ARGV.shift.to_i
nbloop = ARGV.shift.to_i
trsec = []
trsec << 5 << 10 << 15 <<20 << 30 << 40 << 50 << 75 << 100 << 150 << 200 << 350 <<  500 << 750 << 1000 << 5000 << 10000 << 50000 << 0
smallOneRM = []
smallMultipleRM = []
bigOneRM = []
bigMultipleRM = []

trsec.each do  |i|
STDOUT << i<< "\n"
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:1")
smallOneRM <<  ( exec.readlines.last.to_i / nbloop)
STDOUT << smallOneRM.last << "\n"

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:2")
smallMultipleRM  << ( exec.readlines.last.to_i / nbloop )
STDOUT << smallMultipleRM.last<< "\n"

exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:3")
bigOneRM <<  ( exec.readlines.last.to_i / nbloop )
STDOUT << bigOneRM.last<< "\n"
  
exec = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{i}:4")
bigMultipleRM <<  ( exec.readlines.last.to_i / nbloop )
STDOUT <<  bigMultipleRM.last<< "\n"
end
