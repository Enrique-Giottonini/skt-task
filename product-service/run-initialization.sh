# Wait for the SQL Server to be available
sleep 15s

# Run the SQL script
/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -No -d master -i /usr/src/app/create-database.sql