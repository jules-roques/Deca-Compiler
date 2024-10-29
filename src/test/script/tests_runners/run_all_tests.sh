#! /bin/sh

# Auteur : Jules
# Version initiale : 12/01/2024

# Lance tous les tests

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../../.. || exit 1

# Chemin vers les scripts que l'on utilise
PATH=./src/test/script/tests_runners:./src/main/bin:"$PATH"

# Couleurs pour affichage
BICYAN='\033[1;96m'
NC='\033[0m' # No Color

decac -b

echo "${BICYAN} ___ ___       __   ___         ${NC}"
echo "${BICYAN}|__   |   /\\  |__) |__      /\\  ${NC}"
echo "${BICYAN}|___  |  /~~\\ |    |___    /~~\\ ${NC}"
echo
stepA_run_tests.sh
echo

echo "${BICYAN} ___ ___       __   ___     __  ${NC}"
echo "${BICYAN}|__   |   /\\  |__) |__     |__) ${NC}"
echo "${BICYAN}|___  |  /~~\\ |    |___    |__) ${NC}"
echo
stepB_run_tests.sh
echo

echo "${BICYAN} ___ ___       __   ___     __  ${NC}"
echo "${BICYAN}|__   |   /\\  |__) |__     /  \` ${NC}"
echo "${BICYAN}|___  |  /~~\\ |    |___    \\__, ${NC}"
echo 
stepC_run_tests.sh
echo

echo "${BICYAN} __   ___  __        __        ${NC}"
echo "${BICYAN}|  \\ |__  /  \`  /\\  /  \`       ${NC}"
echo "${BICYAN}|__/ |___ \\__, /~~\\ \\__,       ${NC}"
echo
decac_run_tests.sh
echo

echo "${BICYAN}___  __     __   __  ${NC}"
echo "${BICYAN} |  |__) | / __ /  \\ ${NC}"
echo "${BICYAN} |  |  \\ | \\__/ \__/ ${NC}"
echo
trigo_run_tests.sh