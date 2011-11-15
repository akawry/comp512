#!/usr/bin/env ruby

# Print help
def print_usage()
  puts "Usage : ./performance.rb host port"
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 2 )
  print_usage
end

host = ARGV.shift
port = ARGV.shift.to_i
nbloop = 100
trsec=  0

nbclient = []
nbclient <<  1 << 3 << 8 << 20

nbclient.each do  |i|
  puts i

  (1..2).each do |type|
    eachClient = []
    # Run it 
    threads = []
    i.times do |c|
      threads << Thread.new { Thread.current[:responsetime] = IO.popen("./launch_client_automatic.sh rmi #{host}:#{port} #{nbloop}:#{trsec}:#{type}").readlines.last.to_i 
      }
    end

    # Wait for the thread results
    threads.each do |t|
      t.join
      eachClient <<  t[:responsetime]
    end

    # Calculate the average
    average = 0
    eachClient.each do |c|
      average += c
    end
    average /= i
    puts average
  end
end
