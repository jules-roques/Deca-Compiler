#! /bin/sh

# Auteur : gl44
# Version initiale : 01/01/2024

# Base pour un script de test de la lexicographie.
# On teste un fichier valide et un fichier invalide.
# Il est conseillé de garder ce fichier tel quel, et de créer de
# nouveaux scripts (en s'inspirant si besoin de ceux fournis).

# Il faudrait améliorer ce script pour qu'il puisse lancer test_lex
# sur un grand nombre de fichiers à la suite.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


PATH_TO_VALID_DECA_FILES="src/test/deca/lexicon"



# Use 'find' to recursively test all .deca files in the specified directory
find "$PATH_TO_VALID_DECA_FILES" -name "*.deca" -exec sh -c '
    for deca_file do
        # Test each .deca file and check for errors
        if test_syntax "$deca_file" 2>&1 | grep -q -e "$deca_file:[0-9]"; then
            echo "Echec inattendu pour test_syntax avec $deca_file"
            # Uncomment the next line if you want the script to exit on the first failure
            # exit 1
        else
            echo "Succes attendu de test_syntax avec $deca_file"
        fi
    done
' find-sh {} +




