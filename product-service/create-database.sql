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

IF NOT EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'insertProduct')
BEGIN
    EXEC('
    CREATE PROCEDURE insertProduct
        @Name NVARCHAR(50),
        @Description NVARCHAR(100) = NULL,
        @Price DECIMAL(18, 2)
    AS
    BEGIN
        SET NOCOUNT ON;

        DECLARE @NewProductId BIGINT;

        INSERT INTO Product (Name, Description, Price)
        VALUES (@Name, @Description, @Price);

        SET @NewProductId = SCOPE_IDENTITY();

        SELECT Id, Name, Description, Price
        FROM Product
        WHERE Id = @NewProductId;
    END
    ')
END
GO

IF NOT EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'getAllProducts')
BEGIN
    EXEC('
    CREATE PROCEDURE getAllProducts
    AS
    BEGIN
        SET NOCOUNT ON;

        SELECT Id, Name, Description, Price
        FROM Product;
    END
    ')
END
GO
