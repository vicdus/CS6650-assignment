import numpy as np
#import matplotlib.pyplot as plt
import subprocess




fns = subprocess.check_output("ls log/", shell=True).decode("utf-8")
fileNames = [v for v in fns.split("\n") if len(v) > 0]

print("\t\t\t".join([str(v) for v in ["Type", "Client", "Server", "DB", "t", "p50", "p99"]]))
for name in fileNames:
	client, server, db = name.split(".")[0].split("-")[1:]
	
	content = open("./log/" + name, "r").read()
	latency = [int(v.split(" ")[1]) for v in content.split("\n") if len(v) > 0]
	wallTime = latency[-1]
	latency = latency[:-1]

	t = int(len(latency) / (wallTime / 1000))
	p50 = np.percentile(latency, 50)
	p99 = np.percentile(latency, 99)
	print("\t\t\t".join([str(v) for v in [name.split("-")[0], client, server, db, t, p50, p99]]))




#
#get_latency = []
#get_starttime = []
#post_latency = []
#post_starttime = []
#wall_latency = None
#get_err = 0
#post_err = 0
#line_count = 0
#for line in f:
#	line_count += 1
#	pair = line[:-1].split(" ")
#	name, time = pair[0], pair[1]
#	if name == "GET":
#		if time == "FAILURE":
#			get_err += 1
#		else:
#			get_latency.append(int(time))
#			get_starttime.append(int(pair[2]))
#	elif name == "POST":
#		if time == "FAILURE":
#			post_err += 1
#		else:
#			post_latency.append(int(time))
#			post_starttime.append(int(pair[2]))
#	else:
#		wall_latency = int(time)
#
#
#print(thread, "threads, 100 iterations")
#
#print("50 percentile POST:", np.percentile(post_latency, 50), "ms")
#print("95 percentile POST:", np.percentile(post_latency, 95), "ms")
#print("99 percentile: POST", np.percentile(post_latency, 99), "ms")
#print("50 percentile GET:", np.percentile(get_latency, 50), "ms")
#print("95 percentile GET:", np.percentile(get_latency, 95), "ms")
#print("99 percentile: GET", np.percentile(get_latency, 99), "ms")
#
#print("Failed GET number:", get_err)
#print("Failed POST number:", post_err)
#print("Wall latency:", wall_latency, "ms")
#print("Total request: ", line_count - 1)
#fig, ax = plt.subplots()
#
#
#plt.hist(get_latency, 75, histtype="step", label="get")
#plt.hist(post_latency, 75, histtype="step", label="post")
#ax.set_xlabel("Latency time in ms")
#ax.set_ylabel("Number of request")
#legend = ax.legend()
#
##plt.show()
#plt.savefig("hist" + thread +".png")
#plt.clf()
#
#
#fig, ax = plt.subplots()
#plt.scatter(np.array(get_starttime) - min(get_starttime), get_latency, s=1, label='get')
#plt.scatter(np.array(post_starttime) - min(post_starttime), post_latency, s=1, label='post')
#
#legend = ax.legend()
#ax.set_xlabel("Start time in ms")
#ax.set_ylabel("Latency time in ms")
#
##plt.show()
#plt.savefig("scatter" + thread +".png")
#




