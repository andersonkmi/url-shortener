FROM postgres:17.5
ENV POSTGRES_USER=pguser
ENV POSTGRES_PASSWORD=pgpwd
ENV POSTGRES_DB=urlshortner
COPY init.sql /docker-entrypoint-initdb.d/
