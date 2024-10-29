#! /bin/bash

# Auteur : Jules
# Version initiale : 11/01/2024

# Vérifie le succès d'un test lancé sur un programme deca
#
# Prend en params:
# - le test
# - le fichier deca sur lequel on va exécuter le test

# Chemin vers les scripts que l'on utilise
PATH="$(dirname "$0")"/../../../../src/test/script/launchers:"$PATH"

# Couleurs pour affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# On vérifie qu'il y a deux arguments passés
if [ "$#" -ne 2 ]
then
    echo "Usage: $0 test deca_file"
    exit 1
fi

# Arguments passés au programme
test="$1"
deca_file="$2"

# On stocke les résultats de l'exécution du test dans un fichier en vérifiant le succès du programme
result_file="${deca_file%.deca}".lis
if ! $test "$deca_file" > "$result_file" 2>&1
then
    echo -e "${RED}[ECHEC]  $deca_file: Echec inattendu pour $test:${NC}"
    cat $result_file | sed 's/^/         /'
    exit 1
fi

echo -e "${GREEN}[SUCCES] $deca_file: l'exécution de $test n'échoue pas${NC}"