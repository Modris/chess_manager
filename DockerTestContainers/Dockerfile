
#SELECT Base MySQL version
FROM mysql:8.0 

COPY my.cnf /etc/mysql/conf.d/my.cnf

#Copy schema tables and data into the mysql image. It will auto initialize.
COPY base.sql /docker-entrypoint-initdb.d/