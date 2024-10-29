#! /bin/sh

# Auteur : Jules
# Version initiale : 08/01/2024
#
# Prend en arguments
#  - le type de test effectué (non d'un programme du dossier voisin "launchers")
#  - le nom du fichier deca à tester
#
# Vérifie si un certain script test (dossier launchers) lancé sur un programme deca incorrect
# renvoie en effet une erreur.
# Autrement dit on vérifie que le test renseigné (par ex test_lex, test_synt, ...)
# rejette le programme deca incorrect
#
# /!\ La deuxième ligne du programme deca incorrect
# /!\ doit contenir (en commentaires) la ligne de l'erreur
#
# Exemple: on teste le programme
#           // Ligne erreur:
#           //    3
#           if idf {print("Hello")}
#
# Avec test_lex:  succès de test_lex car le programme correct lexicalement     -> echec de verif_echec_test
# Avec test_synt: echec de test_synt car le programme incorrect syntaxiquement -> succès de verif_echec_test

# Chemin vers les scripts que l'on utilise
PATH="$(dirname "$0")"/../../../../src/test/script/launchers:"$PATH"

# Couleurs pour affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# On vérifie qu'il y a deux arguments passés
if [ "$#" -ne 2 ]
then
    echo "Usage: $0 launcher_test deca_file"
    exit 1
fi

# Explicitation des arguments passés
test="$1"
deca_file="$2"

# On stocke les résultats de l'exécution du test dans un fichier
# En vérifiant qu'il a bien échoué
result_file="${deca_file%.deca}".lis
if $test "$deca_file" > "$result_file" 2>&1
then
    echo "${RED}[ECHEC]  $deca_file: Succes inattendu de $test:${NC}"
    cat $result_file | sed 's/^/         /'
    exit 1
fi

# On récupère la ligne supposée de l'erreur
theorical_err_pos=$(sed -n '2p' $deca_file | grep -o "[0-9]\+:[0-9]\+" | head -n 1) || exit 1

# On récup l'éventuelle chaine dont on veut vérif la présence dans le message d'erreur
error_info=$(awk 'NR==3 {sub(/^\/\/[[:space:]]*/, "", $0); print}' $deca_file) || exit 1

# Ligne d'erreur détectée (peut être vide s'il n'y a pas d'erreur)
detected_err_pos=$(cat "$result_file" | grep -o "$deca_faile_name:[0-9]\+:[0-9]\+" | grep -o "[0-9]\+:[0-9]\+")

# Différentes issues en fonction de s'il y a une erreur
# Et de si elle est détectée à l'endroit attendu
# Avec une éventuelle information à l'intérieur
if [ -n "$detected_err_pos" ]
then
    if [ "$detected_err_pos" = "$theorical_err_pos" ]
    then
        if ! grep -q "$error_info" "$result_file"; then
            echo "${RED}[ECHEC]  $deca_file: Information \"$error_info\" non détectée dans le message d'erreur pour $test:${NC}"
            cat $result_file | sed 's/^/         /'
            exit 1
        fi
        echo "${GREEN}[SUCCES] $deca_file: Erreur attendue pour $test en position $detected_err_pos ${NC}"
    else
        echo "${RED}[ECHEC]  $deca_file: Erreur à la mauvaise position pour $test (attendu $theorical_err_pos, détecté $detected_err_pos):${NC}"
        cat $result_file | sed 's/^/         /'
        exit 1
    fi
else
    echo "${RED}[ECHEC]  $deca_file: Erreur non liée au programme deca pour $test:${NC}"
    cat $result_file | sed 's/^/         /'
    exit 1
fi