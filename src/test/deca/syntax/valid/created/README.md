Pour chaque fichier ".deca" créé dans ce répertoire, suivre le forme de programme suivante. En particulier, il est important d'écrire à partir de la deuxième ligne en commentaires :
- chaque mot terminal de la syntaxe abstraite attendu, un par ligne, avec sa position dans le fichier
- les listes d'arbres telles que ListDeclVar (pas d'indication de position dans ce cas)

Ne pas respecter cette forme mène à des erreurs dans les scripts test.

```
// Règles attendues (occurences dans lignes du fichier résultat):
//  Program 26:0
//      ListDeclClass
//      Main 26:0
//          ListDeclVar
//              DeclVar 27:8
//                  Identifier 27:4
//                  Identifier 27:8
//                  Initialization 27:12
//                      Int 27:12
//          ListInst
//              Assign 28:4
//                  Identifier 28:4
//                      Plus 28:10
//                      Identifier 28:8
//                      Int 28:12
//
// Auteur: Aymeric, Jules
//
// Historique:
//    cree le 10/01/2024
//
// Description:
//    test de programme contextuellement correct.

{
    int i = 1;
    i = i + 1;
}
```