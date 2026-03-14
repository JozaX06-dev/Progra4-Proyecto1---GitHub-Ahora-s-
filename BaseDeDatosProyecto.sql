-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ProyectoBolsaEmpleo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ProyectoBolsaEmpleo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ProyectoBolsaEmpleo` DEFAULT CHARACTER SET utf8 ;
USE `ProyectoBolsaEmpleo` ;

-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `correo` VARCHAR(100) NOT NULL,
  `clave` VARCHAR(255) NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`nacionalidad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`nacionalidad` (
  `iso` VARCHAR(2) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`iso`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`caracteristica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`caracteristica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `padre_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_caracteristica_padre_idx` (`padre_id` ASC) VISIBLE,
  CONSTRAINT `fk_caracteristica_padre`
    FOREIGN KEY (`padre_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`empresa` (
  `usuario_id` INT NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `localizacion` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `descripcion` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `fk_empresa_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`oferente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`oferente` (
  `usuario_id` INT NOT NULL,
  `identificacion` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `nacionalidad_iso` VARCHAR(2) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `lugar_residencia` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  INDEX `fk_nacionalidad_iso_idx` (`nacionalidad_iso` ASC) VISIBLE,
  CONSTRAINT `fk_oferente_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nacionalidad_iso`
    FOREIGN KEY (`nacionalidad_iso`)
    REFERENCES `ProyectoBolsaEmpleo`.`nacionalidad` (`iso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`administrador` (
  `usuario_id` INT NOT NULL,
  `identificacion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`usuario_id`),
  CONSTRAINT `fk_administrador_usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`puesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`puesto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(255) NOT NULL,
  `salario` DOUBLE NOT NULL,
  `es_publico` TINYINT NOT NULL,
  `activo` TINYINT NOT NULL,
  `empresa_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `empresa_id_idx` (`empresa_id` ASC) VISIBLE,
  CONSTRAINT `fk_empresa_id`
    FOREIGN KEY (`empresa_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`empresa` (`usuario_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`requisito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`requisito` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `puesto_id` INT NOT NULL,
  `caracteristica_id` INT NOT NULL,
  `nivel_deseado` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_puesto_id_idx` (`puesto_id` ASC) VISIBLE,
  INDEX `fk_caracteristica_id_idx` (`caracteristica_id` ASC) VISIBLE,
  CONSTRAINT `fk_puesto_id`
    FOREIGN KEY (`puesto_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`puesto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_requisito_caracteristica`
    FOREIGN KEY (`caracteristica_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ProyectoBolsaEmpleo`.`habilidad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProyectoBolsaEmpleo`.`habilidad` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `oferente_id` INT NOT NULL,
  `caracteristica_id` INT NOT NULL,
  `nivel` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_oferente_id_idx` (`oferente_id` ASC) VISIBLE,
  INDEX `fk_caracteristica_id_idx` (`caracteristica_id` ASC) VISIBLE,
  CONSTRAINT `fk_oferente_id`
    FOREIGN KEY (`oferente_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`oferente` (`usuario_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_habilidad_caracteristica`
    FOREIGN KEY (`caracteristica_id`)
    REFERENCES `ProyectoBolsaEmpleo`.`caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
