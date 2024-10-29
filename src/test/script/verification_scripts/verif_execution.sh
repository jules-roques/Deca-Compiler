#! /bin/bash

# Auteur : Jules
# Version initiale : 15/01/2024
#
# Prend en arguments
#  - le nom du programme deca dont on veut vérifier l'exécution


# Chemin vers les scripts que l'on utilise
PATH="$(dirname "$0")"/../../../../src/main/bin:"$PATH"

# Couleurs pour affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color



# On vérifie qu'il y a un argument passé
[ "$#" -eq 1 ] || die "Usage: $0 deca_file"

# Explicitation des arguments passés
deca_file="$1"

# On stocke les résultats de la compilation du test dans un fichier
ass_file="${deca_file%.deca}".ass
if ! decac -r 4 "$deca_file" > "$ass_file" 2>&1; then
    echo -e "${RED}[ECHEC]  $deca_file: Echec de compilation: ${NC}"
    sed 's/^/         /' $ass_file
    rm $ass_file
    exit 1
fi

# Résultat de l'exécution par ima
res_file="${deca_file%.deca}".res
if ! ima "$ass_file" > "$res_file" 2>&1; then
    echo -e "${RED}[ECHEC]  $ass_file: Echec d'exécution par ima${NC}"
    sed 's/^/         /' $res_file
    rm $res_file
    exit 1
fi


# Comparaison avec le résultat attendu
exp_file="${deca_file%.deca}".exp

# Si le fichier de résultat existe et qu'il n'est pas vide
if [ -s "$exp_file" ]; then
    if [ -n "$(diff "$exp_file" "$res_file")" ]; then
        echo -e "${RED}[ECHEC]  $deca_file: Le résultat de l'exécution n'est pas celui attendu${NC}"
        echo "               Obtenu:"
        sed 's/^/         /' $res_file
        echo
        echo "               Attendu:"
        sed 's/^/         /' $exp_file
        echo
        exit 1
    fi
    echo -e "${GREEN}[SUCCES] $deca_file: Résultat d'exécution conforme à celui attendu${NC}"
else
    if [ -s "$res_file" ]; then
        echo -e "${RED}[ECHEC]  $deca_file: L'exécution ne devrait pas produire de sortie${NC}"
        echo "               Obtenu:"
        sed 's/^/         /' $res_file
        echo
        exit 1
    fi
    echo -e "${GREEN}[SUCCES] $deca_file: L'exécution ne produit pas de sortie${NC}"
    rm $res_file
fi

