(defun last-character (x)
    (char x (- (length x) 1)))

(defun first-character (x)
    (char x 0))

(defun cons-if-non-nil (head tail)
    "Cons head onto the front of tail iff head is non-nil."
    (if (equal head nil)
        tail
        (cons head tail)
        )
    )

(defun search-replace-all (x a b i r)
    (if (<= (+ i (length a)) (length x))
        (search-replace-all x a b (+ i 1)
            (cons-if-non-nil
                (if
                    (equal (subseq x i (+ i (length a))) a)
                    (concatenate 'string (subseq x 0 i) b (subseq x (+ i (length a))))
                    )
                r
                )
            )
        r
        )
    )

(defun list-contains (a b)
    "Returns true if a is found in the list b."
    (if (equal b nil)
        nil
        (if (equal a (car b))
            t
            (list-contains a (cdr b))
            )
        )
    )

(defun set-subtract (a b)
    "Returns the set of elements in a that are not in b (duplicates from a are also removed)"
    ;(format t "A: ~A~%" a)
    ;(format t "B: ~A~%" b)
    (if (equal a nil)
        ()
        (cons-if-non-nil
            (if (list-contains (car a) b)
                nil
                (car a))
            (set-subtract (cdr a) (cons (car a) b))
            )
        )
    )

(defun sort-shortest-first (x)
    (sort x
        (lambda (a b)
            (< (length a) (length b))
            )
        )
    )

(defun rule-1 (x)
    "xI => xIU"
    (if (equal (last-character x) #\I)
        (list (concatenate 'string x "U"))
        )
    )

(defun rule-2 (x)
    "Mx => Mxx."
    (if (equal (first-character x) #\M)
        (let ((tail (subseq x 1)))
            (list (concatenate 'string "M" tail tail)))
        )
    )

(defun rule-3 (x)
    "xIIIy => xUy."
    (search-replace-all x "III" "U" 0 nil)
    )

(defun rule-4 (x)
    "xUUy => xy."
    (search-replace-all x "UU" "" 0 nil)
    )

(defun main (queue previous visited target)
    (let ((curr (car queue)))
        (format t "Current: ~A~%" curr)
        (if (equal curr target)
            nil
            (let ((next
                        (set-subtract
                            (append
                                (rule-1 curr)
                                (rule-2 curr)
                                (rule-3 curr)
                                (rule-4 curr))
                            visited)))
                (main (append (cdr queue) next) previous (append visited next) target)
                )
            )
        )
    )

(main (list "MI") () (list "MI") "MU")


