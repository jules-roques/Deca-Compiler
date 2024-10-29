# #! /bin/sh

# # Auteur : gl44
# # Version initiale : 01/01/2024

# # Test minimaliste de la vérification contextuelle.
# # Le principe et les limitations sont les mêmes que pour basic-synt.sh
# cd "$(dirname "$0")"/../../.. || exit 1

# PATH=./src/test/script/launchers:"$PATH"

# if test_context src/test/deca/context/invalid/provided/affect-incompatible.deca 2>&1 | \
#     grep -q -e 'affect-incompatible.deca:15:'
# then
#     echo "Echec attendu pour test_context"
# else
#     echo "Succes inattendu de test_context"
   
# fi

# if test_context src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
#     grep -q -e 'hello-world.deca:[0-9]'
# then
#     echo "Echec inattendu pour test_context"
#     exit 1
# else
#     echo "Succes attendu de test_context"
# fi






# #! /bin/sh

# # Author: gl44
# # Initial version: 01/01/2024
# # Modified to use 'find' for testing all .deca files in specified directories

# # Script to test all .deca files within valid and invalid directories using 'find'.
# cd "$(dirname "$0")"/../../.. || exit 1

# VALID_PATH="src/test/deca/context/valid/created/avec_objet" # Replace with the actual path for valid files
# INVALID_PATH="src/test/deca/context/invalid/created" # Replace with the actual path for invalid files
# PATH=./src/test/script/launchers:"$PATH"

# # Function to test .deca files and report success or failure
# test_deca_files() {
#     for deca_file in "$@"; do
#         if test_context "$deca_file" 2>&1 | grep -q -e "$deca_file:[0-9]"; then
#             echo "Echec inattendu pour test_context avec $deca_file"
#             # Uncomment to exit on first failure
#             # exit 1
#         else
#             echo "Succes attendu de test_context avec $deca_file"
#         fi
#     done
# }

# # Find and test all valid .deca files
# echo "Testing valid .deca files:"
# find "$VALID_PATH" -name "*.deca" -exec sh -c 'test_deca_files "$@"' find-sh {} +

# # Find and test all invalid .deca files
# echo "Testing invalid .deca files:"
# # find "$INVALID_PATH" -name "*.deca" -exec sh -c 'test_deca_files "$@"' find-sh {} +



#! /bin/sh

# Auteur : gl44
# Version initiale : 01/01/2024
# Modified to use 'find' for testing all .deca files in a specific directory

# Script to test all .deca files within a directory using 'find'.
cd "$(dirname "$0")"/../../.. || exit 1

PATH_TO_VALID_DECA_FILES="src/test/deca/context/valid" 
PATH_TO_INVALID_DECA_FILES="src/test/deca/context/invalid"
PATH=./src/test/script/launchers:"$PATH"

# Use 'find' to recursively test all .deca files in the specified directory
find "$PATH_TO_VALID_DECA_FILES" -name "*.deca" -exec sh -c '
    for deca_file do
        # Test each .deca file and check for errors
        if test_context "$deca_file" 2>&1 | grep -q -e "$deca_file:[0-9]"; then
            echo "Echec inattendu pour test_context avec $deca_file"
            # Uncomment the next line if you want the script to exit on the first failure
            # exit 1
        else
            echo "Succes attendu de test_context avec $deca_file"
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
