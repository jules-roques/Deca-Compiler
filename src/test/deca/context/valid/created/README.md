Pour chaque fichier ".deca" créé dans ce répertoire, suivre le forme de programme suivante. En particulier, il est important d'écrire à partir de la deuxième ligne en commentaires:
- chaque mot terminal de la syntaxe abstraite attendu, un par ligne, avec sa position dans le fichier
- les listes d'arbres telles que ListDeclVar (pas d'indication de position dans ce cas)
- les décorations (en minuscule) avec pour les variables leur position de déclaration 

Ne pas respecter cette forme mène à des erreurs dans les scripts test.

```
// Règles attendues (occurences dans lignes du fichier résultat):
//  Program 34:0
//      ListDeclClass
//      Main 34:0
//          ListDeclVar
//              DeclVar 35:8
//                  Identifier 35:4
//                  int
//                  Identifier 35:8
//                  int 35:8
//                  Initialization 35:12
//                      Int 35:12
//                      int
//          ListInst
//              Assign 36:4
//              int
//                  Identifier 36:4
//                  int 35:8
//                      Plus 36:10
//                      int
//                          Identifier 36:8
//                          int 35:8 
//                          Int 36:12
//                          int
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