import numpy as np
import subprocess

fns = subprocess.check_output("ls log/", shell=True).decode("utf-8")
fileNames = [v for v in fns.split("\n") if len(v) > 0]

print("\t".join([str(v) for v in ["Type", "Client", "Server", "DB", "Throughput", "Wall min", "avg", "p50", "p95", "p99"]]))
for name in fileNames:
	client, server, db = name.split(".")[0].split("-")[1:]
	
	content = open("./log/" + name, "r").read()
	latency = [int(v.split(" ")[1]) for v in content.split("\n") if len(v) > 0]
	wallTime = latency[-1]
	latency = latency[:-1]

	t = int(len(latency) / (wallTime / 1000))
	p50 = np.percentile(latency, 50)
	avg = round(np.mean(latency), 2)
	p95 = np.percentile(latency, 95)
	p99 = np.percentile(latency, 99)
	
	
	wt_in_min = round(wallTime / (60 * 1000), 2)
	print("\t".join([str(v) for v in [name.split("-")[0], client, server, db, t, wt_in_min, avg, p50, p95, p99]]))


