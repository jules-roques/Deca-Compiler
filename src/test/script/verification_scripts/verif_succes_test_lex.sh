#! /bin/bash

# Auteur : Jules
# Version initiale : 10/01/2024

# Vérifie la sortie du lexer pour un programme valide lexicalement
# Lire la fiche README de src/test/deca/syntax/valid/created/lexer/README.md
# pour des infos sur la forme du fichier deca à tester

# Chemin vers les scripts que l'on utilise
PATH="$(dirname "$0")"/../../../../src/test/script/launchers:"$PATH"

# Couleurs pour affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# On vérifie qu'il y a un argument passé
if [ "$#" -ne 1 ]
then
    echo "Usage: $0 deca_file"
    exit 1
fi

# Argument passé au programme
deca_file="$1"

# On stocke les résultats de l'exécution du test dans un fichier
# En vérifiant le succès du programme
result_file="${deca_file%.deca}".lis
if ! test_lex "$deca_file" > "$result_file" 2>&1
then
    echo -e "${RED}[ECHEC]  $deca_file: echec inattendu pour test_lex:${NC}"
    cat $result_file | sed 's/^/         /'
    exit 1
fi

# Récupérer les tokens obtenus depuis le fichier résultat
detected_tokens=$(awk '{ print $1 }' "$result_file" | sed 's/:$//')

# Pour récupérer les tokens attendus dans le fichier deca
expected_tokens=$(awk 'NF && NR > 1 { print $2 } \
                /^\/\/[[:space:]]*$/{ exit }' "$deca_file" \
                | head -n -1)  # Suppression de la dernière ligne qui est vide

# Convertit les listes en tableaux
detected_tokens=($detected_tokens)
expected_tokens=($expected_tokens)

# Vérifie qu'il y a autant de tokens détectés qu'attendus
if [ "${#detected_tokens[@]}" -ne "${#expected_tokens[@]}" ]
then
    echo -e "${RED}[ECHEC]  $deca_file génère ${#detected_tokens[@]} tokens, ${#expected_tokens[@]} sont attendus${NC}"
    exit 1
fi

# Compare les tokens attendus et détectés 1 par 1
number_tokens=${#detected_tokens[@]}
for i in $(seq 0 1 $number_tokens)
do
    if [ "${detected_tokens[i]}" != "${expected_tokens[i]}" ]
    then
        echo -e "${RED}[ECHEC]  $deca_file: Token $i non conforme (${expected_tokens[i]} attendu, ${detected_tokens[i]} obtenu)${NC}"
        exit 1
    fi
done
echo -e "${GREEN}[SUCCES] $deca_file: Tokens attendus détectés${NC}"