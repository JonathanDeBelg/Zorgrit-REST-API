-- -----------------------------------------------------
-- Schema zorgrit
-- -----------------------------------------------------
USE `zorgrit`;

-- -----------------------------------------------------
-- Insert
-- -----------------------------------------------------
INSERT INTO `utility` (name, priority) VALUES
  ('scootmobiel', 3),
  ('rolstoel', 2),
  ('rollator', 1);

INSERT INTO `limitation` (name) VALUES
  ('verstandelijk gehandicapten'),
  ('zware/Fysieke handicap'),
  ('ouderen');

INSERT INTO `user` (firstName, lastName, email, phoneNumber, street, houseNumber, zipCode, residence, password,
                      PasswordSalt, dateOfBirth)
VALUES
  ('Wiedo', 'Harkema', 'wiede@gmail.com', '0612345678', 'Kerkstraat', 7, '1234GB', 'Arnhem', '1lI4tLdzNvnJ9/p84JKkoA==', 'abc', '1995-06-23'),
  ('Robin', 'Schuiling', 'robin@hotmail.com', '0687654321', 'Dorpstraat', 33, '1234EF', 'Arnhem', '1lI4tLdzNvnJ9/p84JKkoA==', 'abc', '1997-10-07'),
  ('Koen', 'van Keulen', 'koen@yahoo.com', '0678945612', 'Dorplaan', 12, '5673TR', 'Wageningen', 'pass', 'wer', '1997-10-07'),
  ('Hoang', 'Nguyen', 'hoang@outlook.com', '0645698712', 'Kruisweg', 7, '2845DF', 'Duiven', 'ww', 'cvb', '1992-02-25'),
  ('Piet', 'Jansen', 'piet@jansen.nl', '0638458975', 'Gemeentelaan', 78, '4595HG', 'Nijmegen', 'sesamopenu', 'ghg', '1958-12-13'),
  ('Henk', 'Jansen', 'henk@jansen.nl', '0638458976', 'Dorpsweg', 78, '4597HG', 'Nijmegen', 'sesamopenu', 'ghg', '1958-12-13'),
  ('Jan', 'Jansen', 'jan@jansen.nl', '0638458977', 'tulpenlaan', 78, '4548HG', 'Nijmegen', 'sesamopenu', 'ghg', '1958-12-13'),
  ('Clemens', 'van Barneveld', 'clemens@vanbarneveld.net', '0613657226', 'Dorpsstraat', 83, '5673TR', 'Wageningen', 'sesamopenu', 'ghg', '1958-12-13'),
  ('Mitch', 'Zantingh', 'mitch@zantingh.net', '063515266', 'Dorpsstraat', 83, '6932HG', 'Westervoort', 'sesamopenu', 'ghg', '1955-11-17'),
  ('Abdirizak', 'Middelburg', 'abdirizakc@kpn.net', '0623458256', 'Johannes Husslaan', 13, '1185BK', 'Amstelveen', 'sesamopenu', 'ghg', '1992-12-11'),
  ('Kashif', 'Niewold', 'kashif@niewold.net', '0623458256', 'Hooicamp', 25, '3992BW', 'Houten', 'sesamopenu', 'ghg', '1986-12-11'),
  ('Iefke', 'Vloet', 'iefke@vloet.com', '0623458256', 'Haverakker', 102, '2743EJ', 'Waddinxveen', 'sesamopenu', 'ghg', '1989-04-16'),
  ('Mickey', 'van Zanten', 'mickey@tielmobiel.net', '0623458256', 'Marga Klompestraat', 140, '4133HN', 'Vianen', 'sesamopenu', 'ghg', '1964-02-23');

INSERT INTO `client` (clientId, companion, utility, driverPreferenceForced) VALUES
  (2, 'Sven', 'rolstoel', TRUE),
  (3, 'Henk', NULL, FALSE),
  (5, NULL, NULL, FALSE),
  (6, 'Klaas', 'scootmobiel', FALSE),
  (7, NULL, 'rollator', TRUE);

INSERT INTO `driver` (driverId, verification, utility)
VALUES
  (1, TRUE, NULL),
  (4, FALSE, 'rolstoel'),
  (8, FALSE, 'scootmobiel'),
  (9, FALSE, 'rollator'),
  (10, FALSE, 'scootmobiel'),
  (11, FALSE, 'rolstoel'),
  (12, FALSE, 'rolstoel'),
  (13, FALSE, 'rollator');

INSERT INTO `driverCar` (driverId, utility, numberPlate, numberOfPassengers) VALUES
  (1, 'rollator', 'fg-56-rj', 4),
  (4, 'scootmobiel', 'gh-81-ac', 2),
  (9, 'rolstoel', 'af-83-dc', 1),
  (8, 'scootmobiel', 'nf-26-fe', 3);

