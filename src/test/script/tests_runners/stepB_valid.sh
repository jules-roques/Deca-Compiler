#! /bin/bash

# Auteur : Jules
# Version initiale : 12/01/2024

# Script de test de l'étape B.
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


echo -e "${YELLOW}On stocke la sortie de l'étape B d'un \"fichier.deca\" dans \"fichier.lis\"${NC}"
echo



# --------------------------- TESTS SUR FICHIERS DECA VALIDES CONTEXTUELLEMENT ---------------------------
deca_files_dir="src/test/deca/context/valid/created"
echo -e "${BICYAN}Valides contextuellement (dans $deca_files_dir)${NC}"
echo -e "${YELLOW}Le test ici n'est pas très sûr, on vérifie seulement${NC}"
echo -e "${YELLOW}que chaque programme deca ne renvoie pas une erreur${NC}"
echo -e "${YELLOW}(on ne vérifie pas l'arbre et ses décorations)${NC}"

# Sans objet
echo -e "${ICYAN}         Sans objet (dans $deca_files_dir/sans_objet)${NC}"
program_on_deca_in_folder.sh "verif_succes_basic.sh test_context" $deca_files_dir/sans_objet | sed 's/^/                  /'

# Avec objet
echo -e "${ICYAN}         Avec objet (dans $deca_files_dir/avec_objet)${NC}"
program_on_deca_in_folder.sh "verif_succes_basic.sh test_context" $deca_files_dir/avec_objet | sed 's/^/                  /'
