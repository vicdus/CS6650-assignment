import subprocess
import sys, os, paramiko

EC2_IP = "ec2-52-37-136-216.us-west-2.compute.amazonaws.com"
KEY_PATH = "./resource/key.pem"



def setMaxServerThread(n):
	template = '''<?xml version='1.0' encoding='utf-8'?>
	<Server port="8081" shutdown="SHUTDOWN">
		<Service name="Catalina">
			<Connector port="8080" protocol="HTTP/1.1"
					   connectionTimeout="2000000"
					   maxThread="MAX_THREAD"
			/>
			<Engine name="Catalina" defaultHost="localhost">
				<Host name="localhost" unpackWARs="false" autoDeploy="false"/>
			</Engine>
		</Service>
	</Server>
	'''
	with open('src/main/webapp/server.xml', 'w') as f:
		f.write(template.replace('MAX_THREAD', str(n)))
		subprocess.call("scp -i {0} ./src/main/webapp/server.xml ec2-user@{1}:/home/ec2-user/workspace/CS6650-assignment/assignment/assignment/src/main/webapp/".format(KEY_PATH, EC2_IP), shell=True)


def setMaxDBConnection(n):
	content = ''
	with open('./resource/db_config.yml', 'r') as f:
		content = '\n'.join([c if not c.startswith('db_connection_pool_size') else 'db_connection_pool_size: ' + str(n) for c in f.read().split('\n')])
	with open('./resource/db_config.yml', 'w') as f:
		f.write(content)
	subprocess.call("scp -i {0} ./resource/db_config.yml ec2-user@{1}:/home/ec2-user/workspace/CS6650-assignment/assignment/assignment/resource/".format(KEY_PATH, EC2_IP), shell=True)
	

def startServerBlocking():
	client = paramiko.SSHClient()
	client.set_missing_host_key_policy(paramiko.WarningPolicy)
	client.connect(EC2_IP, port=22, username="ec2-user", key_filename=KEY_PATH)
	stdin, stdout, stderr = client.exec_command("cd ~/workspace/CS6650-assignment/assignment/assignment; ls; mvn clean package tomcat:run")
	while True:
		p = stdout.readline()
		if p.startswith("[INFO] Running"):
			return client
		

def runWrtie(clientThread, serverThread, dbConnection):
	subprocess.call('''curl -X "DELETE" http://ec2-52-37-136-216.us-west-2.compute.amazonaws.com:8080/assignment2/reset''', shell=True)
	setMaxDBConnection(dbConnection)
	setMaxServerThread(serverThread)
	serverSSH = startServerBlocking()
	print("server starts!")

	logfile = './log/log-{0}-{1}-{2}.txt'.format(clientThread, serverThread, dbConnection)
	cmd = u'''mvn compile exec:java -Dexec.mainClass="client.LoadClient" -Dexec.args="-threads={0} -logfile={1} -ip={2}"'''.format(clientThread, logfile, EC2_IP)
	subprocess.call(cmd, shell=True, stdout=open(os.devnull, 'w'))
	serverSSH.close()
	
	print("finish!")
	
	
	
	
def runGet(serverThread, dbConnection):
	setMaxDBConnection(dbConnection)
	setMaxServerThread(serverThread)
	serverSSH = startServerBlocking()
	print("server starts!")
	logfile = './log/get-{0}-{1}-{2}.txt'.format(100, serverThread, dbConnection)
	cmd = u'''mvn compile exec:java -Dexec.mainClass="client.MyVertClient" -Dexec.args="-logfile={0} -ip={1}"'''.format(logfile, EC2_IP)
	subprocess.call(cmd, shell=True, stdout=open(os.devnull, 'w'))
	
	serverSSH.close()
	
	
	

def runBoth(serverThread, dbConnection):
	CLIENT_THREAD = 50
	DAY2_SER_FILE = "./resource/BSDSAssignment2Day2.ser"
	setMaxDBConnection(dbConnection)
	setMaxServerThread(serverThread)
	serverSSH = startServerBlocking()
	print("server starts!")
	getLog = './log/bothGet-{0}-{1}-{2}.txt'.format(CLIENT_THREAD, serverThread, dbConnection)
	postLog = './log/bothPost-{0}-{1}-{2}.txt'.format(CLIENT_THREAD, serverThread, dbConnection)
	cmd1 = u'''mvn compile exec:java -Dexec.mainClass="client.MyVertClient" -Dexec.args="-logfile={0} -ip={1}"'''.format(getLog, EC2_IP)
	cmd2 = u'''mvn compile exec:java -Dexec.mainClass="client.LoadClient" -Dexec.args="-threads={0} -logfile={1} -ip={2} -source={3}"'''.format(CLIENT_THREAD, postLog, EC2_IP, DAY2_SER_FILE)
	proc1 = subprocess.Popen(cmd1, shell=True)
	proc2 = subprocess.Popen(cmd2, shell=True)
	proc1.wait()
	proc2.wait()
	serverSSH.close()
	print("done!")



#runBoth(200, 20)

clientThreads = [10, 20, 40, 80, 160]
serverThreads = [10, 20, 40, 80, 160]
dbConns = [10, 20, 40, 80, 160]

params = [[i, j, k] for i in clientThreads for j in serverThreads for k in dbConns if i > j and j > k]
print(params)
print(len(params))

runWrtie(80, 40, 20)
#for p in params[5:]:
#	runWrtie(p[0], p[1], p[2])

