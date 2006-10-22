(* http://mathschallenge.net/index.php?section=project&ref=problems&id=4 *)

let rec reverse a b =
    if a = 0 then
        b
    else
        let d = a mod 10 in
            reverse (a / 10) (b * 10 + d);;

let palindromify3 n =
    n * 1000 + reverse n 0;;

let isproduct n =
    let rec check n m =
        if (n / m) >= 1000 then
            false
        else
            if (n mod m) = 0 then
                true
            else
                check n (m-1)
    in
        check n 999;;

let rec search n =
    let p = palindromify3 n in
        if isproduct p then
            p
        else
            search (n-1);;

print_int(search 999);;

