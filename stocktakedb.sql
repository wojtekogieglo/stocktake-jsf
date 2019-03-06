-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 06 Mar 2019, 15:54
-- Wersja serwera: 10.3.10-MariaDB
-- Wersja PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `stocktakedb`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `department`
--

CREATE TABLE `department` (
  `idDepartment` int(11) NOT NULL,
  `nameDepartment` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `archive` tinyint(4) NOT NULL,
  `Institution_idInstitution` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `department`
--

INSERT INTO `department` (`idDepartment`, `nameDepartment`, `archive`, `Institution_idInstitution`) VALUES
(1, 'Zaklad Systemow Komputerowych', 0, 2),
(2, 'Zaklad Systemow Informatycznych', 0, 2),
(3, 'Katedra Prawa 1', 0, 3),
(4, 'Katedra Prawa 2', 0, 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `groups`
--

CREATE TABLE `groups` (
  `idGroup` int(11) NOT NULL,
  `category` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `archive` tinyint(4) NOT NULL,
  `description` text COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci ROW_FORMAT=COMPACT;

--
-- Zrzut danych tabeli `groups`
--

INSERT INTO `groups` (`idGroup`, `category`, `archive`, `description`) VALUES
(1, 'Komputery', 0, 'Grupa komputerow'),
(2, 'Monitory', 0, 'Grupa monitorow');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `institution`
--

CREATE TABLE `institution` (
  `idInstitution` int(11) NOT NULL,
  `nameInstitution` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `archive` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `institution`
--

INSERT INTO `institution` (`idInstitution`, `nameInstitution`, `archive`) VALUES
(1, 'StockTake System', 0),
(2, 'Instytut Informatyki', 0),
(3, 'Instytut prawa', 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `invobject`
--

CREATE TABLE `invobject` (
  `idInvObject` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `inventoryNo` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `serialNo` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `quantity` int(11) NOT NULL,
  `toDate` date NOT NULL,
  `purchaseDate` date NOT NULL,
  `archive` tinyint(4) NOT NULL,
  `description` text COLLATE utf8_polish_ci DEFAULT NULL,
  `PersonResponsible_idPersonResponsible` int(11) NOT NULL,
  `Group_idGroup` int(11) NOT NULL,
  `Room_idRoom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `invobject`
--

INSERT INTO `invobject` (`idInvObject`, `name`, `inventoryNo`, `serialNo`, `quantity`, `toDate`, `purchaseDate`, `archive`, `description`, `PersonResponsible_idPersonResponsible`, `Group_idGroup`, `Room_idRoom`) VALUES
(1, 'ASUS', '01', 'AXJSDA', 1, '2019-02-28', '2019-02-18', 0, '', 4, 1, 3),
(2, 'ACER', '01', 'ACERXSDA', 1, '2019-02-28', '2019-02-18', 1, '', 3, 1, 4),
(3, 'TOSHIBA', '01', 'TOSHIBADSA', 1, '2019-02-28', '2019-02-18', 0, '', 2, 2, 1),
(4, 'AMICA', '01', 'AMICADSA', 1, '2019-02-28', '2019-02-18', 0, '', 1, 2, 2),
(5, 'KOMPUTER1', '02', 'KOMP', 1, '2019-02-28', '2019-02-18', 0, '', 8, 2, 6),
(6, 'PRZEDMIOT3', '02', 'PRZEDMIOT', 1, '2019-02-28', '2019-02-18', 0, '', 5, 1, 5);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `personresponsible`
--

CREATE TABLE `personresponsible` (
  `idPersonResponsible` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `surname` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `archive` tinyint(4) NOT NULL,
  `description` text COLLATE utf8_polish_ci DEFAULT NULL,
  `Department_idDepartment` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `personresponsible`
--

INSERT INTO `personresponsible` (`idPersonResponsible`, `name`, `surname`, `archive`, `description`, `Department_idDepartment`) VALUES
(1, 'Andrzej', 'Niedzielan', 0, 'Opis osoby odpowiedzialnej', 1),
(2, 'Robert', 'Lewandowski', 0, '', 1),
(3, 'Arkadiusz', 'Milik', 0, '', 2),
(4, 'Andrzej', 'Boruc', 0, '', 2),
(5, 'Lukas', 'Podolski', 0, '', 3),
(6, 'Miroslav', 'Klose', 0, '', 3),
(7, 'Szymon', 'Zurkowski', 0, '', 4),
(8, 'Igor', 'Angulo', 0, '', 4);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `role`
--

CREATE TABLE `role` (
  `idRole` int(11) NOT NULL,
  `roleName` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `description` text COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `role`
--

INSERT INTO `role` (`idRole`, `roleName`, `description`) VALUES
(1, 'ROLE_ADMIN', NULL),
(2, 'ROLE_INST_ADMIN', NULL),
(3, 'ROLE_USER', NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `roleuser`
--

CREATE TABLE `roleuser` (
  `Role_idRole` int(11) NOT NULL,
  `User_idUser` int(11) NOT NULL,
  `grantDate` date NOT NULL,
  `expirationDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `roleuser`
--

INSERT INTO `roleuser` (`Role_idRole`, `User_idUser`, `grantDate`, `expirationDate`) VALUES
(1, 1, '2019-02-18', '2019-02-28'),
(2, 2, '2019-02-18', '2019-02-28'),
(2, 4, '2019-02-18', '2019-02-28'),
(3, 3, '2019-02-18', '2019-02-28'),
(3, 5, '2019-02-18', '2019-02-28');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `room`
--

CREATE TABLE `room` (
  `idRoom` int(11) NOT NULL,
  `number` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `floor` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `archive` tinyint(4) NOT NULL,
  `description` text COLLATE utf8_polish_ci DEFAULT NULL,
  `Department_idDepartment` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `room`
--

INSERT INTO `room` (`idRoom`, `number`, `floor`, `archive`, `description`, `Department_idDepartment`) VALUES
(1, '200', '2', 0, 'Pokoj numer 200 w Instytucie Informatyki, wydzial: zaklad systemow komputerowych', 1),
(2, '201', '2', 0, 'Pokoj 201', 1),
(3, '100', '12', 0, 'Pokoj 100', 2),
(4, '101', '1', 0, 'Pokoj 101', 2),
(5, '2004', '12', 0, '', 3),
(6, '1004', '20', 0, '', 4);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `roomperson`
--

CREATE TABLE `roomperson` (
  `Room_idRoom` int(11) NOT NULL,
  `PersonResponsible_idPersonResponsible` int(11) NOT NULL,
  `fromDate` date NOT NULL,
  `toDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `roomperson`
--

INSERT INTO `roomperson` (`Room_idRoom`, `PersonResponsible_idPersonResponsible`, `fromDate`, `toDate`) VALUES
(1, 1, '2019-02-18', '2019-02-28'),
(2, 2, '2019-02-18', '2019-02-28'),
(3, 3, '2019-02-18', '2019-02-28'),
(4, 4, '2019-02-18', '2019-02-28');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `surname` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `email` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `password` varchar(45) COLLATE utf8_polish_ci NOT NULL,
  `active` tinyint(1) NOT NULL,
  `Institution_idInstitution` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`idUser`, `name`, `surname`, `email`, `password`, `active`, `Institution_idInstitution`) VALUES
(1, 'Wojciech', 'Ogieglo', 'superadmin@projekt.pl', 'superadmin', 1, 1),
(2, 'Jan', 'Kowalski', 'admininformatyka@projekt.pl', 'user', 1, 2),
(3, 'Maciej', 'Nowak', 'userinformatyki@projekt.pl', 'user', 1, 2),
(4, 'Pawel', 'Nowakowski', 'adminprawa@projekt.pl', 'user', 1, 3),
(5, 'Janusz', 'Nosacz', 'userprawa@projekt.pl', 'user', 1, 3);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`idDepartment`),
  ADD KEY `fk_Department_Institution1_idx` (`Institution_idInstitution`);

--
-- Indeksy dla tabeli `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`idGroup`);

--
-- Indeksy dla tabeli `institution`
--
ALTER TABLE `institution`
  ADD PRIMARY KEY (`idInstitution`);

--
-- Indeksy dla tabeli `invobject`
--
ALTER TABLE `invobject`
  ADD PRIMARY KEY (`idInvObject`),
  ADD KEY `fk_InvObject_PersonResponsible1_idx` (`PersonResponsible_idPersonResponsible`),
  ADD KEY `fk_InvObject_Group1_idx` (`Group_idGroup`),
  ADD KEY `fk_InvObject_Room` (`Room_idRoom`);

--
-- Indeksy dla tabeli `personresponsible`
--
ALTER TABLE `personresponsible`
  ADD PRIMARY KEY (`idPersonResponsible`),
  ADD KEY `fk_PersonResponsible_Department` (`Department_idDepartment`);

--
-- Indeksy dla tabeli `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`idRole`);

--
-- Indeksy dla tabeli `roleuser`
--
ALTER TABLE `roleuser`
  ADD PRIMARY KEY (`Role_idRole`,`User_idUser`),
  ADD KEY `fk_Role_has_User_User1_idx` (`User_idUser`),
  ADD KEY `fk_Role_has_User_Role_idx` (`Role_idRole`);

--
-- Indeksy dla tabeli `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`idRoom`),
  ADD KEY `fk_Room_Department1_idx` (`Department_idDepartment`);

--
-- Indeksy dla tabeli `roomperson`
--
ALTER TABLE `roomperson`
  ADD PRIMARY KEY (`Room_idRoom`,`PersonResponsible_idPersonResponsible`),
  ADD KEY `fk_Room_has_PersonResponsible_PersonResponsible1_idx` (`PersonResponsible_idPersonResponsible`),
  ADD KEY `fk_Room_has_PersonResponsible_Room1_idx` (`Room_idRoom`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD KEY `fk_User_Institution1_idx` (`Institution_idInstitution`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `department`
--
ALTER TABLE `department`
  MODIFY `idDepartment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `groups`
--
ALTER TABLE `groups`
  MODIFY `idGroup` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `institution`
--
ALTER TABLE `institution`
  MODIFY `idInstitution` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `invobject`
--
ALTER TABLE `invobject`
  MODIFY `idInvObject` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `personresponsible`
--
ALTER TABLE `personresponsible`
  MODIFY `idPersonResponsible` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT dla tabeli `role`
--
ALTER TABLE `role`
  MODIFY `idRole` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `room`
--
ALTER TABLE `room`
  MODIFY `idRoom` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `department`
--
ALTER TABLE `department`
  ADD CONSTRAINT `fk_Department_Institution1` FOREIGN KEY (`Institution_idInstitution`) REFERENCES `institution` (`idInstitution`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `invobject`
--
ALTER TABLE `invobject`
  ADD CONSTRAINT `fk_InvObject_Group1` FOREIGN KEY (`Group_idGroup`) REFERENCES `groups` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_InvObject_PersonResponsible1` FOREIGN KEY (`PersonResponsible_idPersonResponsible`) REFERENCES `personresponsible` (`idPersonResponsible`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_InvObject_Room` FOREIGN KEY (`Room_idRoom`) REFERENCES `room` (`idRoom`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `personresponsible`
--
ALTER TABLE `personresponsible`
  ADD CONSTRAINT `fk_PersonResponsible_Department` FOREIGN KEY (`Department_idDepartment`) REFERENCES `department` (`idDepartment`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `roleuser`
--
ALTER TABLE `roleuser`
  ADD CONSTRAINT `fk_Role_has_User_Role` FOREIGN KEY (`Role_idRole`) REFERENCES `role` (`idRole`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Role_has_User_User1` FOREIGN KEY (`User_idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `fk_Room_Department1` FOREIGN KEY (`Department_idDepartment`) REFERENCES `department` (`idDepartment`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `roomperson`
--
ALTER TABLE `roomperson`
  ADD CONSTRAINT `fk_Room_has_PersonResponsible_PersonResponsible1` FOREIGN KEY (`PersonResponsible_idPersonResponsible`) REFERENCES `personresponsible` (`idPersonResponsible`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Room_has_PersonResponsible_Room1` FOREIGN KEY (`Room_idRoom`) REFERENCES `room` (`idRoom`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ograniczenia dla tabeli `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_User_Institution1` FOREIGN KEY (`Institution_idInstitution`) REFERENCES `institution` (`idInstitution`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
