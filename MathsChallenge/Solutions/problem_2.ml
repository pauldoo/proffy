(* http://mathschallenge.net/index.php?section=project&ref=problems&id=2 *)

let fib n =
    let rec f a b n =
        if n = 0 then a
        else f b (a+b) (n-1)
    in
        f 0 1 n;;

let total n =
    let rec f t m =
        let
            k = fib(m)
        in
            if k < n then
                f (t+(if (k mod 2) = 0 then k else 0)) (m+1)
            else
                t
    in
        f 0 1;;

print_int(total 1000000);;