INSERT INTO `clientLimitation` (clientId, limitation) VALUES
  (2, 'zware/Fysieke handicap'),
  (3, 'ouderen'),
  (6, 'ouderen');

INSERT INTO `driverAvailability` (driverId, startDateTime, endDateTime) VALUES
  (1, '2019-01-03 12:00:00', '2019-01-03 13:00:00'),
  (4, '2019-07-28 10:00:00', '2019-07-28 17:00:00'),
  (8, '2019-01-03 12:00:00', '2019-01-03 13:00:00'),
  (9, '2019-01-03 12:00:00', '2019-01-03 13:00:00'),
  (10, '2019-01-03 12:00:00', '2019-01-03 13:00:00'),
  (11, '2019-01-03 12:00:00', '2019-01-03 14:00:00'),
  (12, '2019-01-03 12:00:00', '2019-01-03 17:00:00'),
  (13, '2019-01-03 14:00:00', '2019-01-03 15:00:00');

INSERT INTO `driverLimitationManageable` (driverId, limitation) VALUES
  (1, 'verstandelijk gehandicapten'),
  (4,
      'ouderen'),
  (4,
      'zware/Fysieke handicap');

INSERT INTO `clientDriverPreference` (clientId, driverId) VALUES
  (2, 4),
  (3, 8),
  (5, 4),
  (6, 12),
  (7, 10);

INSERT INTO `driverClientPreference` (driverId, clientId) VALUES
  (1, 3),
  (4, 2),
  (8, 5),
  (9, 6),
  (10, 7),
  (11, 2),
  (12, 2),
  (13, 3);

INSERT INTO `careInstitution` (name) VALUES
  ('Reinearde'),
  ('Cordaan'),
  ('IrisZorg'),
  ('IZZ'),
  ('NedRAI');

INSERT INTO `clientCareInstitution` (clientId, careInstitutionId) VALUES
  (2, 1),
  (3, 2),
  (5, 3),
  (6, 5),
  (7, 4);

INSERT INTO `driverCareInstitution` (driverId, careInstitutionId) VALUES
  (1, 2),
  (4, 1),
  (8, 5),
  (9, 3),
  (10, 1),
  (11, 5),
  (12, 4),
  (13, 3);

INSERT INTO `ride` (clientId, driverId, pickUpDateTime, pickUpLocation, dropOffLocation, numberOfcompanions, numberOfLuggage, returnRide, callService, utility, repeatingRideId, cancelledByClient, executed, `started`, duration, distance)
VALUES
  (2, 1, '2019-01-03 12:00:00', 'Steenstraat 2 6828 CJ Arnhem', 'Kalverstraat 2, 1012 PC Amsterdam', 1, 0, TRUE, FALSE, 'rollator', 2, FALSE, TRUE, TRUE, 30, 1500),
  (2, 4, '2019-07-28 12:00:00', 'Ketelstraat 3 6811 CX Arnhem', 'Coolsingel 3, 3011 AD Rotterdam', 0, 3, TRUE, FALSE, 'rolstoel', 2, FALSE, FALSE, FAlSE, 30, 6500),
  (2, 1, '2019-07-03 12:00:00', 'Velperplein 4 6811 AS Arnhem', 'Blaak 4, 3011 TA Rotterdam', 1, 0, TRUE, FALSE, 'rollator', 2, TRUE, FALSE, FAlSE, 30, 8500),
  (2, 4, '2019-09-03 12:00:00', 'Barteljorisstraat 5 2011 RA Haarlem', 'Vredenburg 5 3511 CW Utrecht', 1, 2, TRUE, FALSE, 'rollator', 2, FALSE, FALSE, FAlSE, 30, 4000),
  (3, 4, '2019-04-12 12:56:23', 'Steenstraat 6, Arnhem', 'Kalverstraat 6, 1012 PC Amsterdam', 1, 0, TRUE, FALSE, 'rollator', NULL, FALSE, FALSE, FAlSE, 30, 1568),
  (6, NULL, '2019-06-03 16:00:00', 'Willemsplein 13 6811 KB Arnhem', 'Dorpsstraat 3 4043 KK Opheusden', 0, 0, TRUE, FALSE, 'rollator', NULL, FALSE, FALSE, FAlSE, 30, 3000);

INSERT INTO `rideCanceledByDriver` (rideId, driverId, cancelledDate) VALUES
  (3, 1, '2019-01-01 12:00:00');

INSERT INTO `rideRating` (rideId, rating, `message`, `timestamp`) VALUES
  (1, 2, 'Chauffeur was te laat', '2019-01-03 14:24:00');
