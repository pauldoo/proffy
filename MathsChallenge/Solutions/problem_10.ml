(* http://mathschallenge.net/index.php?section=project&ref=problems&id=10 *)

(*
let rec makelist a b c =
    if a < b then
        makelist a (b-1) ((b-1) :: c)
    else
        c;;

let rec filterlist n p r =
    match n with
        [] -> r
    |   h :: t ->
            if (h mod p) = 0 then
                filterlist t p r
            else
                filterlist t p (h :: r);;

let rec reverse l r =
    match l with
        [] -> r
    |   h :: t -> reverse t (h :: r);;
                
let rec generate n p =
    match n with
        [] -> p
    |   h :: t ->
            (print_int(h); print_string("\n");
            generate (reverse (filterlist t h []) []) (h :: p));;

let rec sum l f =
    match l with
        [] -> f
    |   h :: t ->
            sum t (f + h);;

print_int (sum (generate (makelist 2 1000000 []) []) 0);;
*)

let rec isdivisible n p =
    match p with
        [] -> false
    |   h :: t ->
            if h*h > n then
                false
            else
                if (n mod h) = 0 then true else isdivisible n t;;

let rec append a b =
    match b with
        [] -> [a]
    |   h :: t -> h :: (append a t)

let rec generate n p k t =
    if n >= k then
        t
    else
        if isdivisible n p then
            generate (n + 2) p k t
        else
            (print_int(n); print_string("\n");
            (generate (n + 2) (append n p) k (n+t)));;

print_int(generate 3 [2] 1000000 0);;



