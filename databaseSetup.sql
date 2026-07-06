DROP TABLE IF EXISTS Assegnazione CASCADE;
DROP TABLE IF EXISTS Partecipazione CASCADE;
DROP TABLE IF EXISTS Attivita CASCADE;
DROP TABLE IF EXISTS Progetto CASCADE;
DROP TABLE IF EXISTS Utente CASCADE;
--Reset del DataBase e cancellazione dei vincoli se queste tabelle già esistono così da evitare futuri conflitti


CREATE TABLE Utente (
    nickname VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL
);


CREATE TABLE Progetto (
    id_progetto SERIAL PRIMARY KEY, --Serial incrementa l'id automaticamente, l'ho visto su bytebase.com
    titolo VARCHAR(200) NOT NULL,
    dataCreazione DATE NOT NULL,
    estensione VARCHAR(50),
    nomeFile VARCHAR(255),
    percorso VARCHAR(500),
    tipoDocumentazione VARCHAR(100),
    creatore_nickname VARCHAR(50) NOT NULL,
    CONSTRAINT fk_progetto_creatore FOREIGN KEY (creatore_nickname) REFERENCES Utente(nickname) ON DELETE CASCADE
); --Vincolo che impone le foreign key e acosa si riferisce e nel caso viene eliminata elimina tutto a cascata



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


CREATE TABLE Attivita (
    id_attivita SERIAL PRIMARY KEY, 
    titolo VARCHAR(200) NOT NULL,
    descrizione TEXT, 
    dataCreazione DATE NOT NULL,
    dataScadenza DATE,
    statoAvanzamento VARCHAR(50) NOT NULL, -- es: "Da fare", "In corso", "Completata"
    tipoAttivita VARCHAR(50) NOT NULL, -- es: "Documentazione", "Sviluppo"
    infoSpecifiche TEXT, 
    progetto_id INT NOT NULL,
    CONSTRAINT fk_attivita_progetto FOREIGN KEY (progetto_id) REFERENCES Progetto(id_progetto) ON DELETE CASCADE
);


CREATE TABLE Assegnazione (
    utente_nickname VARCHAR(50),
    attivita_id INT,
    dataCreazione DATE NOT NULL,
    PRIMARY KEY (utente_nickname, attivita_id),
    CONSTRAINT fk_assegnazione_utente FOREIGN KEY (utente_nickname) REFERENCES Utente(nickname) ON DELETE CASCADE,
    CONSTRAINT fk_assegnazione_attivita FOREIGN KEY (attivita_id) REFERENCES Attivita(id_attivita) ON DELETE CASCADE
);

-- 1. Trigger per il controllo temporale dell'Attività
-- Garantisce che la dataCreazione e la dataScadenza di un'attività 
-- non siano antecedenti alla data di creazione del progetto associato.
CREATE OR REPLACE FUNCTION check_date_attivita()
RETURNS TRIGGER AS $$
DECLARE
    v_progetto_id          Attivita.progetto_id%TYPE;
    v_data_creazione       Attivita.dataCreazione%TYPE;
    v_data_scadenza        Attivita.dataScadenza%TYPE;
    v_data_creazione_prog  Progetto.dataCreazione%TYPE;
BEGIN
 
    v_progetto_id    := NEW.progetto_id;
    v_data_creazione := NEW.dataCreazione;
    v_data_scadenza  := NEW.dataScadenza;


    SELECT dataCreazione INTO v_data_creazione_prog
    FROM Progetto
    WHERE id_progetto = v_progetto_id;

    -- L'attività non può essere creata prima del progetto
    IF v_data_creazione < v_data_creazione_prog THEN
        RAISE EXCEPTION 'La data di creazione dell''attività non può essere precedente a quella del progetto.';
    END IF;

    -- La scadenza dell'attività non può essere precedente a quella del progetto
    IF v_data_scadenza IS NOT NULL AND v_data_scadenza < v_data_creazione_prog THEN
        RAISE EXCEPTION 'La data di scadenza dell''attività non può essere precedente alla creazione del progetto.';
    END IF;

    --La scadenza non può essere precedente alla creazione dell'attività
    IF v_data_scadenza IS NOT NULL AND v_data_scadenza < v_data_creazione THEN
        RAISE EXCEPTION 'La data di scadenza non può essere precedente alla data di creazione dell''attività.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_date_attivita ON Attivita;
CREATE TRIGGER trg_check_date_attivita
BEFORE INSERT OR UPDATE ON Attivita
FOR EACH ROW EXECUTE FUNCTION check_date_attivita();


