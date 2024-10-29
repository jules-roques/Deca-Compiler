#! /bin/bash

# Auteur : Jules
# Version initiale : 08/01/2024

# Test de la syntaxe.
# On lance test_synt sur des fichiers valides et invalides.

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


echo -e "${YELLOW}On stocke la sortie du parser d'un \"fichier.deca\" dans \"fichier.lis\"${NC}"
echo

# --------------------------- TESTS SUR FICHERS DECA INVALIDES SYNTAXIQUEMENT ---------------------------
deca_files_dir="src/test/deca/syntax/invalid/created"
echo -e "${BICYAN}Invalides syntaxiquement (dans $deca_files_dir)${NC}"
echo -e "${YELLOW}Vérifie la position de l'erreur et le message d'erreur reçu ${NC}"

# Sans objet
echo -e "${ICYAN}         Sans objet${NC}"
program_on_deca_in_folder.sh "verif_echec_test.sh test_synt" $deca_files_dir/sans_objet | sed 's/^/                  /'

# Avec objet
echo -e "${ICYAN}         Avec objet${NC}"
program_on_deca_in_folder.sh "verif_echec_test.sh test_synt" $deca_files_dir/avec_objet | sed 's/^/                  /'

echo

# --------------------------- TESTS SUR FICHIERS DECA VALIDES SYNTAXIQUEMENT ---------------------------
deca_files_dir="src/test/deca/syntax/valid/created"
# echo -e "${BICYAN}Tests sur fichiers deca valides syntaxiquement (dans $deca_files_dir)${NC}"
# echo -e "${YELLOW}Vérifie que l'on a bien l'arbre attendu${NC}"
# echo -e "${YELLOW}(on repère les terminaux de la syntaxe abstraite (avec leur position dans le .deca), ainsi que les listes d'arbres)${NC}"
echo -e "${BICYAN}Valides syntaxiquement (dans $deca_files_dir)${NC}"
echo -e "${YELLOW}Le test ici n'est pas très sûr, on vérifie seulement${NC}"
echo -e "${YELLOW}que chaque programme deca ne renvoie pas une erreur${NC}"
echo -e "${YELLOW}(on ne vérifie pas l'arbre)${NC}"

# Sans objet
echo -e "${ICYAN}         Sans objet${NC}"
#program_on_deca_in_folder.sh "verif_tree.sh test_synt" $deca_files_dir/sans_objet | sed 's/^/                  /'
program_on_deca_in_folder.sh "verif_succes_basic.sh test_synt" $deca_files_dir/sans_objet | sed 's/^/                  /'

# Avec objet
echo -e "${ICYAN}         Avec objet${NC}"
#program_on_deca_in_folder.sh "verif_tree.sh test_synt" $deca_files_dir/avec_objet | sed 's/^/                  /'
program_on_deca_in_folder.sh "verif_succes_basic.sh test_synt" $deca_files_dir/avec_objet | sed 's/^/                  /'

