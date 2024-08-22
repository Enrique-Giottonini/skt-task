FROM mcr.microsoft.com/mssql/server:2022-latest
USER root
# Arguments to be passed to the image
ARG ACCEPT_EULA
ARG MSSQL_PID
ARG MSSQL_SA_PASSWORD
ARG MSSQL_TCP_PORT

COPY run-initialization.sh /usr/src/app/run-initialization.sh
COPY entrypoint.sh /usr/src/app/entrypoint.sh
COPY create-database.sql /usr/src/app/create-database.sql

RUN chmod +x /usr/src/app/run-initialization.sh /usr/src/app/entrypoint.sh

ENTRYPOINT /bin/bash /usr/src/app/entrypoint.sh