-- 2. Trigger per il controllo dell'Assegnazione
-- Garantisce che un utente possa essere assegnato a un'attività 
-- SOLO SE fa parte del progetto (è il creatore oppure ha accettato l'invito).
CREATE OR REPLACE FUNCTION check_assegnazione_utente()
RETURNS TRIGGER AS $$
DECLARE
    v_attivita_id      Assegnazione.attivita_id%TYPE;
    v_utente_nickname  Assegnazione.utente_nickname%TYPE;
    v_progetto_id      Attivita.progetto_id%TYPE;
    v_creatore         Progetto.creatore_nickname%TYPE;
    v_is_membro        BOOLEAN;
BEGIN
 
    v_attivita_id     := NEW.attivita_id;
    v_utente_nickname := NEW.utente_nickname;

  
    SELECT progetto_id INTO v_progetto_id FROM Attivita WHERE id_attivita = v_attivita_id;
    SELECT creatore_nickname INTO v_creatore FROM Progetto WHERE id_progetto = v_progetto_id;

    -- Se l'utente che stiamo assegnando è il creatore del progetto, va bene.
    IF v_utente_nickname = v_creatore THEN
        RETURN NEW;
    END IF;

    -- Altrimenti, controlliamo se l'utente ha una Partecipazione "Accettato" per quel progetto
    SELECT EXISTS (
        SELECT 1 FROM Partecipazione 
        WHERE utente_nickname = v_utente_nickname 
          AND progetto_id = v_progetto_id 
          AND statoInvito = 'Accettato'
    ) INTO v_is_membro;

    IF NOT v_is_membro THEN
        RAISE EXCEPTION 'L''utente % non può essere assegnato perché non è un membro attivo del progetto.', v_utente_nickname;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_assegnazione_utente ON Assegnazione;
CREATE TRIGGER trg_check_assegnazione_utente
BEFORE INSERT ON Assegnazione
FOR EACH ROW EXECUTE FUNCTION check_assegnazione_utente();


-- 3. Trigger per il controllo della Scadenza dell'Invito
-- Impedisce a un utente di accettare un invito se la data di scadenza è già passata.
CREATE OR REPLACE FUNCTION check_scadenza_invito()
RETURNS TRIGGER AS $$
DECLARE
    v_nuovo_stato    Partecipazione.statoInvito%TYPE;
    v_vecchio_stato  Partecipazione.statoInvito%TYPE;
    v_data_scadenza  Partecipazione.dataScadenza%TYPE;
BEGIN

    v_nuovo_stato   := NEW.statoInvito;
    v_vecchio_stato := OLD.statoInvito;
    v_data_scadenza := NEW.dataScadenza;

    -- Se l'utente sta cercando di accettare l'invito
    IF v_nuovo_stato = 'Accettato' AND v_vecchio_stato = 'In attesa' THEN
        -- Controlla se la data odierna ha superato la scadenza
        IF CURRENT_DATE > v_data_scadenza THEN
            RAISE EXCEPTION 'Impossibile accettare l''invito: la data di scadenza (%) è già passata.', v_data_scadenza; --(%) ci serve per inserire la variabile 
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_scadenza_invito ON Partecipazione;
CREATE TRIGGER trg_check_scadenza_invito
BEFORE UPDATE ON Partecipazione
FOR EACH ROW EXECUTE FUNCTION check_scadenza_invito();


-- 4. Trigger per la Protezione delle Attività Completate
-- Impedisce l'eliminazione accidentale di un'attività se è già nello stato "Completata"
CREATE OR REPLACE FUNCTION prevent_delete_attivita_completata()
RETURNS TRIGGER AS $$
DECLARE
    v_stato_vecchio Attivita.statoAvanzamento%TYPE;
BEGIN

    v_stato_vecchio := OLD.statoAvanzamento;

    IF v_stato_vecchio = 'Completata' THEN
        RAISE EXCEPTION 'Operazione non consentita: le attività completate non possono essere eliminate per motivi di tracciamento.';
    END IF;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_delete_attivita_completata ON Attivita;
CREATE TRIGGER trg_prevent_delete_attivita_completata
BEFORE DELETE ON Attivita
FOR EACH ROW EXECUTE FUNCTION prevent_delete_attivita_completata();

-- 5. Procedura per la pulizia degli inviti scaduti
CREATE OR REPLACE PROCEDURE rimuovi_miei_inviti_scaduti(p_nickname Utente.nickname%TYPE)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM Partecipazione 
    WHERE utente_nickname = p_nickname 
      AND (
          statoInvito = 'Rifiutato' 
          OR 
          (statoInvito = 'In attesa' AND dataScadenza < CURRENT_DATE)
      );
END;
$$;
