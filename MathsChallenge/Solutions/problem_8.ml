(* http://mathschallenge.net/index.php?section=project&ref=problems&id=8 *)

let rec go a b c d =
    try
        let e = read_int() in
            (a * b * c * d * e) :: (go b c d e)
    with
        End_of_file -> []
    |   Failure t -> (go a b c d);;

let rec max f =
    match f with
        [] -> -1
    |   h :: t -> let r = (max t) in if r > h then r else h;;

print_int(max(go 0 0 0 0));;

