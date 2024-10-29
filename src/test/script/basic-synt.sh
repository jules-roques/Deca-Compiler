# #! /bin/sh

# # Auteur : gl44
# # Version initiale : 01/01/2024

# # Test minimaliste de la syntaxe.
# # On lance test_synt sur un fichier valide, et les tests invalides.

# # dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# # d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# # exemple, en stoquant la valeur attendue quelque part, et en
# # utilisant la commande unix "diff".
# #
# # Il faudrait aussi lancer ces tests sur tous les fichiers deca
# # automatiquement. Un exemple d'automatisation est donné avec une
# # boucle for sur les tests invalides, il faut aller encore plus loin.

# cd "$(dirname "$0")"/../../.. || exit 1

# PATH=./src/test/script/launchers:"$PATH"

# # exemple de définition d'une fonction
# test_synt_invalide () {
#     # $1 = premier argument.
#     # On stocke les résultats de l'exécution du test dans un fichier
#     result_file_name="${1%.deca}".lis
#     test_synt "$1" > "$result_file_name" 2>&1

#     # On récupère la ligne supposée de l'erreur
#     theorical_err_lign=$(sed -n '2p' $1 | grep -o "[0-9]\+" | head -n 1)

#     # Ligne d'erreur détectée (peut être vide s'il n'y a pas d'erreur)
#     err_lign=$(cat "$result_file_name" | grep -o "$1:[0-9]\+:" | grep -o "[0-9]\+")

#     # S'il y a une erreur (si err_lign n'est pas vide)
#     if [ -n "$err_lign" ]
#     then
#         if [ "$err_lign" = "$theorical_err_lign" ]
#         then
#             echo "Echec attendu pour test_synt sur $1 à la ligne $err_lign."
#         else
#             echo "Echec attendu ligne $theorical_err_lign mais détecté ligne $err_lign sur $1."
#             exit 1
#         fi
#     else
#         echo "Succes inattendu de test_synt sur $1."
#         exit 1
#     fi
# }    

# for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
# do
#     test_synt_invalide "$cas_de_test"
# done


# if test_synt src/test/deca/syntax/valid/provided/hello.deca 2>&1 | \
#     grep -q -e ':[0-9][0-9]*:'
# then
#     echo "Echec inattendu pour test_synt sur src/test/deca/syntax/valid/provided/hello.deca"
#     exit 1
# else
#     echo "Succes attendu de test_synt sur src/test/deca/syntax/valid/provided/hello.deca"
# fi



#! /bin/sh

# Auteur : gl44
# Version initiale : 01/01/2024
# Modified to use 'find' for testing all .deca files in a specific directory

# Script to test all .deca files within a directory using 'find'.
cd "$(dirname "$0")"/../../.. || exit 1

PATH_TO_VALID_DECA_FILES="src/test/deca/syntax/valid"
PATH_TO_INVALID_DECA_FILES="src/test/deca/syntax/invalid/created" 
PATH=./src/test/script/launchers:"$PATH"

# Use 'find' to recursively test all .deca files in the specified directory
find "$PATH_TO_VALID_DECA_FILES" -name "*.deca" -exec sh -c '
    for deca_file do
        # Test each .deca file and check for errors
        if test_syntax "$deca_file" 2>&1 | grep -q -e "$deca_file:[0-9]"; then
            echo "Echec inattendu pour test_syntax avec $deca_file"
            # Uncomment the next line if you want the script to exit on the first failure
            # exit 1
        else
            echo "Succes attendu de test_syntax avec $deca_file"
        fi
    done
' find-sh {} +


find "$PATH_TO_INVALID_DECA_FILES" -name "*.deca" -exec sh -c '
    for deca_file do
        # Test each .deca file and check for errors
        if test_context "$deca_file" 2>&1 | grep -q -e "$deca_file:[0-9]"; then
            echo "Echec attendu pour test_context avec $deca_file"
            # Uncomment the next line if you want the script to exit on the first failure
            # exit 1
        else
            echo "Succes inattendu de test_context avec $deca_file"
        fi
    done
' find-sh {} +


