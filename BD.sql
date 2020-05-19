Sur base1

CREATE TABLE GARAGE (
    idgarage INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(255),
    ville VARCHAR(255),
    jourdefermeture VARCHAR(255),
    PRIMARY KEY (idgarage)
);


CREATE TABLE MECANICIEN (
    idpers INT NOT NULL AUTO_INCREMENT,
    idgarage INT NOT NULL,
    niveau INT,
    PRIMARY KEY (idpers),
    FOREIGN KEY (idgarage)         -- Colonne sur laquelle on crée la clé
    REFERENCES GARAGE(idgarage)        -- Colonne de référence
);


CREATE TABLE PERSONNE (
    idpers INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255),
    age INT,
    PRIMARY KEY (idpers)
);


Sur base2

CREATE TABLE HABILITE (
    idgarage INT NOT NULL AUTO_INCREMENT,
    marque VARCHAR(255),
    PRIMARY KEY (idgarage, marque)
);


INSERT INTO GARAGE (nom, ville, jourdefermeure) VALUES ('Speedy', 'Paris', 'Dimanche'),
                                                       ('AutoService', 'Tours', 'Lundi'),
                                                       ('Feu Vert', 'Paris', 'Dimanche'),
                                                       ('iDGARAGE', 'Lyon', 'Samedi'),
                                                       ('Modern Garage', 'Bordeaux', 'Lundi'),
                                                       ('Garage Auto', 'Paris', 'Dimanche'),
                                                       ('La Centrale', 'Marseille', 'Lundi'),
                                                       ('AD Garage', 'Paris', 'Dimanche'),
                                                       ('Euromaster', 'Lyon', 'Samedi'),
                                                       ('Nauroto', 'La Rochelle', 'Dimanche');


INSERT INTO PERSONNE (nom, prenom, age) VALUES ('Armand', 'Rébecca', 24),
                                             ('Hebert', 'Aimée', 35),
                                             ('Ribeiro', 'Marielle', 18),
                                             ('Savary', 'Hilaire', 27),
                                             ('Dupont', 'Jean', 64),
                                             ('Dubois', 'Léo', 23),
                                             ('Mbappé', 'Kylian', 21),
                                             ('Georges', 'Antoine', 25),
                                             ('Cavani', 'Edinson', 32),
                                             ('Messi', 'Lionel', 33),
                                             ('Precieuse', 'Pierre', 45),
                                             ('Durand','Jeanne', 18),
                                             ('Zoulou','Zoé', 18),
                                             ('Pitaut','Enzo', 18),
                                             ('Hunter','Paulo', 18);


INSERT INTO MECANICIEN (idpers, idgarage, niveau) VALUES (1, 1, 2),
                                                         (2, 1, 3),
                                                         (4, 10, 2),
                                                         (5, 10, 1),
                                                         (6, 3, 2),
                                                         (8, 6, 2),
                                                         (10, 3, 1),
                                                         (11, 4, 2),
                                                         (12, 4, 2),
                                                         (13, 1, 2);


INSERT INTO HABILITE (idgarage, marque) VALUES (1, 'Citröen'),
                                               (1, 'Peugeot'),
                                               (1, 'Nissan'),
                                               (2, 'Nissan'),
                                               (2, 'Peugeot'),
                                               (3, 'BMW'),
                                               (4, 'Mercedes'),
                                               (5, 'Peugeot'),
                                               (6, 'Citröen'),
                                               (7, 'Renault');

Pour exo 4 :

 CREATE TABLE MECANICIEN (
    idpers INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255),
    idgarage INT NOT NULL,
    niveau INT,
    PRIMARY KEY (idpers),
    FOREIGN KEY (idgarage)         -- Colonne sur laquelle on crée la clé
    REFERENCES GARAGE(idgarage)        -- Colonne de référence
);               

CREATE TABLE HABILITE (
    idgarage INT NOT NULL AUTO_INCREMENT,
    marque VARCHAR(255),
    PRIMARY KEY (idgarage, marque)
);

INSERT INTO MECANICIEN (idpers, nom, prenom, idgarage, niveau) VALUES (1,'Armand', 'Rébecca', 1, 2),
                                                         (2, 'Hebert', 'Aimée', 1, 3),
                                                         (4, 'Ribeiro', 'Marielle', 10, 2),
                                                         (5, 'Savary', 'Hilaire', 10, 1),
                                                         (6, 'Dupont', 'Jean', 3, 2),
                                                         (8, 'Georges', 'Antoine', 6, 2),
                                                         (10, 'Precieuse', 'Pierre', 3, 1),
                                                         (11, 'Pitaut','Enzo', 4, 2),
                                                         (12, 'Hunter','Paulo', 4, 2),
                                                         (13, 'Mbappé', 'Kylian', 1, 2);

                                                         ('Savary', 'Hilaire', 27),
                                             ('Dupont', 'Jean', 64),
                                             ('Dubois', 'Léo', 23),
                                             ('Mbappé', 'Kylian', 21),
                                             ('Georges', 'Antoine', 25),
                                             ('Cavani', 'Edinson', 32),
                                             ('Messi', 'Lionel', 33),
                                             ('Precieuse', 'Pierre', 45),
                                             ('Durand','Jeanne', 18),
                                             ('Zoulou','Zoé', 18),
                                             ('Pitaut','Enzo', 18),
                                             ('Hunter','Paulo', 18);