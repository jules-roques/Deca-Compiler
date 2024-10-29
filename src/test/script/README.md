# Lancer un test sur un seul ficher deca
Dans le dossier launchers choisir quel test on veut réaliser (lexique, syntaxe, contexte...) et la lancer sur un fichier deca:

Par exemple depuis le répertoire du projet:
```
src/test/script/launchers/test_synt src/test/deca/syntax/valid/created/parser/sans_objet/else_rule.deca
```

# Lancer les tests sur un ensemble de fichiers deca
Dans le dossier tests_runners, chaque script lance les tests sur un ensemble de fichiers deca.

Exemple
```
src/test/script/tests_runners/synt_run_tests.sh
```

# Nettoyer (supprimer les fichiers générés)

Depuis le repertoire du projet
```
./clean
```