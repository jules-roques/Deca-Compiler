package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

public class MethodCall extends AbstractExpr {
    private AbstractExpr classCalled;
    private AbstractIdentifier methodCalled;
    private ListExpr paramsMethod;

    public MethodCall(AbstractExpr classCalled, AbstractIdentifier methodCalled, ListExpr paramsMethod) {
        Validate.notNull(classCalled);
        Validate.notNull(methodCalled);
        Validate.notNull(paramsMethod);
        this.classCalled = classCalled;
        this.methodCalled = methodCalled;
        this.paramsMethod = paramsMethod;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        classCalled.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition classDef = compiler.getEnvironmentTypes().defOfType(classCalled.getType().getName());
        // verification de type de la variable
        if (!(classDef instanceof ClassDefinition)) {
            throw new ContextualError("Appel de méthode sur un type non-classe (règle 3.41)", getLocation());
        }
        Type t = methodCalled.verifyMethodExpr(compiler, (ClassDefinition)classDef, currentClass);
        
        // verification de la signature
        Type type;
        Signature signVerif = new Signature();
        for (AbstractExpr e : paramsMethod.getList()) {
            type = e.verifyExpr(compiler, localEnv, currentClass);
            signVerif.add(type);
        }
        // lance les erreurs de mauvais paramètres
        MethodDefinition methDef = methodCalled.getMethodDefinition();
        signVerif.verifySignature(methDef);
        this.verifyRValue(compiler, localEnv, currentClass, t);
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classCalled.decompile(s);  //J'ai pas géré le cas où il n'y a pas de "classCalled"
        s.print(".");
        methodCalled.decompile(s);
        s.print("(");
        paramsMethod.decompile();
        s.print(")");
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Print method call not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classCalled.iter(f);
        methodCalled.iter(f);
        paramsMethod.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classCalled.prettyPrint(s, prefix, true);
        methodCalled.prettyPrint(s, prefix, true);
        paramsMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected GPRegister codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("a faire");
    }
}