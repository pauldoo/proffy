(* http://mathschallenge.net/index.php?section=project&ref=problems&id=1 *)

let mcp(m, n) =
    let f = n / m in m * f * (f+1) / 2;;

let total(n) =
    mcp(3, n-1) + mcp(5, n-1) - mcp(15, n-1);;

print_int(total(1000));;

