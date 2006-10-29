(* http://mathschallenge.net/index.php?section=project&ref=problems&id=12 *)

let divisors n =
    let rec iter k =
        let k2 = k*k in
            if k2 = n then
                1
            else
                if k2 > n then
                    0
                else
                    (if n mod k = 0 then 2 else 0) + (iter (k+1))
    in
        iter 1;;

print_int(divisors 28);;
print_string("\n");;

let go t=
    let rec iter k n =
        if divisors k > t then
            k
        else
            iter (k+n) (n+1)
    in
        iter 0 1;;

print_int(go 5);;
print_string("\n");;

print_int(go 500);;
print_string("\n");;


