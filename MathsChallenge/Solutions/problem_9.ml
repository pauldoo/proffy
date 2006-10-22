(* http://mathschallenge.net/index.php?section=project&ref=problems&id=9 *)

let int_sqrt a = 
    int_of_float(sqrt(float_of_int a));;

let is_square a =
    let b = int_sqrt a in
        b * b = a;;

let rec outer a =
    let rec inner b =
        if (is_square(a*a + b*b) && (int_sqrt(a*a + b*b) + a + b) = 1000) then
            [a; b]
        else
            if b > a then
                inner (b-1)
            else
                []
    in
        let k = inner 1000 in
            match k with
                [] -> if a > 1 then outer (a-1) else []
            |   h :: t -> k;;

match (outer 1000) with
    a :: b :: t -> let c = (1000 - (a + b)) in print_int(a * b * c)
|   k -> print_string("failed");;

