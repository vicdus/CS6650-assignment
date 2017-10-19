import numpy as np
import matplotlib.pyplot as plt

thread = "100"


f = open("log-" + thread + ".txt", "r")


get_latency = []
get_starttime = []
post_latency = []
post_starttime = []
wall_latency = None
get_err = 0
post_err = 0
line_count = 0
for line in f:
	line_count += 1
	pair = line[:-1].split(" ")
	name, time = pair[0], pair[1]
	if name == "GET":
		if time == "FAILURE":
			get_err += 1
		else:
			get_latency.append(int(time))
			get_starttime.append(int(pair[2]))
	elif name == "POST":
		if time == "FAILURE":
			post_err += 1
		else:
			post_latency.append(int(time))
			post_starttime.append(int(pair[2]))
	else:
		wall_latency = int(time)


print(thread, "threads, 100 iterations")

print("50 percentile POST:", np.percentile(post_latency, 50), "ms")
print("95 percentile POST:", np.percentile(post_latency, 95), "ms")
print("99 percentile: POST", np.percentile(post_latency, 99), "ms")
print("50 percentile GET:", np.percentile(get_latency, 50), "ms")
print("95 percentile GET:", np.percentile(get_latency, 95), "ms")
print("99 percentile: GET", np.percentile(get_latency, 99), "ms")

print("Failed GET number:", get_err)
print("Failed POST number:", post_err)
print("Wall latency:", wall_latency, "ms")
print("Total request: ", line_count - 1)
fig, ax = plt.subplots()


plt.hist(get_latency, 75, histtype="step", label="get")
plt.hist(post_latency, 75, histtype="step", label="post")
ax.set_xlabel("Latency time in ms")
ax.set_ylabel("Number of request")
legend = ax.legend()

#plt.show()
plt.savefig("hist" + thread +".png")
plt.clf()


fig, ax = plt.subplots()
plt.scatter(np.array(get_starttime) - min(get_starttime), get_latency, s=1, label='get')
plt.scatter(np.array(post_starttime) - min(post_starttime), post_latency, s=1, label='post')

legend = ax.legend()
ax.set_xlabel("Start time in ms")
ax.set_ylabel("Latency time in ms")

#plt.show()
plt.savefig("scatter" + thread +".png")





