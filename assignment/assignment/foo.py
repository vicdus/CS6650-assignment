import subprocess
import sys, os

out = sys.stdout
sys.stdout = open(os.devnull, 'w')


def mprint(e):
	out.write(e)


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


def setMaxDBConnection(n):
	content = ''
	with open('resource/db_config.yml', 'r') as f:
		content = '\n'.join([c if not c.startswith('db_connection_pool_size') else 'db_connection_pool_size: ' + str(n) for c in f.read().split('\n')])
	with open('resource/db_config.yml', 'w') as f:
		f.write(content)


def startServerBlocking():
	p = subprocess.Popen("mvn package tomcat:run", stdout=subprocess.PIPE, shell=True)
	while True:
		line = p.stdout.readline()
		if not line:
			break
		if line.startswith(b"[INFO] Running war"):
			return

def runWrtie(clientThread, serverThread, dbConnection):
	setMaxDBConnection(dbConnection)
	setMaxServerThread(serverThread)
	startServerBlocking()
	mprint("server starts!")
	logfile = './log/log-{0}-{1}-{2}.txt'.format(clientThread, serverThread, dbConnection)
	cmd = u'''mvn exec:java -Dexec.mainClass="client.LoadClient" -Dexec.args="-threads={0} -logfile={1}"'''.format(clientThread, logfile)
	subprocess.call(cmd, shell=True, stdout=open(os.devnull, 'w'))
	subprocess.call('mvn exec:java -Dexec.mainClass="client.ResetClient" -Dexec.args=""', shell=True, stdout=open(os.devnull, 'w'))
	mprint("finish!")
	


runWrtie(100, 50, 4)

