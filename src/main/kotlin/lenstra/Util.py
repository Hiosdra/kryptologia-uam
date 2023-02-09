import gmpy2

def ExcInfo(ex):
    return f'{type(ex).__name__}: {ex}'


class InvError(Exception):
    def __init__(self, v):
        self.value = v

def Int(x):
    return gmpy2.mpz(x)

def Print(*pargs, **nargs):
    print(*pargs, **{'flush': True, **nargs})
