echo "Building app..."
./mvnw clean package -DskipTests

echo "Deploy files to server..."
ssh root@14.225.211.236 "mkdir -p /var/www/be"
scp target/be.jar root@14.225.211.236:/var/www/be/

ssh -t root@14.225.211.236 <<EOF
pid=\$(lsof -t -i:8080)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    kill -9 "\$pid"
fi
cd /var/www/be
java -jar be.jar
EOF
exit
echo "Done!"