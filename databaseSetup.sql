DROP TABLE IF EXISTS Assegnazione CASCADE;
DROP TABLE IF EXISTS Partecipazione CASCADE;
DROP TABLE IF EXISTS Attivita CASCADE;
DROP TABLE IF EXISTS Progetto CASCADE;
DROP TABLE IF EXISTS Utente CASCADE;
--Reset del DataBase e cancellazione dei vincoli se queste tabelle già esistono così da evitare futuri conflitti

-- Tabella Utente
CREATE TABLE Utente (
    nickname VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL
);

-- Tabella Progetto
CREATE TABLE Progetto (
    id_progetto SERIAL PRIMARY KEY, --Serial incrementa l'id automaticamente, l'ho visto su bytebase.com
    titolo VARCHAR(200) NOT NULL,
    dataCreazione DATE NOT NULL,
    estensione VARCHAR(50),
    nomeFile VARCHAR(255),
    percorso VARCHAR(500),
    tipoDocumentazione VARCHAR(100),
    tipoProgetto VARCHAR(50) NOT NULL, -- es: "Sviluppo", "Ricerca", ecc.
    creatore_nickname VARCHAR(50) NOT NULL,
    CONSTRAINT fk_progetto_creatore FOREIGN KEY (creatore_nickname) REFERENCES Utente(nickname) ON DELETE CASCADE
); --Vincolo che impone le foreign key e acosa si riferisce e nel caso viene eliminata elimina tutto a cascata
    -- Uguale sotto

-- Tabella Partecipazione (Associazione tra Utente e Progetto)
CREATE TABLE Partecipazione (
    utente_nickname VARCHAR(50),
    progetto_id INT,
    statoInvito VARCHAR(20) NOT NULL, -- es: "In attesa", "Accettato", "Rifiutato"
    dataInvito DATE NOT NULL,
    dataScadenza DATE NOT NULL,
    PRIMARY KEY (utente_nickname, progetto_id),
    CONSTRAINT fk_partecipazione_utente FOREIGN KEY (utente_nickname) REFERENCES Utente(nickname) ON DELETE CASCADE,
    CONSTRAINT fk_partecipazione_progetto FOREIGN KEY (progetto_id) REFERENCES Progetto(id_progetto) ON DELETE CASCADE
);

-- Tabella Attivita
CREATE TABLE Attivita (
    id_attivita SERIAL PRIMARY KEY, -- stessa cosa di sopra per il serial
    titolo VARCHAR(200) NOT NULL,
    descrizione TEXT, --Non ho usato varchar perchè è inutile limitare la quantità di char
    dataCreazione DATE NOT NULL,
    dataScadenza DATE,
    statoAvanzamento VARCHAR(50) NOT NULL, -- es: "Da fare", "In corso", "Completata"
    tipoAttivita VARCHAR(50) NOT NULL, -- es: "Documentazione", "Sviluppo"
    infoSpecifiche TEXT, --stessa cosa qui per il text
    progetto_id INT NOT NULL,
    CONSTRAINT fk_attivita_progetto FOREIGN KEY (progetto_id) REFERENCES Progetto(id_progetto) ON DELETE CASCADE
);

-- Tabella Assegnazione (Associazione tra Utente e Attivita)
CREATE TABLE Assegnazione (
    utente_nickname VARCHAR(50),
    attivita_id INT,
    dataCreazione DATE NOT NULL,
    PRIMARY KEY (utente_nickname, attivita_id),
    CONSTRAINT fk_assegnazione_utente FOREIGN KEY (utente_nickname) REFERENCES Utente(nickname) ON DELETE CASCADE,
    CONSTRAINT fk_assegnazione_attivita FOREIGN KEY (attivita_id) REFERENCES Attivita(id_attivita) ON DELETE CASCADE
);

