#! /bin/bash

# Auteur : Jules
# Version initiale : 08/01/2024
#
# Exécute un programme sur tous les fichiers deca d'un repertoires lancés par un certain test (dossier launchers)
# dont la sortie est vérifiée par un programme
#
# Prend en arguments
#  - le nom du programme qui vérifie la sortie d'un test ainsi que le test
#       ex: "verif_succes_test_lex.sh" ou "verif_echec_test.sh test_lex"
#  - le nom du repertoire des dossiers deca à tester (depuis le répertoire principal du projet)

# On vérifie le nombre d'arguments passés
if [ "$#" -ne 2 ]
then
    echo "Usage: $0 program folder"
    exit 1
fi

# Explicitation des arguments
program="$1"
folder="$2"

PATH="$(pwd)/src/test/script/verification_scripts:"$PATH

# Pour avoir le dernier argument passé au programme
cd "$folder" || exit 1

deca_files=($(find . -type f -name '*.deca' -printf "%P\n" | sort))
if [ -z "$deca_files" ]; then
    echo "Pas de fichiers '.deca' trouvés dans $folder"
else
    for deca_file in "${deca_files[@]}"; do
        $program "$deca_file"
    done
fi