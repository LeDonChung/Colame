- Open zookeeper-server-start
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
- Create one broker server
bin\windows\kafka-server-start.bat config\server.properties
- Create topic quickstart-events
bin\windows\kafka-topics.bat --create --topic quickstart-events --bootstrap-server localhost:9092
- Write topic quickstart-events
bin\windows\kafka-console-producer.bat --topic quickstart-events --bootstrap-server localhost:9092
- Read topic quickstart-events
bin\windows\kafka-console-consumer.bat --topic quickstart-events --from-beginning --bootstrap-server localhost:9092

- Login DB CMD with psql
psql -U root -d ColameDBPost;

docker inspect 81cf295a3ad
-> IpAddress 172.21.0.2
ALTER USER postgres WITH PASSWORD 'password'