#!/usr/bin/env ruby

require 'gruff'

# Print help
def print_usage()
  puts "Usage : ./graph.rb filename"
  exit 1
end

# Read parameters ARGV
if (ARGV.size != 1 )
  print_usage
end

filename = ARGV.shift
x = []

smallOneRM = []
smallMultipleRM = []
bigOneRM = []
bigMultipleRM = []

i = 0
STDIN.readlines.each do |l|
  if i % 5 == 0 
    x << l
  elsif i %  5 == 1
    smallOneRM << l.to_i
  elsif i % 5 == 2
    smallMultipleRM  << l.to_i 
  elsif i % 5 == 3
    bigOneRM <<  l.to_i
  elsif i % 5 == 4
    bigMultipleRM << l.to_i
  end
  i = i + 1
end


# Generate graph
g = Gruff::Line.new(600)
g.title = "Performance"

i = 0
x.each do |l|
  g.labels.store(i, l.to_s)
  i = i + 1
end

g.data("Small One", smallOneRM)
g.data("Small Multiple", smallMultipleRM)
g.data("Big One", bigOneRM)
g.data("Big Multiple", bigMultipleRM)
g.write(filename)
