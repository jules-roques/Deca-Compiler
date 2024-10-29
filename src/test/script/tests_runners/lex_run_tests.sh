#!/bin/bash

# Auteur : Jules
# Version initiale : 09/01/2024

# Script de test de la lexicographie.
# On teste plusieurs fichiers valides et invalides.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../../.. || exit 1

# Chemin vers les scripts que l'on utilise
PATH=./src/test/script/verification_scripts:"$PATH"


# Couleurs pour affichage
BICYAN='\033[1;96m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}On stocke la sortie du lexer d'un \"fichier.deca\" dans \"fichier.lis\"${NC}"
echo

# TESTS SUR FICHERS DECA INVALIDES LEXICALEMENT
deca_files_rep="src/test/deca/lexicon/invalid"
echo -e "${BICYAN}Invalides lexicalement (dans $deca_files_rep)${NC}"
echo -e "${YELLOW}Vérifie la position de l'erreur et le message d'erreur reçu ${NC}"
program_on_deca_in_folder.sh "verif_echec_test.sh test_lex" $deca_files_rep | sed 's/^/         /'
echo


# TESTS SUR FICHIERS DECA VALIDES LEXICALEMENT (mais invalides syntaxiquement)
deca_files_rep="src/test/deca/lexicon/valid"
echo -e "${BICYAN}Valides lexicalement (dans $deca_files_rep)${NC}"
echo -e "${YELLOW}Vérifie que l'on a bien les tokens attendus${NC}"
program_on_deca_in_folder.sh verif_succes_test_lex.sh $deca_files_rep | sed 's/^/         /'






