(* http://mathschallenge.net/index.php?section=project&ref=problems&id=7 *)

let rec append a b =
    match b with
        [] -> [a]
    |   h :: t -> h :: (append a t);;

let rec isdivisible n p =
    match p with
        [] -> false
    |   h :: t -> if (n mod h) = 0 then true else isdivisible n t;;

let rec generate n p k =
    if isdivisible n p then
        generate (n + 1) p k
    else
        (* n is a new prime *)
        if k = 1 then
            n
        else
            generate (n + 1) (append n p) (k-1);;

print_int(generate 2 [] 10001);;


