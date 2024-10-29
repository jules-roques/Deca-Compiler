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

# --------------------------- TESTS SUR FICHERS DECA INVALIDES CONTEXTUELLEMENT ---------------------------
deca_files_dir=src/test/deca/context/invalid/created
echo -e "${BICYAN}Invalides contextuellement (dans $deca_files_dir)${NC}"
echo -e "${YELLOW}Vérifie la position de l'erreur et le message d'erreur reçu ${NC}"

# Répertoires des règles
rules_directories=($(find $deca_files_dir -type d -name 'rule*' | sort))
for rule_folder in ${rules_directories[@]}
do
    echo -e "${ICYAN}         Règle $(basename $rule_folder | sed 's/rule//')${NC}"
    program_on_deca_in_folder.sh "verif_echec_test.sh test_context" $rule_folder | sed 's/^/                  /'
done