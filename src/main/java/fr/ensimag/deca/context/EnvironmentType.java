package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl44
 * @date 01/01/2024
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // envTypes.put(stringSymb, new TypeDefinition(STRING, Location.BUILTIN));

        // définition de la classe Object
        Symbol objectSymb = compiler.createSymbol("Object");
        ClassType objectType = new ClassType(objectSymb, Location.BUILTIN, null);
        ClassDefinition classDef = new ClassDefinition(objectType, Location.BUILTIN, null);
        Identifier object = new Identifier(objectSymb);
        object.setDefinition(classDef);
        envTypes.put(objectSymb, classDef);
        
    }

    @Override
    public String toString() {
        StringBuilder affiche = new StringBuilder("Affiche Environment Type\n");
        for (Symbol name : envTypes.keySet()) {
            affiche.append("Nom Symbol : " + name.toString() + "\n");
            affiche.append("Type Definition : " + envTypes.get(name).toString() + "\n");
        }
        return affiche.toString();
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public  Map<Symbol, TypeDefinition> getMap() {
        return envTypes;
    }

    public void put(Symbol s, TypeDefinition typeDef) {
        envTypes.put(s, typeDef);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
}
