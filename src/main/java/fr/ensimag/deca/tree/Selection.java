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
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

public class Selection extends AbstractLValue {
    private AbstractExpr classCalled;
    private AbstractIdentifier fieldCalled;

    public Selection(AbstractExpr classCalled, AbstractIdentifier fieldCalled) {
        Validate.notNull(classCalled);
        Validate.notNull(fieldCalled);
        this.classCalled = classCalled;
        this.fieldCalled = fieldCalled;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type classType = classCalled.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition classDef = compiler.getEnvironmentTypes().defOfType(classType.getName());
        // verification que la sélection se fait sur une classe
        if (!(classDef instanceof ClassDefinition)) {
            throw new ContextualError("Sélection sur un type non-classe (règle 3.41)", getLocation());
        }
        // vérification que le champ existe dans un environment parent
        Type type = fieldCalled.verifyExpr(compiler, ((ClassDefinition)classDef).getMembers(), currentClass);
        
        ExpDefinition fieldDef = fieldCalled.getExpDefinition();
        
        if (fieldDef instanceof FieldDefinition) {
            Visibility fieldVisib = ((FieldDefinition)fieldDef).getVisibility();
            if (fieldVisib == Visibility.PROTECTED) {
                ClassDefinition fieldClassDef = ((FieldDefinition)fieldDef).getContainingClass();
                ClassType fieldClassType = fieldClassDef.getType();
                ClassType currenClassType = currentClass.getType();
                if (!(currenClassType.isSubClassOf(fieldClassType))) {
                    throw new ContextualError("Accès incorrect à un champ protégé depuis une classe non parente (règle 3.70)", getLocation());
                }
            }
        } else {
            throw new ContextualError("Sélection sur autre chose qu'un champ", getLocation());
        }
        
        this.verifyRValue(compiler, localEnv, currentClass, type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classCalled.decompile(s);
        s.print(".");
        fieldCalled.decompile(s);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("L'affichage de champs n'est pas supporté.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classCalled.iter(f);
        fieldCalled.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classCalled.prettyPrint(s, prefix, true);
        fieldCalled.prettyPrint(s, prefix, true);
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("a faire");
    }
}