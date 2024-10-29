#! /bin/bash

# Auteur : Jules
# Version initiale : 12/01/2024

# Script de test de l'étape A.
# On teste plusieurs fichiers valides et invalides.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../../.. || exit 1

# Chemin vers les scripts que l'on utilise
PATH=./src/test/script/tests_runners:"$PATH"

# Couleurs pour affichage
BICYAN='\033[1;96m'
NC='\033[0m' # No Color

echo -e "${BICYAN}Test Lexer${NC}"
stdbuf -oL lex_run_tests.sh | sed 's/^/         /'
echo

echo -e "${BICYAN}Test Parser${NC}"
stdbuf -oL synt_run_tests.sh | sed 's/^/         /'