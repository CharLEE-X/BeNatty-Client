### Run Shop Web client

1. Build the image

```shell
docker build -t be-natty-shop-web-client .
```

2. Run the container

```shell
docker run --name be-natty-shop-web-client -d -p 3000:8081 be-natty-shop-web-client
```
