package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclMethod extends AbstractDeclMethod {
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listOfParams;
    final private AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam listOfParams, AbstractMethodBody methodBody) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listOfParams);
        Validate.notNull(methodBody);
        this.type = type;
        this.methodName = methodName;
        this.listOfParams = listOfParams;
        this.methodBody = methodBody;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        listOfParams.decompile(s);
        s.print(")");
        methodBody.decompile(s);
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, int index) throws ContextualError {
        // Vérification du type
        Type t = type.verifyType(compiler, true);
        
        // calcul de la signature 
        Signature sign = new Signature();
        this.listOfParams.verifyListParam(compiler, localEnv, currentClass, sign);
        // vérification de redéfinition dans la même classe
        ExpDefinition methodDef = localEnv.get(methodName.getName());
        if (methodDef != null) {
            throw new ContextualError("nom de méthode déjà déclaré (règle 2.7)", getLocation());
        }
        // vérification de redéfinition dans la classe hérité
        EnvironmentExp superEnv = currentClass.getSuperClass().getMembers();
        methodDef = superEnv.get(methodName.getName());
        if (methodDef != null) {
            if (methodDef instanceof MethodDefinition) {
                Type methType = methodDef.getType();
                // type de retour est un sous-type du type de retour de la méthode héritée
                if (t.isClass() && methType.isClass()) {
                    if(!((ClassType)t).isSubClassOf((ClassType)methType)) {
                        throw new ContextualError("type de retour non conforme lors de la redéfinition (règle 2.7)", getLocation());
                    }
                } else if (!(t.sameType(methType))) {
                    throw new ContextualError("type de retour non conforme lors de la redéfinition (règle 2.7)", getLocation());
                }
                // même signature que la méthode héritée
                sign.verifySignature((MethodDefinition)methodDef);
            } else {
                throw new ContextualError("redéfinition d'un attribut en méthode (règle 2.4)", getLocation());
            }
        }
        
        methodDef = new MethodDefinition(t, getLocation(), sign, index);
        
        // définition de la method
        methodName.setDefinition(methodDef);
        methodName.setType(t);

        try {
            // ajout de la method dans l'EnvironmentExp   
            localEnv.declare(methodName.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + methodName.getName() + " is already defined in the current scope", methodName.getLocation());
        }

        methodName.verifyExpr(compiler, localEnv, currentClass);
        
    }
    
    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Vérification du type
        Type t = type.verifyType(compiler, true);
        this.methodBody.verifyMethodBody(compiler, localEnv, currentClass, t);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listOfParams.prettyPrint(s, prefix, true);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        listOfParams.iter(f);
        methodBody.iter(f);
    }
}
