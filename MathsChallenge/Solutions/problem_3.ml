(* http://mathschallenge.net/index.php?section=project&ref=problems&id=3 *)

let rec isdivisible n p =
    match p with
        [] -> false
    |   h :: t -> if (Int64.rem n h) = 0L then true else isdivisible n t;;

let rec factorout n p =
    if (Int64.rem n p) = 0L then
        factorout (Int64.div n p) p
    else
        n;;

let rec generate n p r =
    if isdivisible n p then
        generate (Int64.succ n) p r
    else
        (* n is a new prime *)
        let m = factorout r n in
            (if not (r = m) then (print_string(Int64.to_string n); print_string("\n")));
            (if not (m = 1L) then generate (Int64.succ n) (n :: p) m);;

(* print_string(Int64.to_string 317584931803L);; *)

let target = 317584931803L;;
generate 2L [] target;;

