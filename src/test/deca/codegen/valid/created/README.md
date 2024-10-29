On écrit dans ce dossier des programmes deca valides pour les compiler et tester leur exécution par ima



Pour chaque fichier deca créé dans ce dossier, écrire un programme de même nom dont l'extension est ".exp" (expected) qui contient le résultat supposé de l'exécution en assembleur

Pour les fichiers deca n'écrivant pas sur la sortie standard, on peut ne pas créer de fichier ".exp" correspondant au ".deca".


Exemple:

Fichier "cond0.deca"
```
// Résultat exécution ima
//    ok
//
// Description:
//    tests d'exécution d'un programme correct par ima
//
// Auteur:
//    Jules
//
// Historique:
//    cree le 15/01/2024

{
   if (1 >= 0) {
      println("ok"); 
   } else { 
      println("erreur");
   }
}
```

Fichier "cond0.exp"
```
ok
```