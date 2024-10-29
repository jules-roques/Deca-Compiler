#! /bin/bash

# Auteur : Jules
# Version initiale : 12/01/2024

# Script de test de l'étape B.
# On teste plusieurs fichiers valides et invalides.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../../.. || exit 1

# Chemin vers les scripts que l'on utilise
PATH=./src/test/script/tests_runners:"$PATH"

# Couleurs pour affichage
BICYAN='\033[1;96m'
ICYAN='\033[0;96m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color


echo -e "${YELLOW}On stocke la sortie de l'étape B d'un \"fichier.deca\" dans \"fichier.lis\"${NC}"
echo

# --------------------------- TESTS SUR FICHERS DECA INVALIDES CONTEXTUELLEMENT ---------------------------
stepB_invalid.sh
echo

# --------------------------- TESTS SUR FICHIERS DECA VALIDES CONTEXTUELLEMENT ---------------------------
stepB_valid.sh