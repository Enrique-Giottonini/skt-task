IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'productsdb')
BEGIN
    CREATE DATABASE productsdb
END
GO
   USE productsdb
GO
IF NOT EXISTS(SELECT 1 FROM sys.tables WHERE name="product" and type = "U")
BEGIN
    CREATE TABLE Product (
        Id BIGINT PRIMARY KEY IDENTITY(1,1),
        Name NVARCHAR(50) NOT NULL,
        Description NVARCHAR(100),
        Price DECIMAL(18, 2) NOT NULL CHECK (Price >= 0)
    )
END
