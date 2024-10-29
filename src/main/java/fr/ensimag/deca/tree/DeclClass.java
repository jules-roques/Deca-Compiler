package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl44
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {

    final private AbstractIdentifier className;
    final private AbstractIdentifier superClass;
    final private ListDeclField listOfFields;
    final private ListDeclMethod listOfMethods;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclField listOfFields, ListDeclMethod listOfMethods) {
        Validate.notNull(className);
        // Validate.notNull(superClass);
        Validate.notNull(listOfFields);
        Validate.notNull(listOfMethods);
        this.className = className;
        this.superClass = superClass;
        this.listOfFields = listOfFields;
        this.listOfMethods = listOfMethods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        className.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
        s.println(" {");
        s.indent();
        listOfFields.decompile(s);
        listOfMethods.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // Vérification de la superClass
        this.superClass.verifyClassExpr(compiler);

        // vérification si la classe n'existe pas déjà 
        Symbol classSymb = compiler.createSymbol(className.getName().toString());
        ClassDefinition classDef = (ClassDefinition)compiler.getEnvironmentTypes().defOfType(classSymb);
        if (classDef != null) {
            throw new ContextualError("classe ou type déjà déclaré (règle 1.3)", getLocation());
        }
        
        // Création du type correspondant à la classe
        ClassType classType = new ClassType(classSymb, getLocation(), superClass.getClassDefinition());
        classDef = new ClassDefinition(classType, getLocation(), superClass.getClassDefinition());
        
        // ajout de ce type dans l'environment des types
        compiler.getEnvironmentTypes().put(classSymb, classDef);

        // définition de la classe
        this.className.setDefinition(classDef);
        this.className.setType(classType);
        
        // vérification de la classe
        this.className.verifyClassExpr(compiler);

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        this.listOfFields.verifyListDeclField(compiler, this.className.getClassDefinition());
        this.listOfMethods.verifyListDeclMethod(compiler, this.className.getClassDefinition());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        this.listOfFields.verifyListFieldInitialization(compiler, this.className.getClassDefinition());
        this.listOfMethods.verifyListMethodBody(compiler, this.className.getClassDefinition());
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        listOfFields.prettyPrint(s, prefix, true);
        listOfMethods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        superClass.iter(f);
        listOfFields.iter(f);
        listOfMethods.iter(f);
    }

}
