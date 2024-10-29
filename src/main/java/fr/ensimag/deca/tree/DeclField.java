package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclField extends AbstractDeclField {
    
    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(visibility.name().toLowerCase());
        s.print(" ");
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);

    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, int index) throws ContextualError {
        // Vérification du type
        Type t = type.verifyType(compiler, false);

        // vérification de redéfinition dans la classe courante
        ExpDefinition varDef = localEnv.get(varName.getName());
        if (varDef != null) {
            throw new ContextualError("nom de champ déjà déclaré (règle 2.4)", getLocation());
        }

        // vérification de redéfinition dans la classe hérité
        EnvironmentExp superEnv = currentClass.getSuperClass().getMembers();
        varDef = superEnv.get(varName.getName());
        if (varDef != null) {
            if (varDef instanceof FieldDefinition) {
                Type superVarType = varDef.getType();
                // type de retour est un sous-type du type de retour de la méthode héritée
                if (t.isClass() && superVarType.isClass()) {
                    if(!((ClassType)t).isSubClassOf((ClassType)superVarType)) {
                        throw new ContextualError("type de champ non conforme lors de la redéfinition (règle 2.7)", getLocation());
                    }
                } else if (!(t.sameType(superVarType))) {
                    throw new ContextualError("type de champ non conforme lors de la redéfinition (règle 2.7)", getLocation());
                }
            } else {
                throw new ContextualError("redéfinition d'une méthode en attribut (règle 2.4)", getLocation());
            }
        }

        varDef = new FieldDefinition(t, getLocation(), visibility, currentClass, index);
        
        // définition de la variable
        varName.setDefinition(varDef);
        varName.setType(t);

        try {
            // ajout de la variable dans l'EnvironmentExp   
            localEnv.declare(varName.getName(), varDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + varName.getName() + " is already defined in the current scope", varName.getLocation());
        }

        varName.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void verifyFieldInitialization(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Vérification du type
        Type t = type.verifyType(compiler, false);

        // Vérification de l'initialisation
        initialization.verifyInitialization(compiler, t , localEnv, currentClass);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, false);
    }

    protected void prettyPrintVisibility(PrintStream s, String prefix) {
        s.print("[visibility=" + visibility + "] ");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
}
