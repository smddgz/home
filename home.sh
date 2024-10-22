cd /root/home || exit
git pull
mvn package
port=$(lsof -i:80|awk 'NR==2 {print $2}')
kill -9 "$port"
nohup java -jar /root/home/target/home.jar &
