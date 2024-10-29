#!/usr/bin/python3

import subprocess

# Current energy counter in micro joules
commande = "cat /sys/class/powercap/intel-rapl/intel-rapl\:0/energy_uj"

res = subprocess.run(commande, shell=True, check=True, stdout=subprocess.PIPE, text=True)
val1 = int(res.stdout)
print("Buffer value :")
print(val1)

subprocess.run("java TestMath > /dev/null", shell=True, check=True)

res3 = subprocess.run(commande, shell=True, check=True, stdout=subprocess.PIPE, text=True)
val2 = int(res3.stdout)
print("End value :")
print(val2)

# Used energy in micro joules
diff = val2-val1
print("Used energy in micro joules = ", diff, "µJ")
# Used energy in kiloWatt hour
energy = diff / (3.6*(10**9))
print("Used energy in watt-hour = ", energy)