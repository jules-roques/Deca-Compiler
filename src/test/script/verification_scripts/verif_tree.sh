#! /bin/bash

# Auteur : Jules
# Version initiale : 11/01/2024

# Vérifie la sortie du parser ou de l'étape B pour un programme syntaxiquement correct
# (Vérifie que l'arbre a les informations cruciales au bons endroits)
#
# Prend en params:
# - le test affichant un arbre (soit test_synt soit test_contest)
# - le fichier deca dont on veut voir l'arbre (décoré ou non)

# Chemin vers les scripts que l'on utilise
PATH="$(dirname "$0")"/../../../../src/test/script/launchers:"$PATH"

# Couleurs pour affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# On vérifie qu'il y a deux arguments passés
if [ "$#" -ne 2 ]
then
    echo "Usage: $0 test deca_file"
    exit 1
fi

# Arguments passés au programme
test="$1"
deca_file="$2"

# On stocke les résultats de l'exécution du test dans un fichier en vérifiant le succès du programme
result_file="${deca_file%.deca}".lis
if ! $test "$deca_file" > "$result_file" 2>&1
then
    echo -e "${RED}[ECHEC]  $deca_file: Echec inattendu pour $test:${NC}"
    cat $result_file | sed 's/^/         /'
    exit 1
fi

# Tableau contenant les règles détectées (et position s'il y a)
number_ignored_lines=0
declare -a detected_rules
while IFS= read -r line
do
    # Pour toutes les lignes contenant '>' on récup le premier mot
    # qui est du vocab terminal ou un nom de liste d'arbres (ex. ListDeclVar)
    # Sinon cela veut dire que la ligne correspond à une décoration, on récupère le dernier mot (type)
    # (mot sous-entend uniquement lettres)
    line=$(echo $line | grep -v DEBUG)
    if echo "$line" | grep -q '>'; then
        relevant_word=$(echo "$line" | grep -o "[A-Za-z]\+" | head -1)
    else
        relevant_word=$(echo "$line" | grep -o "[A-Za-z]\+" | tail -n1)
    fi
    
    pos_err=$(echo "$line" | grep -o "\[[0-9]\+, [0-9]\+\]" | head -1 | tr -d " " | tr ',' ':' | sed 's/^.//;s/.$//')
    rule=$(echo ""$relevant_word $pos_err"" | sed 's/\s*$//')   # Supprime le dernier élément s'il est un espace
    if [ -n "$rule" ]; then detected_rules+=("$rule"); else ((number_ignored_lines++)); fi
done < "$result_file"

# Tableau contenant les règles attendues (et position si nécessaire)
declare -a expected_rules
while IFS= read -r line
do
    expected_rules+=("$line")
done < <(awk 'NF && NR > 1 { print $2 " " $3 } /^\/\/[[:space:]]*$/{ exit }' "$deca_file" | head -n -1 | sed 's/\s*$//')


# Vérifie qu'il y a autant de règles détectées qu'attendues
if [ "${#detected_rules[@]}" -ne "${#expected_rules[@]}" ]
then
    echo -e "${RED}[ECHEC]  $deca_file: L'arbre généré en sortie du parser possède ${#detected_rules[@]} lignes, ${#expected_rules[@]} sont attendues${NC}"
    echo "         Arbre généré:"
    cat $result_file | sed 's/^/         /'
    exit 1
fi


# Compare les règles attendues et détectées unes par unes
for i in $(seq 0 1 ${#expected_rules[@]})
do
    if [ "${detected_rules[i]}" != "${expected_rules[i]}" ]
    then
        line_in_tree=(expr $i + 1)
        problematic_line_in_file=$(expr $i + 1 + $number_ignored_lines)
        echo -e "${RED}[ECHEC]  $deca_file: Ligne $line_in_tree de l'arbre généré non conforme ('${expected_rules[i]}' attendu, '${detected_rules[i]}' obtenu)${NC}"
        echo "         Arbre généré: "
        sed 's/^/         /' $result_file | awk -v line="$problematic_line_in_file" -v red=${RED} -v nc=${NC} '{print (NR == line) ? red $0 "      <------------------" nc : $0}'
        exit 1
    fi
done
echo -e "${GREEN}[SUCCES] $deca_file: Règles attendues détectées${NC}"