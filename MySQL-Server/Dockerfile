# Use the official MySQL 8.0 image as the base image
FROM mysql:8.0

# Set the MySQL root password
ENV MYSQL_ROOT_PASSWORD=my-secret-pw

# Copy the SQL scripts to initialize the database
COPY ./scripts/ /docker-entrypoint-initdb.d/