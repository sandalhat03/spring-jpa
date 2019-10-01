CREATE SCHEMA `spring_jpa`;

CREATE TABLE `spring_jpa`.`Customer` (
  `Id` INT NOT NULL,
  `FirstName` VARCHAR(40) NULL,
  `LastName` VARCHAR(40) NULL,
  `City` VARCHAR(40) NULL,
  `Country` VARCHAR(40) NULL,
  `Phone` VARCHAR(20) NULL,
  PRIMARY KEY (`Id`),
  INDEX `IndexCustomerName` (`LastName` ASC, `FirstName` ASC) VISIBLE);

  
 CREATE TABLE `spring_jpa`.`Order` (
  `Id` INT NOT NULL,
  `OrderDate` DATETIME NULL,
  `OrderNumber` VARCHAR(10) NULL,
  `CustomerId` INT NOT NULL,
  `TotalAmount` DECIMAL(12,2) NULL,
  PRIMARY KEY (`Id`),
  INDEX `FKCustomerId_idx` (`CustomerId` ASC) VISIBLE,
  INDEX `IndexOrderCustomerId` (`CustomerId` ASC) VISIBLE,
  INDEX `IndexOrderOrderDate` (`OrderDate` ASC) VISIBLE,
  CONSTRAINT `FKCustomerId`
    FOREIGN KEY (`CustomerId`)
    REFERENCES `spring_jpa`.`Customer` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);