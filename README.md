# Configuring applications with Docker

This repository acts as companion code for the [blog post](https://new-grumpy-mentat.blogspot.com/2019/09/configuring-applications-with-docker_9.html), it provides 6 examples:
- Image with arguments.
- Image that includes files.
- Container with runtime arguments.
- Container with environment variables.
- All of the above.

## Running examples
Running the examples bares down to just invoking `docker build`

### Image with arguments
```
docker build \
  --file Dockerfile.build_args \
  --build-args MY_BUILD_ARG1=Foo \
  --build-args MY_BUILD_ARG2=Bar \
  --tag bash_build_args .
```

You should see the first build argument in the console.

### Image that includes files
```
docker build \
  --file Dockerfile.build_files \
  --tag bash_build_files .
```

If you connect to the container you should be able to see the files:

```
docker run -it bash_build_files
$ ls /cat_pics
cat-1045782__480.jpg  cat-2083492__480.jpg  cat-2273598__480.jpg  cat-2934720__480.jpg  cat-323262__480.jpg
```

### Container with runtime arguments
**Without script:**
```
docker build \
  --file Dockerfile.run_args \
  --tag bash_run_args .
```

**With script:**
```
docker build \
  --file Dockerfile.run_args_script \
  --tag bash_run_args_script .
```

And the you can run each container with the message you want to see in the console:

```
docker run bash_run_args_script This is a Cool Message!!!
```

### Container with environment variables
```
docker build \
  --file Dockerfile.run_env \
  --tag bash_run_env .
```

After which you run the container:
```
docker run -it \
  --env MY_NAME="Jane Smith" \
  --env MY_EMAIL="jane.smith@noname.com" \
  bash-run_env
```

These two environment variables are available in the shell (eg. `echo $MY_NAME`)

### All of the above
First we need to build the image:

```
docker build \
  --build-args github_account="<your GitHub account>" \
  --build-args github_repository_name=<your GitHub fork> \
  --tag petshop .
```

After you can run the container, mounting where uploaded images should go:
```
docker run \
  --publish 7000:7000 \
  --env JAVA_OPTS="-Xmx1536m" \
  --volume $(pwd)/my_photos:/tmp/petshop/upload \
  petshop
```
