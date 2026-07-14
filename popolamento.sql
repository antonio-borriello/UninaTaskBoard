
TRUNCATE TABLE Utente CASCADE;  --elimina tutte le indormazioni nella table 
ALTER SEQUENCE progetto_id_progetto_seq RESTART WITH 1; --serve a settare l'id con serial e si attige proprio con questa nomenclatura
ALTER SEQUENCE attivita_id_attivita_seq RESTART WITH 1;


INSERT INTO Utente (nickname, nome, cognome, email, password) VALUES
('chris', 'Christian', 'Auriemma', 'christian.auriemma@nerv.com', 'password123'),
('antonio', 'Antonio', 'Borriello', 'antonio.borriello@nerv.com', 'password123'),
('shinji', 'Shinji', 'Ikari', 'shinji.ikari@nerv.com', 'pilot01'),
('rei', 'Rey', 'Ayanami', 'rey.ayanami@nerv.com', 'pilot00'),
('gendo', 'Gendo', 'Ikari', 'commander@nerv.com', 'topsecret');


INSERT INTO Progetto (titolo, dataCreazione, estensione, nomeFile, percorso, tipoDocumentazione, tipoProgetto, creatore_nickname) VALUES
('Sincronizzazione Sistema Operativo MAGI', '2026-06-01', '.cpp', 'magi_core', '/opt/nerv/magi/src/', NULL, 'AttivitàSviluppo', 'gendo'),
('Protocollo Progetto Perfezionamento Umano', '2026-06-15', NULL, NULL, NULL, 'PDF', 'Documentazione', 'gendo');


INSERT INTO Partecipazione (utente_nickname, progetto_id, statoInvito, dataInvito, dataScadenza) VALUES
('gendo', 1, 'Accettato', '2026-06-01', '2026-06-08'),
('chris', 1, 'Accettato', '2026-06-02', '2026-06-09'),
('antonio', 1, 'Accettato', '2026-06-02', '2026-06-09'),
('gendo', 2, 'Accettato', '2026-06-15', '2026-06-22'),
('rei', 2, 'Accettato', '2026-06-16', '2026-06-23'),
('shinji', 2, 'In attesa', '2026-06-16', '2026-06-23');


INSERT INTO Attivita (titolo, descrizione, dataCreazione, dataScadenza, statoAvanzamento, infoSpecifiche, progetto_id) VALUES
('Calibrazione Supercomputer Casper', 'Sincronizzazione...', '2026-06-05', '2026-06-20', 'Completata', 'Priorità Alfa', 1),
('Debugging Modulo Balthasar', 'Risoluzione kernel panic...', '2026-06-10', '2026-06-25', 'In corso', 'Errore 402', 1),
('Deploy Firmware Melchior', 'Installazione sensori...', '2026-06-20', '2026-07-10', 'Da fare', 'Richiede approvazione', 1),
('Stesura Rapporto Densità LCL', 'Creazione documento...', '2026-06-16', '2026-06-30', 'Completata', 'Template Word', 2),
('Traduzione Manoscritti del Mar Morto', 'Analisi...', '2026-06-18', '2026-07-05', 'In corso', 'TOP SECRET', 2);


INSERT INTO Assegnazione (utente_nickname, attivita_id, dataCreazione) VALUES
('chris', 1, '2026-06-05'),
('antonio', 1, '2026-06-05'),
('chris', 2, '2026-06-10'),
('gendo', 3, '2026-06-20'),
('rei', 4, '2026-06-16'),
('gendo', 5, '2026-06-18');