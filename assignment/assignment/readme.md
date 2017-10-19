JDK 1.8 and Maven is requried.



Start server:

```
$ mvn package tomcat:run
```

Start client:

```
$ mvn package exec:java -Dexec.args="-threads 10 -iterations 100 -ip 127.0.0.1 -port 8080"
```

`threads` and `iterations` are optional parameters.


Client will output a `log.txt` file about latency and the timestamp of request sending. See assignment1 submission for more details.
