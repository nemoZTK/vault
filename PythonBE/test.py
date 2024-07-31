import time
from guesslang import Guess

start_time = time.time()
# (Istanzia Guess)
guess = Guess()
load_time = time.time() - start_time

print(f"Tempo impiegato per caricare Guess(): {load_time:.2f} secondi")

def guessThisFile():
    file_path = input("Inserisci il percorso del file: ")
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            file_content = file.read()
        
        # (Identifica il linguaggio)
        language = guess.language_name(file_content)
        print(f"Il linguaggio di programmazione rilevato è: {language}")
    except FileNotFoundError:
        print(f"Il file '{file_path}' non è stato trovato. Riprova.")
        guessThisFile()
    except Exception as e:
        print(f"Si è verificato un errore: {e}")
        guessThisFile()

guessThisFile()
