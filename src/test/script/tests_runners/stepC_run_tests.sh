#! /bin/bash

# Auteur : Jules
# Version initiale : 12/01/2024

# Script de test de l'étape C.
# On teste plusieurs fichiers valides et invalides.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../../.. || exit 1

# Chemin vers les scripts que l'on utilise
PATH=./src/test/script/verification_scripts:"$PATH"

# Couleurs pour affichage
BICYAN='\033[1;96m'
ICYAN='\033[0;96m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color


echo -e "${YELLOW}On stocke la sortie de l'étape C d'un \"fichier.deca\" dans \"fichier.ass\"${NC}"
echo -e "${YELLOW}On stocke la sortie d'exécution par ima d'un \"fichier.ass\" dans \"fichier.res\"${NC}"
echo

# --------------------------- TESTS SUR FICHERS DECA VALIDES ---------------------------
deca_files_rep="src/test/deca/codegen/valid/created"
echo -e "${BICYAN}Compilation et exécution de fichiers deca (dans $deca_files_rep)${NC}"
echo -e "${YELLOW}Compile chaque programme deca et vérifie le résultat de son exécution par ima${NC}"

# Sans objet
echo -e "${ICYAN}         Sans objet${NC}"
program_on_deca_in_folder.sh verif_execution.sh $deca_files_rep/sans_objet | sed 's/^/                  /'
echo 

# Avec objet
echo -e "${ICYAN}         Avec objet${NC}"
program_on_deca_in_folder.sh verif_execution.sh $deca_files_rep/avec_objet | sed 's/^/                  /'

echo