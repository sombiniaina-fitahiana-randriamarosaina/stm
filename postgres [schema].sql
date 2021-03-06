sudo -i -u postgres
psql CREATE DATABASE stm;
psql;
CREATE ROLE admin_stm LOGIN password '123456';
ALTER DATABASE stm OWNER TO admin_stm;
\q
exit
psql -U admin_stm stm
123456
CREATE SEQUENCE FORFAIT_SEQ INCREMENT BY 1 START 1;
CREATE TABLE FORFAIT(
    IDFORFAIT VARCHAR DEFAULT 'FRF' || NEXTVAL('FORFAIT_SEQ'),
    NOMFORFAIT VARCHAR NOT NULL UNIQUE,
    DATAUNITE VARCHAR NOT NULL,
    CONSOUNITE VARCHAR NOT NULL,
    PRIMARY KEY (IDFORFAIT)
);

CREATE SEQUENCE SOUSFORFAIT_SEQ INCREMENT BY 1 START 1;
CREATE TABLE SOUSFORFAIT (
    IDSOUSFORFAIT VARCHAR DEFAULT 'SFRF' || NEXTVAL('SOUSFORFAIT_SEQ'),
    IDFORFAIT VARCHAR NOT NULL,
    NOMSOUSFORFAIT VARCHAR NOT NULL UNIQUE,
    COUTCREDIT DECIMAL(10,2) NOT NULL,
    COUTOFFRE DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (IDSOUSFORFAIT),
    FOREIGN KEY (IDFORFAIT) REFERENCES FORFAIT (IDFORFAIT)
);
CREATE UNIQUE INDEX UK_SOUSFORFAIT ON SOUSFORFAIT(IDFORFAIT, NOMSOUSFORFAIT);

CREATE SEQUENCE OFFRE_SEQ INCREMENT BY 1 START 1;
CREATE TABLE OFFRE (
    IDOFFRE VARCHAR DEFAULT 'OFR' || NEXTVAL('OFFRE_SEQ'),
    NOMOFFRE VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY (IDOFFRE)
);

CREATE SEQUENCE SOUSOFFRE_SEQ INCREMENT BY 1 START 1;
CREATE TABLE SOUSOFFRE(
    IDSOUSOFFRE VARCHAR DEFAULT 'SOFR' || NEXTVAL('SOUSOFFRE_SEQ'),
    IDOFFRE VARCHAR NOT NULL,
    PRIX INTEGER NOT NULL,
    VALIDITE INTEGER NOT NULL,
    PRIMARY KEY(IDSOUSOFFRE),
    FOREIGN KEY (IDOFFRE) REFERENCES OFFRE (IDOFFRE)
);
CREATE UNIQUE INDEX UK_SOUSOFFRE ON SOUSOFFRE (IDOFFRE, PRIX);

CREATE TABLE VOLUMESOUSOFFRE (
    IDSOUSOFFRE VARCHAR NOT NULL,
    IDFORFAIT VARCHAR NOT NULL,
    VOLUME INTEGER NOT NULL,
    FOREIGN KEY (IDSOUSOFFRE) REFERENCES SOUSOFFRE (IDSOUSOFFRE),
    FOREIGN KEY (IDFORFAIT) REFERENCES FORFAIT (IDFORFAIT)
);
CREATE UNIQUE INDEX UK_VOLUMESOUSOFFRE ON VOLUMESOUSOFFRE (IDSOUSOFFRE, IDFORFAIT);

CREATE SEQUENCE CLIENT_SEQ INCREMENT BY 1 START 1;
CREATE TABLE CLIENT (
    IDCLIENT VARCHAR DEFAULT 'CLT' || NEXTVAL('CLIENT_SEQ'),
    NOMCLIENT VARCHAR NOT NULL,
    CIN VARCHAR (12) NOT NULL,
    NUMERO VARCHAR (13) NOT NULL UNIQUE,
    MOTDEPASSE VARCHAR(40) NOT NULL,
    PRIMARY KEY (IDCLIENT)
);
CREATE UNIQUE INDEX UK_CLIENT ON CLIENT (NUMERO, CIN);

CREATE TABLE ACHATOFFRE(
    IDCLIENT VARCHAR NOT NULL,
    IDOFFRE VARCHAR NOT NULL,
    DATEACHAT TIMESTAMP NOT NULL,
    FOREIGN KEY (IDCLIENT) REFERENCES CLIENT (IDCLIENT),
    FOREIGN KEY (IDOFFRE) REFERENCES OFFRE (IDOFFRE)
);
CREATE UNIQUE INDEX UK_ACHATOFFRE ON ACHATOFFRE (IDCLIENT, IDOFFRE, DATEACHAT);

