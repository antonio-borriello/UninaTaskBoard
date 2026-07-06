
INSERT INTO Utente (nickname, nome, cognome, email, password) VALUES
('chris', 'Christian', 'Auriemma', 'christian.auriemma@nerv.com', 'password123'),
('antonio', 'Antonio', 'Borriello', 'antonio.borriello@nerv.com', 'password123'),
('shinji', 'Shinji', 'Ikari', 'shinji.ikari@nerv.com', 'pilot01'),
('rei', 'Rey', 'Ayanami', 'rey.ayanami@nerv.com', 'pilot00'),
('gendo', 'Gendo', 'Ikari', 'commander@nerv.com', 'topsecret');



INSERT INTO Progetto (titolo, dataCreazione, estensione, nomeFile, percorso, tipoDocumentazione, tipoProgetto, creatore_nickname) VALUES
-- Progetto di Sviluppo (MAGI System)
('Sincronizzazione Sistema Operativo MAGI', '2026-06-01', '.cpp', 'magi_core', '/opt/nerv/magi/src/', NULL, 'AttivitàSviluppo', 'gendo'),
-- Progetto di Documentazione (Progetto Strumentazione Umana)
('Protocollo Progetto Perfezionamento Umano', '2026-06-15', NULL, NULL, NULL, 'PDF', 'Documentazione', 'gendo');



-- Progetto 1 (MAGI): Gendo (creatore) invita Christian e Antonio. Tutti accettano.
INSERT INTO Partecipazione (utente_nickname, progetto_id, statoInvito, dataInvito, dataScadenza) VALUES
('gendo', 1, 'Accettato', '2026-06-01', '2026-06-08'),
('chris', 1, 'Accettato', '2026-06-02', '2026-06-09'),
('antonio', 1, 'Accettato', '2026-06-02', '2026-06-09');

-- Progetto 2 (Perfezionamento): Gendo invita Rey e Shinji. Rey accetta, Shinji In attesa
INSERT INTO Partecipazione (utente_nickname, progetto_id, statoInvito, dataInvito, dataScadenza) VALUES
('gendo', 2, 'Accettato', '2026-06-15', '2026-06-22'),
('rei', 2, 'Accettato', '2026-06-16', '2026-06-23'),
('shinji', 2, 'In attesa', '2026-06-16', '2026-06-23');



-- Progetto 1 (MAGI)
INSERT INTO Attivita (titolo, descrizione, dataCreazione, dataScadenza, statoAvanzamento, infoSpecifiche, progetto_id) VALUES
('Calibrazione Supercomputer Casper', 'Sincronizzazione del modulo logico Casper con il server centrale della NERV.', '2026-06-05', '2026-06-20', 'Completata', 'Priorità Alfa', 1),
('Debugging Modulo Balthasar', 'Risoluzione kernel panic sul modulo Balthasar causato da intromissione esterna.', '2026-06-10', '2026-06-25', 'In corso', 'Errore alla riga 402 - Timeout di rete', 1),
('Deploy Firmware Melchior', 'Installazione dell''ultimo aggiornamento per i sensori biometrici.', '2026-06-20', '2026-07-10', 'Da fare', 'Richiede approvazione Dott.ssa Akagi', 1);

-- Progetto 2 (Perfezionamento)
INSERT INTO Attivita (titolo, descrizione, dataCreazione, dataScadenza, statoAvanzamento, infoSpecifiche, progetto_id) VALUES
('Stesura Rapporto Densità LCL', 'Creazione del documento ufficiale sui livelli di saturazione ottimali del liquido LCL per i piloti.', '2026-06-16', '2026-06-30', 'Completata', 'Uso template Word aziendale', 2),
('Traduzione Manoscritti del Mar Morto', 'Analisi e formattazione dei nuovi frammenti estratti dal QG.', '2026-06-18', '2026-07-05', 'In corso', 'TOP SECRET: Non condividere via email', 2);


-- Assegnazioni Progetto MAGI
INSERT INTO Assegnazione (utente_nickname, attivita_id) VALUES
('chris', 1),    
('antonio', 1), 
('chris', 2),    
('gendo', 3);    

-- Assegnazioni Progetto Perfezionamento
INSERT INTO Assegnazione (utente_nickname, attivita_id) VALUES
('rei', 4),      
('gendo', 5);   
