# vault
vault project


benvenuti nel progetto vault.

IN BREVE

l'idea è quella di un sito che permetta agli utenti di ospitare documenti e progetti

1) tramite sito l'utente potrà creare un argomento(repo) 
e avrà 2 spazi distinti relativi a quell'argomento, 
-uno per il codice 
-uno per i documenti


2) quando un utente crea un nuovo argomento(repo) 
vengono automaticamente create queste 3 cartelle 
(username/nome-repo, nome-repo/docs, nome-repo/codes ) 

3) l'utente potrà caricare file o intere cartelle 
specificando se esse siano codice o documenti,
una volta caricate le potrà consultare nel suo spazio personale tramite sito 
da dove potrà ri-scaricarle o visualizzarle.

4) i file/cartelle dichiarate come codice verranno analizzati per determinarne il linguaggio, dopodichè l'informazione verrà associata a quei file e salvata nel database.



5) l'utente nel sito avrà la possibilità di navigare tra 2 sezioni principali,
una in cui troverà i suoi argomenti(repo) divisi come descritto,
in un altra invece visualizzerà la sua intera codebase, divisa per linguaggi. 


6) nella sezione di uno specifico linguaggio troverà cartelle e file associati a quel determinato linguaggio.

7) se viene caricata una cartella dichiarata come linguaggio, viene analizzata, e, se nella cartella viene riconosciuto più di un linguaggio,  tutti i file della cartella verranno associati al linguaggio che raggiunge il 70% del totale file di quella cartella


8) è previsto che l'utente possa creare n sottocartelle nel suo repository,
creandole nella sezione "docs" o "codes",
ma la sua capacità di creazione sarà limitata a queste 2 directory 


9) non è previsto che l'utente possa modificare i file dal sito, 
potrà però sovrascaricare file o cartelle già esistenti nel suo spazio.

10) per ora non è previsto alcun sistema di collaborazione/scambio tra utenti. lo spazio è personale.







