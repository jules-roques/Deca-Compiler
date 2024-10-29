On range les **fichiers deca tests de la règle X.X dans le dossier correspondant 'ruleX.X'**

Pour chaque fichier deca créé dans les sous-dossiers correspondant à différentes passes (rule1.1, etc...), suivre la forme ci dessous.
- indiquer en commentaire à la **deuxième ligne** la ligne d'erreur du programme
- éventuellement indiquer **troisième ligne** une chaine que l'on veut voir apparaître dans le message d'erreur

Exemple:

```
// Résultat erreur:
//    15:0
//    identificateur non déclaré (règle 0.1)
//
// Description:
//    tests de programme incorrect contextuellement (identificateur inconnu)
//
// Auteur:
//    Jules
//
// Historique:
//    cree le 13/01/2024

{
    println(i);
}
```