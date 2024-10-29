#!/bin/sh

# This script performs some tests that all compilers should pass.
# The code is intentionally not very readable; it is not an example
# to follow for writing clean tests.
#
die () {
    echo "\033[1;31mERROR:\033[0m $@"
    exit 1
}

if [ "$GL_EVALUATION_ONGOING" = "" ]; then
    PATH=$(dirname "$0")/../../../src/main/bin:"$PATH"
fi

DECAC=$(cd "$(dirname "$(command -v decac)")" && pwd)/decac

[ -x "$DECAC" ] || die "decac command not found"
echo "Testing the executable:"
echo "$DECAC"

test_dir=$(dirname "$DECAC")/../../test/deca
cd "$test_dir" 2> /dev/null || die "Directory $test_dir not accessible"

(cd "$test_dir" && find . -name '*.deca' | egrep -v '^[0-9ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_/.+-]*$' 2> /dev/null) && die "Illegal characters in test or directory names"

decac () {
    (ulimit -t 10 -f 100 2>/dev/null || true;
     "$DECAC" "$@" 2>&1
    )
}

tmpdir=.minimal-test.$$
mkdir "$tmpdir" || die "Unable to create the test directory"

(
    cd "$tmpdir"
    mkdir source || die "Unable to create a directory in $PWD"
    echo '{println(}' > source/syntax-ko.deca
    echo '{println(x);}' > source/syntax-ok-verif-ko.deca
    echo '{println("Hello");}' > source/hello.deca

    decac -p source/hello.deca > hello-p.deca || die "Failed to compile a simple Deca program with -p"
    [ -s hello-p.deca ] || die "decac -p did not generate output on a simple Deca program"
    decac -p hello-p.deca >/dev/null || die "decac -p produced output that is not a valid Deca program"
    
    ! decac -p source/syntax-ko.deca > sortie.txt 2>&1 || die "decac -p did not exit with a status different from 0 on an incorrect program"
    grep -qi "source/syntax-ko.deca:1:" sortie.txt || die "decac -p did not display an error message correctly"

    decac -p source/syntax-ok-verif-ko.deca > sortie.txt 2>&1 || die "decac -p failed on an invalid but syntactically correct program"
    [ -s sortie.txt ] || die "decac -p did not produce output on an invalid but syntactically correct program"
    
    ! grep -qi "source/syntax-ok-verif-ko.deca:1:" sortie.txt || die "decac -p displayed an error on a syntactically correct program"
    
    decac -v source/hello.deca > sortie.txt 2>&1 || die "Failed to compile a simple Deca program with -v"
    [ ! -s sortie.txt ] || die "decac -v produced output on a correct file"
    
    ! decac -v source/syntax-ok-verif-ko.deca > sortie.txt 2>&1 || die "decac -v did not fail on an undeclared variable"
    [ -s sortie.txt ] || die "decac -v did not produce output on an undeclared variable"
    grep -qi "source/syntax-ok-verif-ko.deca:1:" sortie.txt || die "decac -v did not display an error message on an undeclared variable"
    
    decac source/hello.deca > sortie.txt 2>&1 || die "Failed to compile a simple Deca program"
    [ ! -s sortie.txt ] || die "deca produced output on a correct program"
    [ ! -f hello.ass ] || die "decac generated the .ass file in the current directory. It should be generated in the same location as the source file."
    [ -f source/hello.ass ] || die "decac did not generate the .ass file in the correct location"

    ima source/hello.ass > sortie || die "Error executing the generated code"
    echo "Hello" > expected
    diff sortie expected || die "Execution of the generated code for hello-world is incorrect"

    echo "Basic features seem to be operational."
)

status=$?
rm -fr "$tmpdir"
exit "$status"
