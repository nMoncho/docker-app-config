FROM maven:3-jdk-8

ARG github_account
ARG github_repository_name

ENV github_url "https://github.com/${github_account}/${github_repository_name}.git"
ENV JAVA_OPTS "-Xmx1024m"

WORKDIR /tmp

RUN git clone "$github_url"

RUN cd ${github_repository_name}/petshop && \
    mvn clean compile && \
    mv ../petshop /tmp/petshop

COPY petshop.sh petshop/run.sh
RUN ["chmod", "+x", "petshop/run.sh"]

WORKDIR /tmp/petshop
ENTRYPOINT ["./run.sh"]
CMD ["7000"]
