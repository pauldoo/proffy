(* http://mathschallenge.net/index.php?section=project&ref=problems&id=14 *)

let rec f a b =
    ((*print_string(Int64.to_string a); print_string(", "); assert(a > 0L);*)
    if a = 1L then
        b
    else
        if (Int64.rem a 2L) = 0L then
            f (Int64.div a 2L) (b+1)
        else
            f (Int64.add (Int64.mul a 3L) 1L) (b+1));;

print_int(f 13L 0);;
print_string("\n");;

let rec go num best_num best_length =
    if num = 0 then
        best_num
    else
        let length = (f (Int64.of_int num) 0) in
            if length > best_length then
                go (num -1) num length
            else
                go (num-1) best_num best_length;;
                
print_int(go 999999 0 0);;
print_string("\n");;