CREATE TABLE DATA (
    IDCLIENT VARCHAR NOT NULL,
    IDSOUSOFFRE VARCHAR NOT NULL,
    IDFORFAIT VARCHAR NOT NULL,
    DATA DECIMAL (14, 2) NOT NULL,
    DATEEXPIRATION TIMESTAMP,
    FOREIGN KEY (IDCLIENT) REFERENCES CLIENT (IDCLIENT),
    FOREIGN KEY (IDSOUSOFFRE) REFERENCES SOUSOFFRE (IDSOUSOFFRE),
    FOREIGN KEY (IDFORFAIT) REFERENCES FORFAIT(IDFORFAIT)
);
CREATE UNIQUE INDEX UK_DATA ON DATA (IDCLIENT, IDSOUSOFFRE, IDFORFAIT);



CREATE OR REPLACE FUNCTION CLIENT_TRIGGER() RETURNS TRIGGER AS $TRG$
    BEGIN
        INSERT INTO DATA (IDCLIENT, IDSOUSOFFRE, IDFORFAIT, DATA) VALUES (NEW.IDCLIENT, 'DEFAUT', 'TOUS', 0) ;
        RETURN NEW;
    END;
$TRG$ LANGUAGE plpgsql;

CREATE TRIGGER CLIENT_TRIGGER AFTER INSERT ON CLIENT
    FOR EACH ROW EXECUTE PROCEDURE CLIENT_TRIGGER();


CREATE VIEW COMPTE AS
SELECT * FROM 
    CLIENT NATURAL JOIN 
    (SELECT IDCLIENT, MAX(DATA) AS CREDIT FROM DATA WHERE IDSOUSOFFRE='DEFAUT' GROUP BY IDCLIENT ) r;

CREATE VIEW JOUR AS SELECT * FROM (VALUES (1) , (2), (3) , (4), (5), (6), (7)) AS JOUR (JOUR);
CREATE VIEW MOIS AS SELECT * FROM (VALUES (1) , (2), (3) , (4), (5), (6), (7), (8), (9), (10), (11), (12)) AS MOIS (MOIS);

CREATE OR REPLACE VIEW STATOFFREJOURNALIER AS
    SELECT 
        OFFRE.*, 
        CAST(JOUR.JOUR AS VARCHAR) AS LIBELE , 
        COALESCE(NOMBRE, 0) AS NOMBRE 
    FROM OFFRE,JOUR 
        NATURAL LEFT JOIN (SELECT IDOFFRE, EXTRACT(DOW FROM DATEACHAT) JOUR, COUNT(*) NOMBRE FROM ACHATOFFRE GROUP BY IDOFFRE, EXTRACT(DOW FROM DATEACHAT)) R  
    WHERE OFFRE.IDOFFRE !='DEFAUT';


CREATE OR REPLACE VIEW STATOFFREMENSUEL AS 
    SELECT 
        OFFRE.*, 
        CAST(MOIS.MOIS AS VARCHAR) AS LIBELE, 
        COALESCE(NOMBRE, 0) AS NOMBRE 
    FROM OFFRE,MOIS 
        NATURAL LEFT JOIN (SELECT IDOFFRE, EXTRACT(MONTH FROM DATEACHAT) MOIS, COUNT(*) NOMBRE FROM ACHATOFFRE GROUP BY IDOFFRE, EXTRACT(MONTH FROM DATEACHAT)) R  
    WHERE OFFRE.IDOFFRE !='DEFAUT';


CREATE VIEW DEPOTNONVALIDER AS 
    SELECT * FROM TRANSACTION WHERE ETAT = FALSE;

CREATE VIEW CONSOOFFRE AS
    SELECT  DATA.*, SOUSFORFAIT.IDSOUSFORFAIT, SOUSFORFAIT.COUTCREDIT AS COUT FROM DATA NATURAL LEFT JOIN SOUSFORFAIT WHERE IDSOUSOFFRE!='DEFAUT';

CREATE VIEW CONSOCREDIT AS
    SELECT DATA.*, SOUSFORFAIT.IDSOUSFORFAIT, SOUSFORFAIT.COUTCREDIT AS COUT FROM DATA,SOUSFORFAIT WHERE IDSOUSOFFRE='DEFAUT';

/*
    ALAINA DAHOLO NY MONMBAMOMBA ANLE SOUS OFFRE TINY VIDINA
    ALAINA NY DATA ANY PAR RAPPORT AMLE SOUSOFFRE
    NA MI INSERT NA MI UPDATE
*/



 

